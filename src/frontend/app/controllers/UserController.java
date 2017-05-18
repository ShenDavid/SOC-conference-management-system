package controllers;

import com.avaje.ebeaninternal.server.type.ScalarTypeYear;
import models.Profile;
//import org.hibernate.validator.constraints.Email;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;

import javax.inject.Inject;
import models.User;
import play.mvc.Result;
import play.mvc.Results;

import java.util.*;
import play.data.validation.ValidationError;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import javax.ws.rs.core.MediaType;
import play.mvc.Http.Session;
import play.mvc.Http;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

import java.util.Random;

//import play.libs.Mail;
import org.apache.commons.mail.*;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
/**
 * Created by Ling on 2017/3/27.
 */
public class UserController extends Controller {
    @Inject WSClient ws;

    private FormFactory formFactory;

    @Inject
    public UserController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GO_HOME = Results.redirect(
            routes.ShowConferenceController.showMyConference()
    );

    public Result GO_LOGIN = Results.redirect(
            routes.UserController.login()
    );

    public Result register() {
        Form<User> userForm = formFactory.form(User.class);
        return ok(
                views.html.register.render(userForm, 0)
        );
    }

    /**
     * login when get request for login page
     * @return login html page
     */
    public Result login() {
        Form<User> userForm = formFactory.form(User.class);
        return ok(
                views.html.login.render(userForm, 0)
        );
    }

    public CompletionStage<Result> changepwd(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();
        String password = new_user.username;
        String password_again = new_user.password;

        Session session = Http.Context.current().session();
        String verified = session.get("ChangePwdAuthVerified");

        if(!verified.equals("true")){
            return CompletableFuture.completedFuture(badRequest(views.html.changepwd.render(userForm)));
        }

        if(!password.equals(password_again)){
            return CompletableFuture.completedFuture(badRequest(views.html.changepwd.render(userForm)));
        }

        String username = session.get("tmpusername");
        //Long userid = Long.parseLong(session.get("userid"));
        /*User update_user = User.find.byId(userid);
        try {
            //update_user.password = MD5(password);
            update_user.password = password;
            update_user.update();
        } catch (Exception e){
            e.printStackTrace();
        }
        return GO_HOME;*/
        JsonNode json = Json.newObject()
                .put("username", username)
                .put("password", password);
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/changePwd").post(json);
        return res.thenApplyAsync(response -> {
            JsonNode ret = response.asJson();
            if ("successful".equals(ret.get("status").asText())) {
                System.out.println("Answer right");
                session.clear();
                return GO_LOGIN;
            }else {
                System.out.println("change password unsuccessfully");
                return ok("password change unsuccessfully");
            }
        });
    }

    /**
     * Get request for verify auth for changing password
     * @return
     */
    public Result verifyAuth() {
        Form<User> userForm = formFactory.form(User.class);
        Session session = Http.Context.current().session();
        if(session.get("username")==null)
            return GO_LOGIN;
        return ok(
                views.html.verifyChangePwdAuth.render(userForm, 0)
        );
    }

    public Result sendTemporaryPwd(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();
        //String email = new_user.email;

        //generate temporary password
        Random r = new Random();
        int max = 100000;
        int min = 0;
        String tmp_pwd =  Integer.toString(r.nextInt((max - min) + 1) + min);

        //save tmp password into User
        Session session = Http.Context.current().session();
        String username = session.get("username");
        System.out.println("Send tmp pwd Username "+username);

        //String email = new_user.GetEmailByUsername(username);
        String email = session.get("tmpemail");
        //String email = "linghl0915@163.com";
        System.out.println("Send tmp pwd Email "+email);
        //SendSimpleMessage(email, tmp_pwd);
        //SendEmail.SendEmail(email, tmp_pwd);
        SendEmail(email,tmp_pwd);
        System.out.println("Email sent");

        new_user.AddTemporaryPwd(username, tmp_pwd);

        return ok(
                views.html.temporarypwd.render(userForm)
        );
    }

