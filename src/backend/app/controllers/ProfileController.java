package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import play.mvc.Http.Session;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

/**
 * Created by sxh on 17/3/26.
 */
public class ProfileController extends Controller{

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
//    public Result GO_HOME = Results.redirect(
//            routes.ShowPaperController.showMyPaper("a")
//    );

    public Result findById(Long id){
        Form<Profile> profileForm = formFactory.form(Profile.class);
        Profile newProfileData = Profile.find.byId(id);

        JsonNode json;

        if(newProfileData == null){
            json = Json.newObject().put("userid", -1);
        }
        else {
            json = Json.newObject()
                    .put("title", newProfileData.title)
                    .put("research", newProfileData.research)
                    .put("firstname", newProfileData.firstname)
                    .put("lastname", newProfileData.lastname)
                    .put("position", newProfileData.position)
                    .put("affiliation", newProfileData.affiliation)
                    .put("email", newProfileData.email)
                    .put("phone", newProfileData.phone)
                    .put("fax", newProfileData.fax)
                    .put("address", newProfileData.address)
                    .put("city", newProfileData.city)
                    .put("country", newProfileData.country)
                    .put("region", newProfileData.region)
                    .put("zipcode", newProfileData.zipcode)
                    .put("comment", newProfileData.comment)
                    .put("userid", newProfileData.userid);
        }

        return ok(json);
    }


    /**
     * Handle the 'edit form' submission
     *
     */
    public Result edit() throws PersistenceException {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        if(profileForm.hasErrors()) {
            return ok();
        }

        Transaction txn = Ebean.beginTransaction();
        try {
            Profile newProfileData = profileForm.get();
            Profile savedProfile = Profile.find.byId(newProfileData.userid);

            if (savedProfile != null) {
                savedProfile.title = newProfileData.title;
                savedProfile.research = newProfileData.research;
                savedProfile.firstname = newProfileData.firstname;
                savedProfile.lastname = newProfileData.lastname;
                savedProfile.position = newProfileData.position;
                savedProfile.affiliation = newProfileData.affiliation;
                savedProfile.email = newProfileData.email;
                savedProfile.phone = newProfileData.phone;
                savedProfile.fax = newProfileData.fax;
                savedProfile.address = newProfileData.address;
                savedProfile.city = newProfileData.city;
                savedProfile.country = newProfileData.country;
                savedProfile.region = newProfileData.region;
                savedProfile.zipcode = newProfileData.zipcode;
                savedProfile.comment = newProfileData.comment;

                savedProfile.userid = newProfileData.userid;

                savedProfile.update();
                //flash("success", "Profile " + userid + " has been updated");
                txn.commit();
                return ok("insert successfully");
            }
            else{
                Profile newProfile = new Profile();
                newProfile.title = newProfileData.title;
                newProfile.research = newProfileData.research;
                newProfile.firstname = newProfileData.firstname;
                newProfile.lastname = newProfileData.lastname;
                newProfile.position = newProfileData.position;
                newProfile.affiliation = newProfileData.affiliation;
                newProfile.email = newProfileData.email;
                newProfile.phone = newProfileData.phone;
                newProfile.fax = newProfileData.fax;
                newProfile.address = newProfileData.address;
                newProfile.city = newProfileData.city;
                newProfile.country = newProfileData.country;
                newProfile.region = newProfileData.region;
                newProfile.zipcode = newProfileData.zipcode;
                newProfile.comment = newProfileData.comment;

                newProfile.userid = newProfileData.userid;

                newProfile.insert();
                //flash("success", "Profile " + userid + " has been inserted");
                txn.commit();
                return ok("update successfully");
            }
        } finally {
            txn.end();
        }
    }

    /**
     * Handle the 'new profile form' submission
     */
//    public Result save() {
//        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
//        profileForm.get().save();
//        flash("success", "Profile " + profileForm.get().title + profileForm.get().lastname + " has been created");
//        return GO_HOME;
//    }

    /**
     * Handle profile deletion
     */
    public Result delete() {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        Profile deletedProfile = Profile.find.byId(profileForm.get().userid);
        if(deletedProfile != null){
            deletedProfile.delete();
            return ok("delete successfully");
        }
        else{
            return ok("you haven't create your profile yet");
        }
    }

}
