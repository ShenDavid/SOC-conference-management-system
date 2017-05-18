package controllers;

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

import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class PCmemberController extends Controller {
    @Inject
    WSClient ws;

    private FormFactory formFactory;

    @Inject
    public PCmemberController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public CompletionStage<Result> enterPCmember() {
        Form<PCmember> memberForm = formFactory.form(PCmember.class);
        Session session = Http.Context.current().session();

        //TODO JUST FOR TEST REMOVE IT LATER
        //session.put("conferenceinfo","IEEE 2017 ICWS Area 1");

        String conf_title = session.get("conferenceinfo");
        System.out.println("in pc member conf"+conf_title);

        String conf_title_url = conf_title.replace(" ","%20");
        CompletionStage<WSResponse> resp = ws.url("http://localhost:9000/pcmember/"+conf_title_url).get();

        return resp.thenApply(response -> {
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<PCmember> res = new ArrayList<PCmember>();
            for (JsonNode res1 : ret) {
                PCmember show_member = new PCmember();
                show_member.email = res1.get("email").asText();
                show_member.firstname = res1.get("firstname").asText();
                show_member.lastname = res1.get("lastname").asText();
                show_member.ifChair = res1.get("ifChair").asText();
                show_member.ifReviewer = res1.get("ifReviewer").asText();
                res.add(show_member);
            }
            return ok(
                    views.html.ProgramCommittee.render(memberForm,res,0));
        });
    }

    public CompletionStage<Result> addmember(){
        Form<PCmember> memberForm = formFactory.form(PCmember.class).bindFromRequest();
        Session session = Http.Context.current().session();
        String conf_title = session.get("conferenceinfo");
        PCmember new_member = memberForm.get();
        JsonNode json = Json.newObject()
                .put("email",new_member.email)
                .put("firstname",new_member.firstname)
                .put("lastname",new_member.lastname)
                .put("affiliation",new_member.affiliation)
                .put("phone",new_member.phone)
                .put("address",new_member.address)
                .put("ifChair",new_member.ifChair)
                .put("ifReviewer",new_member.ifReviewer)
                .put("conference",conf_title);
        System.out.println("1====fronend get add pc member email "+new_member.email+" is chair "+new_member.ifChair+" is reviewer "+new_member.ifReviewer+" conference "+conf_title);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/addpcmember").post(json);

        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("addmember result is " + ret);
            return redirect("/pcmember");
        });
    }

    public CompletionStage<Result> GetPCmember(String email){
        Form<PCmember> memberForm = formFactory.form(PCmember.class);
        Session session = Http.Context.current().session();

        String conf_title = session.get("conferenceinfo");
        String conf_url = conf_title.replace(" ","%20");
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/singlepcmember/"+email+"/"+conf_url).get();

        return res.thenApply(response -> {
            JsonNode res1 = response.asJson();

            PCmember show_member = new PCmember();
            show_member.email = res1.get("email").asText();
            show_member.firstname = res1.get("firstname").asText();
            show_member.lastname = res1.get("lastname").asText();
            show_member.ifChair = res1.get("ifChair").asText();
            show_member.ifReviewer = res1.get("ifReviewer").asText();
            show_member.affiliation = res1.get("affiliation").asText();
            show_member.phone = res1.get("phone").asText();
            show_member.address = res1.get("address").asText();

            return ok(
                    views.html.singlepcmember.render(memberForm,show_member,0));
        });
    }

    public CompletionStage<Result> ModifyPCmember()
    {
        Form<PCmember> memberForm = formFactory.form(PCmember.class).bindFromRequest();
        Session session = Http.Context.current().session();
        String conf_title = session.get("conferenceinfo");
        PCmember new_member = memberForm.get();
        JsonNode json = Json.newObject()
                .put("email",new_member.email)
                .put("firstname",new_member.firstname)
                .put("lastname",new_member.lastname)
                .put("affiliation",new_member.affiliation)
                .put("phone",new_member.phone)
                .put("address",new_member.address)
                .put("ifChair",new_member.ifChair)
                .put("ifReviewer",new_member.ifReviewer)
                .put("conference",conf_title);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/editpcmember").post(json);

        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("addmember is " + ret);
            return ok(
                    views.html.singlepcmember.render(memberForm,new_member,0));
        });
    }

    public CompletionStage<Result> deletePCmember(String email){
        Form<PCmember> memberForm = formFactory.form(PCmember.class);
        Session session = Http.Context.current().session();
        String conf_title = session.get("conferenceinfo");

        String conf_url = conf_title.replace(" ","%20");
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/deletepcmember/"+email+"/"+conf_url).get();

        return res.thenApply(response -> {
            return redirect("/pcmember");
        });
    }

}