    public Result verifyTmpPwd(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();
        Session session = Http.Context.current().session();
        String username = session.get("username");
        String tmp_pwd = new_user.password;
        if(new_user.IfTemporaryPwdCorrect(username, tmp_pwd)){
            session.put("ChangePwdAuthVerified", "true");
            return ok(
                    views.html.changepwd.render(userForm)
            );
        }
        return badRequest(views.html.temporarypwd.render(userForm));
    }

    public CompletionStage<Result> verifyQA(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();

        String question1 = new_user.security_question1;
        String answer1 = new_user.security_answer1;
        String question2 = new_user.security_question2;
        String answer2 = new_user.security_answer2;
        String username = new_user.username;
        String email = new_user.email;

        Session session = Http.Context.current().session();
        //String username = session.get("username");

        JsonNode json = Json.newObject()
                .put("security_question1", question1)
                .put("security_question2", question2)
                .put("security_answer1", answer1)
                .put("security_answer2", answer2)
                .put("username",username);
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/verifyChangePwdAuth").post(json);
        return res.thenApplyAsync(response -> {
            JsonNode ret = response.asJson();
            if ("successful".equals(ret.get("status").asText())) {
                System.out.println("Answer right");
                session.put("tmpemail",email);
                session.put("tmpusername", username);
                return redirect("/temporarypwd");
            }else{
                return ok(views.html.verifyChangePwdAuth.render(userForm,1));
            }
        });
    }
    /**
     * For login
     * Verify whether username and password is correct
     * @return To homepage if login success, else stay in login page
     */
    public CompletionStage<Result> verifyUser(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();

        User new_user = userForm.get();
        String username = new_user.username;
        String password = new_user.password;

        JsonNode json = Json.newObject()
                .put("username",username)
                .put("password", password);

        Session session = Http.Context.current().session();
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/login").post(json);
        return res.thenApplyAsync(response -> {
            JsonNode ret = response.asJson();
            if ("successful".equals(ret.get("status").asText())) {
                session.put("username", username);
                session.put("email",ret.get("email").asText());
                String privilege = ret.get("privilege").asText();
                session.put("privilege", privilege);
                session.put("userid", ret.get("userid").asText());
                System.out.println("=====Login ok: privilege is"+privilege);

                System.out.println("In session: "+session.get("username") + session.get("email")+session.get("userid"));
                return GO_HOME;
            }else{
                //return ok("hello world");
                return ok(views.html.login.render(userForm, 1));
            }
        });
    }
    /**
     * Register a user
     */
    public CompletionStage<Result> addUser() {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        System.out.println("Start inserting");

            User new_user = userForm.get();
            String username = new_user.username;
            String password = new_user.password;
            String email = new_user.email;
            String security_question1 = new_user.security_question1;
            String security_question2 = new_user.security_question2;
            String security_answer1 = new_user.security_answer1;
            String security_answer2 = new_user.security_answer2;
            String privilege = "user";

            JsonNode json = Json.newObject()
                    .put("username", username)
                    .put("password", password)
                    .put("email",email)
                    .put("security_question1", security_question1)
                    .put("security_question2", security_question2)
                    .put("security_answer1", security_answer1)
                    .put("security_answer2", security_answer2)
                    .put("privilege", privilege);
            Session session = Http.Context.current().session();
            CompletionStage<WSResponse> res = ws.url("http://localhost:9000/register").post(json);
            return res.thenApplyAsync(response -> {
                JsonNode ret = response.asJson();
                if ("successful".equals(ret.get("status").asText())) {
                    session.put("username", username);
                    session.put("email",email);
                    session.put("userid", ret.get("userid").asText());
                    session.put("privilege", privilege);

                    System.out.println("In session: "+session.get("username") + session.get("email"));
                    return GO_HOME;
                }else{
                    //return ok("hello world");
                    return ok(views.html.register.render(userForm, 1));
                }
            });
    }

    public Result logout(){
        Session session = Http.Context.current().session();
        session.clear();
        return redirect(
                routes.UserController.login()
        );
    }


    private static void SendEmail(String emailto, String pwd){
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("socandrew2017@gmail.com", "ling0915"));
            email.setSSLOnConnect(true);
            email.setFrom("socandrew2017@gmail.com");
            email.setSubject("Temporary password");
            email.setMsg("Dear Sir/Madam, your temporary password is "+pwd);
            email.addTo(emailto);
            email.send();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
