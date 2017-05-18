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

public class ConfigSysController extends Controller {

    private FormFactory formFactory;

    @Inject
    public ConfigSysController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GetConfInfo(String conf_title_url)
    {
        ConferenceDetail conf = new ConferenceDetail();

        String conf_title = conf_title_url.replace("%20", " ");

        Long old_conf_id = conf.GetConferenceInfo(conf_title);

        ConferenceDetail old_conf = ConferenceDetail.find.byId(old_conf_id);

        JsonNode json;

        if(old_conf_id == 0){
            json = Json.newObject().put("return_status", "error");
        }
        else {
            json = Json.newObject()
                    .put("return_status","ok")
                    .put("name", old_conf.name)
                    .put("title", old_conf.title)
                    .put("url", old_conf.url)
                    .put("conference_email", old_conf.conference_email)
                    .put("chair_email", old_conf.chair_email)
                    .put("tag_title", old_conf.tag_title)
                    .put("config_content", old_conf.config_content)
                    .put("canPDF", old_conf.canPDF)
                    .put("canPostscript", old_conf.canPostscript)
                    .put("canWord", old_conf.canWord)
                    .put("canZip", old_conf.canZip)
                    .put("canMultitopics", old_conf.canMultitopics)
                    .put("isOpenAbstract", old_conf.isOpenAbstract)
                    .put("isOpenPaper", old_conf.isOpenPaper)
                    .put("isOpenCamera", old_conf.isOpenCamera)
                    .put("isBlindReview", old_conf.isBlindReview)
                    .put("discussMode", old_conf.discussMode)
                    .put("ballotMode", old_conf.ballotMode)
                    .put("reviewer_number", old_conf.reviewer_number)
                    .put("isMailAbstract", old_conf.isMailAbstract)
                    .put("isMailUpload", old_conf.isMailUpload)
                    .put("isMailReviewSubmission", old_conf.isMailReviewSubmission);
            System.out.println("in backend multi topic: "+old_conf.canMultitopics+" NAME "+old_conf.name);
        }
        return ok(json);
    }

    public Result edit() throws PersistenceException {
        Form<ConferenceDetail> conferenceForm = formFactory.form(ConferenceDetail.class).bindFromRequest();
        if(conferenceForm.hasErrors()) {
            return ok();
        }

        Transaction txn = Ebean.beginTransaction();
        try {
            ConferenceDetail newconferenceData = conferenceForm.get();
            //Conference savedconference = Conference.find.byId(newconferenceData.userid);

            if (newconferenceData.ifInfoExist(newconferenceData.title)) {
                String new_title = newconferenceData.updateInfo(newconferenceData);
                //flash("success", "conference " + userid + " has been updated");
                txn.commit();
                return ok();
            }
            else{
                ConferenceDetail new_Conference = new ConferenceDetail();
                new_Conference.title = newconferenceData.title;
                new_Conference.name = newconferenceData.name;
                new_Conference.url = newconferenceData.url;
                new_Conference.conference_email = newconferenceData.conference_email;
                new_Conference.chair_email = newconferenceData.chair_email;
                new_Conference.tag_title = newconferenceData.tag_title;
                new_Conference.config_content = newconferenceData.config_content;
                new_Conference.canPDF = newconferenceData.canPDF;
                new_Conference.canPostscript = newconferenceData.canPostscript;
                new_Conference.canWord = newconferenceData.canWord;
                new_Conference.canZip = newconferenceData.canZip;
                new_Conference.canMultitopics = newconferenceData.canMultitopics;
                new_Conference.isOpenAbstract = newconferenceData.isOpenAbstract;
                new_Conference.isOpenPaper = newconferenceData.isOpenPaper;
                new_Conference.isOpenCamera = newconferenceData.isOpenCamera;
                new_Conference.isBlindReview = newconferenceData.isBlindReview;
                new_Conference.discussMode = newconferenceData.discussMode;
                new_Conference.ballotMode = newconferenceData.ballotMode;
                new_Conference.reviewer_number = newconferenceData.reviewer_number;
                new_Conference.isMailAbstract = newconferenceData.isMailAbstract;
                new_Conference.isMailUpload = newconferenceData.isMailUpload;
                new_Conference.isMailReviewSubmission = newconferenceData.isMailReviewSubmission;

                new_Conference.save();
                //flash("success", "conference " + userid + " has been inserted");
                txn.commit();
                return ok();
            }
        } finally {
            txn.end();
        }
    }

    public Result update(String conf_title_url)
    {
        ConferenceDetail conf = new ConferenceDetail();

        String conf_title = conf_title_url.replaceAll("\\+", " ");

        Long old_conf_id = conf.GetConferenceInfo(conf_title);

        ConferenceDetail old_conf = ConferenceDetail.find.byId(old_conf_id);

        JsonNode json;

        if(old_conf_id == 0){
            json = Json.newObject().put("return_status", "error");
        }
        else {
            json = Json.newObject()
                    .put("return_status","ok")
                    .put("phase", old_conf.phase);
            System.out.println("in backend multi topic: "+old_conf.canMultitopics+" NAME "+old_conf.name);
        }
        return ok(json);
    }

    public Result open() throws PersistenceException {
        Form<ConferenceDetail> conferenceForm = formFactory.form(ConferenceDetail.class).bindFromRequest();
        if(conferenceForm.hasErrors()) {
            return ok();
        }

        Transaction txn = Ebean.beginTransaction();
        try {
            ConferenceDetail conf = new ConferenceDetail();
            ConferenceDetail newconference = conferenceForm.get();
            Long old_conf_id = conf.GetConferenceInfo(newconference.title);

            ConferenceDetail newconferenceData = ConferenceDetail.find.byId(old_conf_id);

            if (newconferenceData != null) {

                newconferenceData.phase = newconference.phase;

                newconferenceData.update();
                txn.commit();
            }
        } finally {
            txn.end();
        }
        return ok();
    }

//    public Result close() throws PersistenceException {
//        Form<ConferenceDetail> conferenceForm = formFactory.form(ConferenceDetail.class).bindFromRequest();
//        if(conferenceForm.hasErrors()) {
//            return ok();
//        }
//
//        Transaction txn = Ebean.beginTransaction();
//        try {
//            ConferenceDetail conf = new ConferenceDetail();
//            ConferenceDetail newconference = conferenceForm.get();
//            Long old_conf_id = conf.GetConferenceInfo(newconference.title);
//
//            ConferenceDetail newconferenceData = ConferenceDetail.find.byId(old_conf_id);
//
//            if (newconferenceData != null) {
//                ConferenceDetail newconferenceData = conferenceForm.get();
//
//                newconferenceData.topic = newconferenceData.topic;
//
//                newconferenceData.update();
//                txn.commit();
//            }
//        } finally {
//            txn.end();
//        }
//    }
}