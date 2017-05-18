package controllers;

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
import java.util.concurrent.CompletableFuture;

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

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import play.mvc.Http.Session;

import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

/**
 * Created by sxh on 17/3/26.
 */
public class ProfileController extends Controller{
    @Inject WSClient ws;

    private FormFactory formFactory;

    @Inject
    public ProfileController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * This result directly redirect to application home.
     */
    /*public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );*/


    public Result GO_HOME = Results.redirect(
            routes.ShowConferenceController.showMyConference()
    );
    public Result GO_LOGIN = Results.redirect(
            routes.UserController.login()
    );

    public CompletionStage<Result> enterProfile(){
        Form<Profile> profileForm = formFactory.form(Profile.class);
        Session session = Http.Context.current().session();
        if(session.get("username")==null)
            return CompletableFuture.completedFuture(GO_LOGIN);
        Long userid = Long.parseLong(session.get("userid"));
//        System.out.println("Enter profile page user id is "+userid.toString());
//        Profile profile = Profile.find.byId(userid);


        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/profile/"+userid).get();
        return res.thenApply(response -> {
//            String str = response.getBody();
//            System.out.println("there is "+str);
//            Json js = new Json();
//            JsonNode ret = js.parse(str);
            JsonNode ret = response.asJson();


            if(ret.get("userid").asText().equals("-1")){
                return ok(
                        views.html.profile.render(profileForm, null, 0)
                );
            }else {

                Profile savedProfile = new Profile();
                savedProfile.title = ret.get("title").asText();
                savedProfile.research = ret.get("research").asText();
                savedProfile.firstname = ret.get("firstname").asText();
                savedProfile.lastname = ret.get("lastname").asText();
                savedProfile.position = ret.get("position").asText();
                savedProfile.affiliation = ret.get("affiliation").asText();
                savedProfile.email = ret.get("email").asText();
                savedProfile.phone = ret.get("phone").asText();
                savedProfile.fax = ret.get("fax").asText();
                savedProfile.address = ret.get("address").asText();
                savedProfile.city = ret.get("city").asText();
                savedProfile.country = ret.get("country").asText();
                savedProfile.region = ret.get("region").asText();
                savedProfile.zipcode = null;
                savedProfile.comment = ret.get("comment").asText();

                savedProfile.userid = Long.parseLong(ret.get("userid").asText());

                return ok(
                        views.html.profile.render(profileForm, savedProfile, 0)
                );
            }
        });

    }


    /**
     * Handle the 'edit form' submission
     *
     */
    public CompletionStage<Result> edit() throws PersistenceException {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();


        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));

        Profile newProfileData = profileForm.get();

        JsonNode json = Json.newObject()
                .put("title", newProfileData.title)
                .put("research", newProfileData.research)
                .put("firstname",newProfileData.firstname)
                .put("lastname", newProfileData.lastname)
                .put("position", newProfileData.position)
                .put("affiliation", newProfileData.affiliation)
                .put("email", newProfileData.email)
                .put("phone", newProfileData.phone)
                .put("fax", newProfileData.fax)
                .put("address",newProfileData.address)
                .put("city", newProfileData.city)
                .put("country", newProfileData.country)
                .put("region", newProfileData.region)
                .put("zipcode", newProfileData.zipcode)
                .put("comment", newProfileData.comment)
                .put("userid", userid);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/profile/edit").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("here is "+ret);
            if(profileForm.hasErrors()) {
                return ok(
                        views.html.profile.render(profileForm, null, -1)
                );
            }
            else if ("insert successfully".equals(ret)) {
                return ok(
                        views.html.profile.render(profileForm, newProfileData, 1)
                );
            }
            else if ("update successfully".equals(ret)) {
                return ok(
                        views.html.profile.render(profileForm, newProfileData, 2)
                );
            }
            else{
                return ok(
                        views.html.profile.render(profileForm, null, -1)
                );
            }
        });
    }

    /**
     * Handle the 'new profile form' submission
     */
    public Result save() {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        profileForm.get().save();
        flash("success", "Profile " + profileForm.get().title + profileForm.get().lastname + " has been created");
        return GO_HOME;
    }

    /**
     * Handle profile deletion
     */
    public CompletionStage<Result> delete() {
        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));

        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        Profile newProfileData = profileForm.get();

        JsonNode json = Json.newObject()
                .put("title", newProfileData.title)
                .put("research", newProfileData.research)
                .put("firstname",newProfileData.firstname)
                .put("lastname", newProfileData.lastname)
                .put("position", newProfileData.position)
                .put("affiliation", newProfileData.affiliation)
                .put("email", newProfileData.email)
                .put("phone", newProfileData.phone)
                .put("fax", newProfileData.fax)
                .put("address",newProfileData.address)
                .put("city", newProfileData.city)
                .put("country", newProfileData.country)
                .put("region", newProfileData.region)
                .put("zipcode", newProfileData.zipcode)
                .put("comment", newProfileData.comment)
                .put("userid", userid);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/profile/delete").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            if ("delete successfully".equals(ret)) {
                return ok(
                        views.html.profile.render(profileForm, null, 3)
                );
            }
            else if ("you haven't create your profile yet".equals(ret)) {
                return ok(
                        views.html.profile.render(profileForm, null, 4)
                );
            }
            else{
                return ok(
                        views.html.profile.render(profileForm, null, -1)
                );
            }
        });
    }

}
