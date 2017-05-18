package controllers;

import com.avaje.ebeaninternal.server.type.ScalarTypeYear;
import models.*;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.libs.Json;
/**
 * Created by Ling on 2017/3/27.
 */
public class UserController extends Controller {
    private FormFactory formFactory;

    @Inject
    public UserController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

//    public Result GO_HOME = Results.redirect(
//            routes.ShowPaperController.showMyPaper("a")
//    );
//
//    public Result GO_LOGIN = Results.redirect(
//            routes.UserController.login()
//    );
//
//    public Result register() {
//        Form<User> userForm = formFactory.form(User.class);
//        return ok(
//                views.html.register.render(userForm, 0)
//        );
//    }

//    /**
//     * login when get request for login page
//     * @return login html page
//     */
//    public Result login() {
//        Form<User> userForm = formFactory.form(User.class);
//        return ok(
//                views.html.login.render(userForm, 0)
//        );
//    }

    public Result changepwd(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();

        String username = new_user.username;
        String password = new_user.password;
        System.out.println("in backend change password "+username);
        List<User> update_users = User.find.where()
                .eq("username", username).findList();
        System.out.println("get size "+update_users.size());
        if(update_users.size()!=0) {
            User update_user = update_users.get(0);
            try {
                update_user.password = MD5(password);
                update_user.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JsonNode res_json = Json.newObject()
                .put("status","successful");
        return ok(res_json);
    }

//    /**
//     * Get request for verify auth for changing password
//     * @return
//     */
//    public Result verifyAuth() {
//        Form<User> userForm = formFactory.form(User.class);
//        return ok(
//                views.html.verifyChangePwdAuth.render(userForm, 0)
//        );
//    }

//    public Result sendTemporaryPwd(){
//        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
//        User new_user = userForm.get();
//        //String email = new_user.email;
//
//        //generate temporary password
//        Random r = new Random();
//        int max = 100000;
//        int min = 0;
//        String tmp_pwd =  Integer.toString(r.nextInt((max - min) + 1) + min);
//
//        //save tmp password into User
//        Session session = Http.Context.current().session();
//        String username = session.get("username");
//        System.out.println("Send tmp pwd Username "+username);
//
//        String email = new_user.GetEmailByUsername(username);
//        //String email = "linghl0915@163.com";
//        System.out.println("Send tmp pwd Email "+email);
//        //SendSimpleMessage(email, tmp_pwd);
//        //SendEmail.SendEmail(email, tmp_pwd);
//        SendEmail(email,tmp_pwd);
//        System.out.println("Email sent");
//
//        new_user.AddTemporaryPwd(username, tmp_pwd);
//
//        return ok(
//                views.html.temporarypwd.render(userForm)
//        );
//    }

//    public Result verifyTmpPwd(){
//        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
//        User new_user = userForm.get();
//        Session session = Http.Context.current().session();
//        String username = session.get("username");
//        String tmp_pwd = new_user.password;
//        if(new_user.IfTemporaryPwdCorrect(username, tmp_pwd)){
//            session.put("ChangePwdAuthVerified", "true");
//            return ok(
//                    views.html.changepwd.render(userForm)
//            );
//        }
//        return badRequest(views.html.temporarypwd.render(userForm));
//    }

    public Result verifyQA(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();

        String question1 = new_user.security_question1;
        String answer1 = new_user.security_answer1;
        String question2 = new_user.security_question2;
        String answer2 = new_user.security_answer2;

        String username = new_user.username;

        if(new_user.IfQACorrect(username, question1, answer1)){
            if(new_user.IfQACorrect(username, question2, answer2)){
                JsonNode res_json = Json.newObject()
                        .put("status","successful");
                return ok(res_json);
            }
        }
        JsonNode res_json = Json.newObject()
                .put("status","error");
        return ok(res_json);
    }
    /**
     * For login
     * Verify whether username and password is correct
     * @return To homepage if login success, else stay in login page
     */
    public Result verifyUser(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        if(userForm.hasErrors()) {
            System.out.println("ERROR");
            String errorMsg = "";
            java.util.Map<String, List<play.data.validation.ValidationError>> errorsAll = userForm.errors();
            for (String field : errorsAll.keySet()) {
                errorMsg += field + " ";
                for (ValidationError error : errorsAll.get(field)) {
                    errorMsg += error.message() + ", ";
                }
            }
            System.out.println("Please correct the following errors: " + errorMsg);
            //return badRequest(views.html.login.render(userForm, 1));
            JsonNode res_json = Json.newObject()
                    .put("username", "")
                    .put("userid", "")
                    .put("email","")
                    .put("status","error");
            return ok(res_json);
        }

            User new_user = userForm.get();
            String username = new_user.username;
            String password = new_user.password;
            if(new_user.VerifyUser(username, password)){
                System.out.println("User " + username + " login successfully!");

                Session session = Http.Context.current().session();
                Long id = new_user.GetUserID(username);
                String privilege = new_user.GetUserPrivilege(username);
                session.put("username",username);
                session.put("userid",id.toString());
                String email = new_user.GetEmailByUsername(username);
                session.put("email",email);
                System.out.println("Login successfully");

                JsonNode res_json = Json.newObject()
                        .put("username", username.toString())
                        .put("userid", id.toString())
                        .put("email",email.toString())
                        .put("status","successful")
                        .put("privilege",privilege);


                return ok(res_json);
            }

            //TODO notify frontend error message
            System.out.println("Login unsuccessfully");
        JsonNode res_json = Json.newObject()
                .put("username", "")
                .put("userid", "")
                .put("email","")
                .put("status","error");
        return ok(res_json);
    }

    /**
     * get all users
     */
    public Result getUsers(){
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arr = new ArrayNode(factory);

        for(User user : User.find.all()){
            JsonNode json = Json.newObject()
                    .put("username", user.username)
                    .put("userid", user.id.toString());
            //node.put(Integer.toString(i++), json);
            arr.add(json);
        }
//        System.out.println(arr);
        JsonNode temp = (JsonNode)arr;

        return ok(temp);
    }

    /**
     * get all reviewers of one conference
     */
    public Result allReviewers(String name){
        String confname = name.replaceAll("\\+", " ");
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arr = new ArrayNode(factory);

        for(Conference conf : Conference.find.where().eq("title", confname).eq("ifreviewer", "Y").findList()){
            User user = User.find.where().eq("username", conf.username).findUnique();
            JsonNode json = Json.newObject()
                    .put("username", user.username)
                    .put("userid", user.id.toString());
            //node.put(Integer.toString(i++), json);
            arr.add(json);
        }
//        System.out.println(arr);
        JsonNode temp = (JsonNode)arr;

        return ok(temp);
    }

    /**
     * Register a user
     */
    public Result addUser() {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        System.out.println("Start inserting");

        if(userForm.hasErrors()) {
            //print error msg
            System.out.println("ERROR");
            String errorMsg = "";
            java.util.Map<String, List<play.data.validation.ValidationError>> errorsAll = userForm.errors();
            for (String field : errorsAll.keySet()) {
                errorMsg += field + " ";
                for (ValidationError error : errorsAll.get(field)) {
                    errorMsg += error.message() + ", ";
                }
            }
            System.out.println("Please correct the following errors: " + errorMsg);
            //return ok(views.html.register.render(userForm,0));
            JsonNode res_json = Json.newObject()
                    .put("username", "")
                    .put("userid", "")
                    .put("email","")
                    .put("status","error");
            return ok(res_json);
        }

        try {
            //Thread.sleep(3000);
            User new_user = userForm.get();
            String username = new_user.username;
            String password = new_user.password;
            //String priviledge = new_user.priviledge;
            //System.out.println("password"+password);
            String email = new_user.email;

            //determine if the username exist (username needs to be unique)
            if(new_user.IfUserExist(username)){
                //if exist
                System.out.println("Username exists!");
                //flash("success", "Username " + username + " existed!");

                JsonNode res_json = Json.newObject()
                        .put("username", "")
                        .put("userid", "")
                        .put("email","")
                        .put("status","error");
                return ok(res_json);

                //return badRequest(views.html.register.render(userForm,1));
            }else{
                //if not exist, add user
                new_user.password = MD5(password);
                new_user.save();

                //check if his/her exists in pc member then pick up information into conference


                //the user has been logged in, save username and id to session
                Long id = new_user.GetUserID(username);
                Session session = Http.Context.current().session();
                session.put("username",username);
                session.put("userid",id.toString());
                session.put("email",email);

                System.out.println("User " + userForm.get().username + " has been created");

                Form<Profile> profileForm = formFactory.form(Profile.class);
                Profile profile = Profile.find.byId(id);

                JsonNode res_json = Json.newObject()
                        .put("username", username.toString())
                        .put("userid", id.toString())
                        .put("email",email.toString())
                        .put("status","successful");


                return ok(res_json);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return ok();
    }

//    public Result logout(){
//        Session session = Http.Context.current().session();
//        session.clear();
//        return redirect(
//                routes.UserController.login()
//        );
//    }

    /**
     *
     * @param password
     * @return password after encryed
     * @throws Exception
     */
    private static String MD5(String password) throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(StandardCharsets.UTF_8.encode(password));
        return String.format("%032x", new BigInteger(1, md.digest()));
    }

    public Result GetEmailByUsername(String username)
    {
        User new_user = new User();
        String email = new_user.GetEmailByUsername(username);
        JsonNode res_json = Json.newObject()
                .put("username", username.toString())
                .put("email",email.toString())
                .put("status","successful");


        return ok(res_json);
    }

//    /**
//     * Send temporary password for password changing
//     * @param email
//     * @return
//     */
//    private static ClientResponse SendSimpleMessage(String email, String tmp_pwd) {
//        String name = "customer";
//        String SendTo = name + " <" + email + ">";
//        Client client = Client.create();
//        client.addFilter(new HTTPBasicAuthFilter("api", "key-8bcaf224a0a4a59388e4dd33683d61e2"));
//        WebResource webResource = client.resource("https://api.mailgun.net/v3/sandboxb3bf5434ac5e4fba8a88fa29a6bc8b74.mailgun.org/messages");
//        MultivaluedMapImpl formData = new MultivaluedMapImpl();
//        formData.add("from", "Mailgun Sandbox <postmaster@sandboxb3bf5434ac5e4fba8a88fa29a6bc8b74.mailgun.org>");
//        formData.add("to", SendTo);
//        formData.add("subject", "Hello customer");
//        formData.add("text", "Dear Sir/Madam, your temporary password is "+tmp_pwd);
//        return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
//                post(ClientResponse.class, formData);
//    }

//    private static void SendEmail(String emailto, String pwd){
//        try {
//            Email email = new SimpleEmail();
//            email.setHostName("smtp.googlemail.com");
//            email.setSmtpPort(465);
//            email.setAuthenticator(new DefaultAuthenticator("socandrew2017@gmail.com", "ling0915"));
//            email.setSSLOnConnect(true);
//            email.setFrom("socandrew2017@gmail.com");
//            email.setSubject("Temporary password");
//            email.setMsg("Dear Sir/Madam, your temporary password is "+pwd);
//            email.addTo(emailto);
//            email.send();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
