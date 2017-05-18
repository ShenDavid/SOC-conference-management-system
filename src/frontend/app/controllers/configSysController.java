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

public class configSysController extends Controller {
    @Inject
    WSClient ws;

    private FormFactory formFactory;

    @Inject
    public configSysController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }
    public Result GO_HOME = Results.redirect(
            routes.configSysController.update()
    );
    public CompletionStage<Result> enterConfigSystem(){
        Form<ConferenceDetail> conferenceForm = formFactory.form(ConferenceDetail.class);
        Session session = Http.Context.current().session();

        //TODO REMOVE IT
        //session.put("conferenceinfo","IEEE 2017 ICWS Area 1");


        String conf_title = session.get("conferenceinfo");

        String conf_title_url = conf_title.replace(" ","%20");
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/confInfo/"+conf_title_url).get();
        return res.thenApply(response -> {
            JsonNode ret = response.asJson();

            if(ret.get("return_status").asText().equals("error")){
                ConferenceDetail null_conf = new ConferenceDetail();
                null_conf.title = session.get("conferenceinfo");
                return ok(
                        views.html.configSys.render(conferenceForm, null_conf, 0)
                );
            }else {
                ConferenceDetail oldConference = new ConferenceDetail();
                oldConference.title = ret.get("title").asText();
                oldConference.name = ret.get("name").asText();
                oldConference.url = ret.get("url").asText();
                oldConference.conference_email = ret.get("conference_email").asText();
                oldConference.chair_email = ret.get("chair_email").asText();
                oldConference.tag_title = ret.get("tag_title").asText();
                oldConference.config_content = ret.get("config_content").asText();
                oldConference.canPDF = ret.get("canPDF").asBoolean();
                oldConference.canPostscript = ret.get("canPostscript").asBoolean();
                oldConference.canWord = ret.get("canWord").asBoolean();
                oldConference.canZip = ret.get("canZip").asBoolean();
                oldConference.canMultitopics = ret.get("canMultitopics").asText();
                oldConference.isOpenAbstract = ret.get("isOpenAbstract").asText();
                oldConference.isOpenPaper = ret.get("isOpenPaper").asText();
                oldConference.isOpenCamera = ret.get("isOpenCamera").asText();
                oldConference.isBlindReview = ret.get("isBlindReview").asText();
                oldConference.discussMode = ret.get("discussMode").asText();
                oldConference.ballotMode = ret.get("ballotMode").asText();
                oldConference.reviewer_number = ret.get("reviewer_number").asText();
                oldConference.isMailAbstract = ret.get("isMailAbstract").asText();
                oldConference.isMailUpload = ret.get("isMailUpload").asText();
                oldConference.isMailReviewSubmission = ret.get("isMailReviewSubmission").asText();

                System.out.println("===in get multi topic "+oldConference.canMultitopics);
                //oldConference.title = session.get("conferenceinfo");
                return ok(
                        views.html.configSys.render(conferenceForm, oldConference, 0)
                );
            }
        });

    }

    //find original record by conference info in session
    public CompletionStage<Result> edit()throws PersistenceException {
        Form<ConferenceDetail> conferenceForm = formFactory.form(ConferenceDetail.class).bindFromRequest();


        Session session = Http.Context.current().session();
        String conf_title = session.get("conferenceinfo");

        ConferenceDetail newProfileData = conferenceForm.get();

        JsonNode json = Json.newObject()
                .put("title", newProfileData.title)
                .put("name", newProfileData.name)
                .put("url",newProfileData.url)
                .put("conference_email", newProfileData.conference_email)
                .put("chair_email", newProfileData.chair_email)
                .put("tag_title", newProfileData.tag_title)
                .put("config_content", newProfileData.config_content)
                .put("canPDF", newProfileData.canPDF)
                .put("canPostscript", newProfileData.canPostscript)
                .put("canWord",newProfileData.canWord)
                .put("canZip", newProfileData.canZip)
                .put("canMultitopics", newProfileData.canMultitopics)
                .put("isOpenAbstract", newProfileData.isOpenAbstract)
                .put("isOpenPaper", newProfileData.isOpenPaper)
                .put("isOpenCamera", newProfileData.isOpenCamera)
                .put("isBlindReview", newProfileData.isBlindReview)
                .put("discussMode", newProfileData.discussMode)
                .put("ballotMode", newProfileData.ballotMode)
                .put("reviewer_number", newProfileData.reviewer_number)
                .put("isMailAbstract", newProfileData.isMailAbstract)
                .put("isMailUpload", newProfileData.isMailUpload)
                .put("isMailReviewSubmission", newProfileData.isMailReviewSubmission);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/editConfInfo").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("here is "+ret);
            if(conferenceForm.hasErrors()) {
                return ok(
                        views.html.configSys.render(conferenceForm, null, -1)
                );
            }
//            else if ("666notexist".equals(ret)) {
//                return ok(
//                        views.html.configSys.render(conferenceForm, newProfileData, 1)
//                );
//            }
//            else{
               // Session session = Http.Context.current().session();
//                String origin_conf = session.get("conferenceinfo");
//                if(!origin_conf.equals(ret))
//                    session.put("conferenceinfo", ret);
                return ok(
                        views.html.configSys.render(conferenceForm, newProfileData, 0)
                );
            //}
        });
    }

    public CompletionStage<Result> update(){
        Form<ConferenceDetail> conferenceForm = formFactory.form(ConferenceDetail.class);
        Session session = Http.Context.current().session();
        String conf_title = session.get("conferenceinfo");

        String conf_title_url = conf_title.replaceAll(" ","+");
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/confInfo/updatephase/"+conf_title_url).get();
        return res.thenApply(response -> {
            JsonNode ret = response.asJson();


                ConferenceDetail oldConference = new ConferenceDetail();
                oldConference.phase = ret.get("phase").asText();


                System.out.println("===in get multi topic "+oldConference.canMultitopics);
                //oldConference.title = session.get("conferenceinfo");
                return ok(
                        views.html.submissionphase.render(oldConference.phase)
                );

        });

    }

    //find original record by conference info in session
    public CompletionStage<Result> open()throws PersistenceException {
        Form<ConferenceDetail> conferenceForm = formFactory.form(ConferenceDetail.class).bindFromRequest();


        Session session = Http.Context.current().session();
        String conf_title = session.get("conferenceinfo");

        ConferenceDetail newProfileData = conferenceForm.get();

        JsonNode json = Json.newObject()
                .put("phase", "OPEN")
                .put("title", conf_title);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/editConfInfo/openphase").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("here is "+ret);

            return GO_HOME;
        });
    }
    //find original record by conference info in session
    public CompletionStage<Result> close()throws PersistenceException {
        Form<ConferenceDetail> conferenceForm = formFactory.form(ConferenceDetail.class).bindFromRequest();


        Session session = Http.Context.current().session();
        String conf_title = session.get("conferenceinfo");

        ConferenceDetail newProfileData = conferenceForm.get();

        JsonNode json = Json.newObject()
                .put("phase", "CLOSE")
                .put("title", conf_title);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/editConfInfo/closephase").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("here is "+ret);

            return GO_HOME;
        });
    }
}