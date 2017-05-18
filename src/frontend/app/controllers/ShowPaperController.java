package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.server.type.ScalarTypeYear;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import models.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import com.avaje.ebeaninternal.server.type.ScalarTypeYear;

import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import play.libs.Json;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.*;



/**
 * Created by keqinli on 3/29/17.
 */
public class ShowPaperController extends Controller{
    @Inject WSClient ws;
//    @Inject HttpExecutionContext ec;
    private FormFactory formFactory;

    @Inject
    public ShowPaperController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * This result directly redirect to application home.
     */
    public Result GO_HOME = Results.redirect(
            routes.ShowPaperController.showMyPaper()
    );

    public Result GO_LOGIN = Results.redirect(
            routes.UserController.login()
    );
    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return GO_HOME;
    }

    public Result list(int page, String sortBy, String order, String filter) {
        return ok(
                views.html.list.render(
                        Computer.page(page, 10, sortBy, order, filter),
                        sortBy, order, filter
                )
        );
    }

    /**
     * Handle profile deletion
     */
//    public static Map<String,String> options() {
//
//        Http.Session session = Http.Context.current().session();
//        String username = session.get("username");
//
//        CompletionStage<WSResponse> resofconf = ws.url("http://localhost:9000/conference/" + username ).get();
//        Set<String> set = new HashSet<>();
//
//        resofconf.thenApplyAsync(response -> {
//            ArrayNode ret =(ArrayNode) response.asJson();
//            System.out.println("here is ");
//            Set<String> confSet = new HashSet<>();
//            for (JsonNode res1 : ret) {
//                confSet.add(res1.get("title").asText());
//            }
//            set = confSet;
//        });
//        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
//        for(String s : set) {
//            options.put(s, s);
//        }
//        return options;
//    }

    public CompletionStage<Result> showMyPaper() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        if(session.get("username")==null)
            return CompletableFuture.completedFuture(GO_LOGIN);
        String username = session.get("username");

//        CompletionStage<WSResponse> resofconf = ws.url("http://localhost:9000/conference/" + username ).get();
//        Set<String> set = new HashSet<>();
//
//        CompletionStage<Result> resultconf =resofconf.thenApplyAsync(response -> {
//            ArrayNode ret =(ArrayNode) response.asJson();
//            System.out.println("here is ");
//            Set<String> confSet = new HashSet<>();
//            for (JsonNode res1 : ret) {
//                confSet.add(res1.get("title").asText());
//            }
//            set = confSet;
//            return ok("OK");
//        });
//
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }

        JsonNode json = Json.newObject()
                .put("username", username);
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/paper/" + username).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
                List<Paper> res = new ArrayList<Paper>();
                for(JsonNode res1 : ret){
                    Paper savedPaper = new Paper();
                    savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                    savedPaper.title = res1.get("title").asText();
                    savedPaper.contactemail = res1.get("contactemail").asText();
                    savedPaper.authors = res1.get("authors").asText();
                    savedPaper.firstname1 = res1.get("firstname1").asText();
                    savedPaper.lastname1 = res1.get("lastname1").asText();
                    savedPaper.email1 = res1.get("email1").asText();
                    savedPaper.affilation1 = res1.get("affilation1").asText();
                    savedPaper.firstname2 = res1.get("firstname2").asText();
                    savedPaper.lastname2 = res1.get("lastname2").asText();
                    savedPaper.email2 = res1.get("email2").asText();
                    savedPaper.affilation2 = res1.get("affilation2").asText();
                    savedPaper.firstname3 = res1.get("firstname3").asText();
                    savedPaper.lastname3 = res1.get("lastname3").asText();
                    savedPaper.email3 = res1.get("email3").asText();
                    savedPaper.affilation3 = res1.get("affilation3").asText();
                    savedPaper.firstname4 = res1.get("firstname4").asText();
                    savedPaper.lastname4 = res1.get("lastname4").asText();
                    savedPaper.email4 = res1.get("email4").asText();
                    savedPaper.affilation4 = res1.get("affilation4").asText();
                    savedPaper.firstname5 = res1.get("firstname5").asText();
                    savedPaper.lastname5 = res1.get("lastname5").asText();
                    savedPaper.email5 = res1.get("email5").asText();
                    savedPaper.affilation5 = res1.get("affilation5").asText();
                    savedPaper.firstname6 = res1.get("firstname6").asText();
                    savedPaper.lastname6 = res1.get("lastname6").asText();
                    savedPaper.email6 = res1.get("email6").asText();
                    savedPaper.affilation6 = res1.get("affilation6").asText();
                    savedPaper.firstname7 = res1.get("firstname7").asText();
                    savedPaper.lastname7 = res1.get("lastname7").asText();
                    savedPaper.email7 = res1.get("email7").asText();
                    savedPaper.affilation7 = res1.get("affilation7").asText();
                    savedPaper.otherauthor = res1.get("otherauthor").asText();
                    savedPaper.candidate = res1.get("candidate").asText();
                    savedPaper.volunteer = res1.get("volunteer").asText();
                    savedPaper.paperabstract = res1.get("paperabstract").asText();
                savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                savedPaper.format = res1.get("format").asText();
                savedPaper.papersize = res1.get("papersize").asText();
                savedPaper.conference = res1.get("conference").asText();
                    savedPaper.topic = res1.get("topic").asText();
                savedPaper.status = res1.get("status").asText();
                savedPaper.date = res1.get("date").asText();

                    savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                    res.add(savedPaper);
                }
                return ok(
                        views.html.showmypaper.render(paperForm,res,session, options,"All My Conference"));

        });

    }


    public CompletionStage<Result> getAllConferencePaper() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);

        String confurl = conferenceinfo.replaceAll(" ","+");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + confurl).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                savedPaper.title = res1.get("title").asText();
                savedPaper.contactemail = res1.get("contactemail").asText();
                savedPaper.authors = res1.get("authors").asText();
                savedPaper.firstname1 = res1.get("firstname1").asText();
                savedPaper.lastname1 = res1.get("lastname1").asText();
                savedPaper.email1 = res1.get("email1").asText();
                savedPaper.affilation1 = res1.get("affilation1").asText();
                savedPaper.firstname2 = res1.get("firstname2").asText();
                savedPaper.lastname2 = res1.get("lastname2").asText();
                savedPaper.email2 = res1.get("email2").asText();
                savedPaper.affilation2 = res1.get("affilation2").asText();
                savedPaper.firstname3 = res1.get("firstname3").asText();
                savedPaper.lastname3 = res1.get("lastname3").asText();
                savedPaper.email3 = res1.get("email3").asText();
                savedPaper.affilation3 = res1.get("affilation3").asText();
                savedPaper.firstname4 = res1.get("firstname4").asText();
                savedPaper.lastname4 = res1.get("lastname4").asText();
                savedPaper.email4 = res1.get("email4").asText();
                savedPaper.affilation4 = res1.get("affilation4").asText();
                savedPaper.firstname5 = res1.get("firstname5").asText();
                savedPaper.lastname5 = res1.get("lastname5").asText();
                savedPaper.email5 = res1.get("email5").asText();
                savedPaper.affilation5 = res1.get("affilation5").asText();
                savedPaper.firstname6 = res1.get("firstname6").asText();
                savedPaper.lastname6 = res1.get("lastname6").asText();
                savedPaper.email6 = res1.get("email6").asText();
                savedPaper.affilation6 = res1.get("affilation6").asText();
                savedPaper.firstname7 = res1.get("firstname7").asText();
                savedPaper.lastname7 = res1.get("lastname7").asText();
                savedPaper.email7 = res1.get("email7").asText();
                savedPaper.affilation7 = res1.get("affilation7").asText();
                savedPaper.otherauthor = res1.get("otherauthor").asText();
                savedPaper.candidate = res1.get("candidate").asText();
                savedPaper.volunteer = res1.get("volunteer").asText();
                savedPaper.paperabstract = res1.get("paperabstract").asText();
                savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                savedPaper.format = res1.get("format").asText();
                savedPaper.papersize = res1.get("papersize").asText();
                savedPaper.conference = res1.get("conference").asText();
                savedPaper.topic = res1.get("topic").asText();
                savedPaper.status = res1.get("status").asText();
                savedPaper.date = res1.get("date").asText();

                savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                res.add(savedPaper);

            }
            return ok(
                    views.html.conferencepaper.render(paperForm,res,session,options,conferenceinfo));

        });

    }
    public CompletionStage<Result> getConferencePaper(String conferencename) {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String username = session.get("username");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);


        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/paper/conference/" + conferencename.replaceAll(" ","+")+"/"+username).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                savedPaper.title = res1.get("title").asText();
                savedPaper.contactemail = res1.get("contactemail").asText();
                savedPaper.authors = res1.get("authors").asText();
                savedPaper.firstname1 = res1.get("firstname1").asText();
                savedPaper.lastname1 = res1.get("lastname1").asText();
                savedPaper.email1 = res1.get("email1").asText();
                savedPaper.affilation1 = res1.get("affilation1").asText();
                savedPaper.firstname2 = res1.get("firstname2").asText();
                savedPaper.lastname2 = res1.get("lastname2").asText();
                savedPaper.email2 = res1.get("email2").asText();
                savedPaper.affilation2 = res1.get("affilation2").asText();
                savedPaper.firstname3 = res1.get("firstname3").asText();
                savedPaper.lastname3 = res1.get("lastname3").asText();
                savedPaper.email3 = res1.get("email3").asText();
                savedPaper.affilation3 = res1.get("affilation3").asText();
                savedPaper.firstname4 = res1.get("firstname4").asText();
                savedPaper.lastname4 = res1.get("lastname4").asText();
                savedPaper.email4 = res1.get("email4").asText();
                savedPaper.affilation4 = res1.get("affilation4").asText();
                savedPaper.firstname5 = res1.get("firstname5").asText();
                savedPaper.lastname5 = res1.get("lastname5").asText();
                savedPaper.email5 = res1.get("email5").asText();
                savedPaper.affilation5 = res1.get("affilation5").asText();
                savedPaper.firstname6 = res1.get("firstname6").asText();
                savedPaper.lastname6 = res1.get("lastname6").asText();
                savedPaper.email6 = res1.get("email6").asText();
                savedPaper.affilation6 = res1.get("affilation6").asText();
                savedPaper.firstname7 = res1.get("firstname7").asText();
                savedPaper.lastname7 = res1.get("lastname7").asText();
                savedPaper.email7 = res1.get("email7").asText();
                savedPaper.affilation7 = res1.get("affilation7").asText();
                savedPaper.otherauthor = res1.get("otherauthor").asText();
                savedPaper.candidate = res1.get("candidate").asText();
                savedPaper.volunteer = res1.get("volunteer").asText();
                savedPaper.paperabstract = res1.get("paperabstract").asText();
                savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                savedPaper.format = res1.get("format").asText();
                savedPaper.papersize = res1.get("papersize").asText();
                savedPaper.conference = res1.get("conference").asText();
                savedPaper.topic = res1.get("topic").asText();
                savedPaper.status = res1.get("status").asText();
                savedPaper.date = res1.get("date").asText();

                    savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                res.add(savedPaper);

            }
            return ok(
                    views.html.showmypaper.render(paperForm,res,session,options,conferencename));

        });

    }

    public CompletionStage<Result> getConferencePaperToAssign() {

        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);


        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + conferenceinfo.replaceAll(" ","+")).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                savedPaper.title = res1.get("title").asText();
                savedPaper.contactemail = res1.get("contactemail").asText();
                savedPaper.authors = res1.get("authors").asText();
                savedPaper.firstname1 = res1.get("firstname1").asText();
                savedPaper.lastname1 = res1.get("lastname1").asText();
                savedPaper.email1 = res1.get("email1").asText();
                savedPaper.affilation1 = res1.get("affilation1").asText();
                savedPaper.firstname2 = res1.get("firstname2").asText();
                savedPaper.lastname2 = res1.get("lastname2").asText();
                savedPaper.email2 = res1.get("email2").asText();
                savedPaper.affilation2 = res1.get("affilation2").asText();
                savedPaper.firstname3 = res1.get("firstname3").asText();
                savedPaper.lastname3 = res1.get("lastname3").asText();
                savedPaper.email3 = res1.get("email3").asText();
                savedPaper.affilation3 = res1.get("affilation3").asText();
                savedPaper.firstname4 = res1.get("firstname4").asText();
                savedPaper.lastname4 = res1.get("lastname4").asText();
                savedPaper.email4 = res1.get("email4").asText();
                savedPaper.affilation4 = res1.get("affilation4").asText();
                savedPaper.firstname5 = res1.get("firstname5").asText();
                savedPaper.lastname5 = res1.get("lastname5").asText();
                savedPaper.email5 = res1.get("email5").asText();
                savedPaper.affilation5 = res1.get("affilation5").asText();
                savedPaper.firstname6 = res1.get("firstname6").asText();
                savedPaper.lastname6 = res1.get("lastname6").asText();
                savedPaper.email6 = res1.get("email6").asText();
                savedPaper.affilation6 = res1.get("affilation6").asText();
                savedPaper.firstname7 = res1.get("firstname7").asText();
                savedPaper.lastname7 = res1.get("lastname7").asText();
                savedPaper.email7 = res1.get("email7").asText();
                savedPaper.affilation7 = res1.get("affilation7").asText();
                savedPaper.otherauthor = res1.get("otherauthor").asText();
                savedPaper.candidate = res1.get("candidate").asText();
                savedPaper.volunteer = res1.get("volunteer").asText();
                savedPaper.paperabstract = res1.get("paperabstract").asText();
                savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                savedPaper.format = res1.get("format").asText();
                savedPaper.papersize = res1.get("papersize").asText();
                savedPaper.conference = res1.get("conference").asText();
                savedPaper.topic = res1.get("topic").asText();
                savedPaper.status = res1.get("status").asText();
                savedPaper.date = res1.get("date").asText();

                savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                res.add(savedPaper);

            }
            return ok(
                    views.html.clicktoassignpaper.render(paperForm,res,session,options,conferenceinfo));

        });

    }

    public CompletionStage<Result> enterPaperStatus() {

        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);


        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + conferenceinfo.replaceAll(" ","+")).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                savedPaper.title = res1.get("title").asText();
                savedPaper.contactemail = res1.get("contactemail").asText();
                savedPaper.authors = res1.get("authors").asText();
                savedPaper.firstname1 = res1.get("firstname1").asText();
                savedPaper.lastname1 = res1.get("lastname1").asText();
                savedPaper.email1 = res1.get("email1").asText();
                savedPaper.affilation1 = res1.get("affilation1").asText();
                savedPaper.firstname2 = res1.get("firstname2").asText();
                savedPaper.lastname2 = res1.get("lastname2").asText();
                savedPaper.email2 = res1.get("email2").asText();
                savedPaper.affilation2 = res1.get("affilation2").asText();
                savedPaper.firstname3 = res1.get("firstname3").asText();
                savedPaper.lastname3 = res1.get("lastname3").asText();
                savedPaper.email3 = res1.get("email3").asText();
                savedPaper.affilation3 = res1.get("affilation3").asText();
                savedPaper.firstname4 = res1.get("firstname4").asText();
                savedPaper.lastname4 = res1.get("lastname4").asText();
                savedPaper.email4 = res1.get("email4").asText();
                savedPaper.affilation4 = res1.get("affilation4").asText();
                savedPaper.firstname5 = res1.get("firstname5").asText();
                savedPaper.lastname5 = res1.get("lastname5").asText();
                savedPaper.email5 = res1.get("email5").asText();
                savedPaper.affilation5 = res1.get("affilation5").asText();
                savedPaper.firstname6 = res1.get("firstname6").asText();
                savedPaper.lastname6 = res1.get("lastname6").asText();
                savedPaper.email6 = res1.get("email6").asText();
                savedPaper.affilation6 = res1.get("affilation6").asText();
                savedPaper.firstname7 = res1.get("firstname7").asText();
                savedPaper.lastname7 = res1.get("lastname7").asText();
                savedPaper.email7 = res1.get("email7").asText();
                savedPaper.affilation7 = res1.get("affilation7").asText();
                savedPaper.otherauthor = res1.get("otherauthor").asText();
                savedPaper.candidate = res1.get("candidate").asText();
                savedPaper.volunteer = res1.get("volunteer").asText();
                savedPaper.paperabstract = res1.get("paperabstract").asText();
                savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                savedPaper.format = res1.get("format").asText();
                savedPaper.papersize = res1.get("papersize").asText();
                savedPaper.conference = res1.get("conference").asText();
                savedPaper.topic = res1.get("topic").asText();
                savedPaper.status = res1.get("status").asText();
                savedPaper.date = res1.get("date").asText();

                savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                res.add(savedPaper);

            }
            return ok(
                    views.html.clicktoassignstate.render(paperForm,res,session,options,conferenceinfo));

        });

    }




    public CompletionStage<Result> getStatusPaper() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        Paper paperInfo = paperForm.get();
        String status = paperInfo.status;
//        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);

        String confurl = conferenceinfo.replaceAll(" ","+");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + confurl).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                if(res1.get("status").asText().equals(paperInfo.status)) {
                    savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                    savedPaper.title = res1.get("title").asText();
                    savedPaper.contactemail = res1.get("contactemail").asText();
                    savedPaper.authors = res1.get("authors").asText();
                    savedPaper.firstname1 = res1.get("firstname1").asText();
                    savedPaper.lastname1 = res1.get("lastname1").asText();
                    savedPaper.email1 = res1.get("email1").asText();
                    savedPaper.affilation1 = res1.get("affilation1").asText();
                    savedPaper.firstname2 = res1.get("firstname2").asText();
                    savedPaper.lastname2 = res1.get("lastname2").asText();
                    savedPaper.email2 = res1.get("email2").asText();
                    savedPaper.affilation2 = res1.get("affilation2").asText();
                    savedPaper.firstname3 = res1.get("firstname3").asText();
                    savedPaper.lastname3 = res1.get("lastname3").asText();
                    savedPaper.email3 = res1.get("email3").asText();
                    savedPaper.affilation3 = res1.get("affilation3").asText();
                    savedPaper.firstname4 = res1.get("firstname4").asText();
                    savedPaper.lastname4 = res1.get("lastname4").asText();
                    savedPaper.email4 = res1.get("email4").asText();
                    savedPaper.affilation4 = res1.get("affilation4").asText();
                    savedPaper.firstname5 = res1.get("firstname5").asText();
                    savedPaper.lastname5 = res1.get("lastname5").asText();
                    savedPaper.email5 = res1.get("email5").asText();
                    savedPaper.affilation5 = res1.get("affilation5").asText();
                    savedPaper.firstname6 = res1.get("firstname6").asText();
                    savedPaper.lastname6 = res1.get("lastname6").asText();
                    savedPaper.email6 = res1.get("email6").asText();
                    savedPaper.affilation6 = res1.get("affilation6").asText();
                    savedPaper.firstname7 = res1.get("firstname7").asText();
                    savedPaper.lastname7 = res1.get("lastname7").asText();
                    savedPaper.email7 = res1.get("email7").asText();
                    savedPaper.affilation7 = res1.get("affilation7").asText();
                    savedPaper.otherauthor = res1.get("otherauthor").asText();
                    savedPaper.candidate = res1.get("candidate").asText();
                    savedPaper.volunteer = res1.get("volunteer").asText();
                    savedPaper.paperabstract = res1.get("paperabstract").asText();
                    savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                    savedPaper.format = res1.get("format").asText();
                    savedPaper.papersize = res1.get("papersize").asText();
                    savedPaper.conference = res1.get("conference").asText();
                    savedPaper.topic = res1.get("topic").asText();
                    savedPaper.status = res1.get("status").asText();
                    savedPaper.date = res1.get("date").asText();

                    savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                    res.add(savedPaper);
                }
            }
            return ok(
                    views.html.statuspaper.render(paperForm,res,session,options,conferenceinfo));

        });

    }




    public CompletionStage<Result> showStatusPaper() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        Paper paperInfo = paperForm.get();
        String status = paperInfo.status;
//        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);

        String confurl = conferenceinfo.replaceAll(" ","+");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + confurl).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                    savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                    savedPaper.title = res1.get("title").asText();
                    savedPaper.contactemail = res1.get("contactemail").asText();
                    savedPaper.authors = res1.get("authors").asText();
                    savedPaper.firstname1 = res1.get("firstname1").asText();
                    savedPaper.lastname1 = res1.get("lastname1").asText();
                    savedPaper.email1 = res1.get("email1").asText();
                    savedPaper.affilation1 = res1.get("affilation1").asText();
                    savedPaper.firstname2 = res1.get("firstname2").asText();
                    savedPaper.lastname2 = res1.get("lastname2").asText();
                    savedPaper.email2 = res1.get("email2").asText();
                    savedPaper.affilation2 = res1.get("affilation2").asText();
                    savedPaper.firstname3 = res1.get("firstname3").asText();
                    savedPaper.lastname3 = res1.get("lastname3").asText();
                    savedPaper.email3 = res1.get("email3").asText();
                    savedPaper.affilation3 = res1.get("affilation3").asText();
                    savedPaper.firstname4 = res1.get("firstname4").asText();
                    savedPaper.lastname4 = res1.get("lastname4").asText();
                    savedPaper.email4 = res1.get("email4").asText();
                    savedPaper.affilation4 = res1.get("affilation4").asText();
                    savedPaper.firstname5 = res1.get("firstname5").asText();
                    savedPaper.lastname5 = res1.get("lastname5").asText();
                    savedPaper.email5 = res1.get("email5").asText();
                    savedPaper.affilation5 = res1.get("affilation5").asText();
                    savedPaper.firstname6 = res1.get("firstname6").asText();
                    savedPaper.lastname6 = res1.get("lastname6").asText();
                    savedPaper.email6 = res1.get("email6").asText();
                    savedPaper.affilation6 = res1.get("affilation6").asText();
                    savedPaper.firstname7 = res1.get("firstname7").asText();
                    savedPaper.lastname7 = res1.get("lastname7").asText();
                    savedPaper.email7 = res1.get("email7").asText();
                    savedPaper.affilation7 = res1.get("affilation7").asText();
                    savedPaper.otherauthor = res1.get("otherauthor").asText();
                    savedPaper.candidate = res1.get("candidate").asText();
                    savedPaper.volunteer = res1.get("volunteer").asText();
                    savedPaper.paperabstract = res1.get("paperabstract").asText();
                    savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                    savedPaper.format = res1.get("format").asText();
                    savedPaper.papersize = res1.get("papersize").asText();
                    savedPaper.conference = res1.get("conference").asText();
                    savedPaper.topic = res1.get("topic").asText();
                    savedPaper.status = res1.get("status").asText();
                    savedPaper.date = res1.get("date").asText();

                    savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                    res.add(savedPaper);

            }
            return ok(
                    views.html.statuspaper.render(paperForm,res,session,options,conferenceinfo));

        });

    }

    public CompletionStage<Result> getIdPaper() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        Paper paperInfo = paperForm.get();
        String status = paperInfo.status;
//        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);

        String confurl = conferenceinfo.replaceAll(" ","+");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + confurl).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                if(res1.get("id").asText().equals(String.valueOf(paperInfo.id))) {
                    savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                    savedPaper.title = res1.get("title").asText();
                    savedPaper.contactemail = res1.get("contactemail").asText();
                    savedPaper.authors = res1.get("authors").asText();
                    savedPaper.firstname1 = res1.get("firstname1").asText();
                    savedPaper.lastname1 = res1.get("lastname1").asText();
                    savedPaper.email1 = res1.get("email1").asText();
                    savedPaper.affilation1 = res1.get("affilation1").asText();
                    savedPaper.firstname2 = res1.get("firstname2").asText();
                    savedPaper.lastname2 = res1.get("lastname2").asText();
                    savedPaper.email2 = res1.get("email2").asText();
                    savedPaper.affilation2 = res1.get("affilation2").asText();
                    savedPaper.firstname3 = res1.get("firstname3").asText();
                    savedPaper.lastname3 = res1.get("lastname3").asText();
                    savedPaper.email3 = res1.get("email3").asText();
                    savedPaper.affilation3 = res1.get("affilation3").asText();
                    savedPaper.firstname4 = res1.get("firstname4").asText();
                    savedPaper.lastname4 = res1.get("lastname4").asText();
                    savedPaper.email4 = res1.get("email4").asText();
                    savedPaper.affilation4 = res1.get("affilation4").asText();
                    savedPaper.firstname5 = res1.get("firstname5").asText();
                    savedPaper.lastname5 = res1.get("lastname5").asText();
                    savedPaper.email5 = res1.get("email5").asText();
                    savedPaper.affilation5 = res1.get("affilation5").asText();
                    savedPaper.firstname6 = res1.get("firstname6").asText();
                    savedPaper.lastname6 = res1.get("lastname6").asText();
                    savedPaper.email6 = res1.get("email6").asText();
                    savedPaper.affilation6 = res1.get("affilation6").asText();
                    savedPaper.firstname7 = res1.get("firstname7").asText();
                    savedPaper.lastname7 = res1.get("lastname7").asText();
                    savedPaper.email7 = res1.get("email7").asText();
                    savedPaper.affilation7 = res1.get("affilation7").asText();
                    savedPaper.otherauthor = res1.get("otherauthor").asText();
                    savedPaper.candidate = res1.get("candidate").asText();
                    savedPaper.volunteer = res1.get("volunteer").asText();
                    savedPaper.paperabstract = res1.get("paperabstract").asText();
                    savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                    savedPaper.format = res1.get("format").asText();
                    savedPaper.papersize = res1.get("papersize").asText();
                    savedPaper.conference = res1.get("conference").asText();
                    savedPaper.topic = res1.get("topic").asText();
                    savedPaper.status = res1.get("status").asText();
                    savedPaper.date = res1.get("date").asText();

                    savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                    res.add(savedPaper);
                }
            }
            return ok(
                    views.html.idpaper.render(paperForm,res,session,options,conferenceinfo));

        });

    }




    public CompletionStage<Result> showIdPaper() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        Paper paperInfo = paperForm.get();
        String status = paperInfo.status;
//        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);

        String confurl = conferenceinfo.replaceAll(" ","+");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + confurl).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                savedPaper.title = res1.get("title").asText();
                savedPaper.contactemail = res1.get("contactemail").asText();
                savedPaper.authors = res1.get("authors").asText();
                savedPaper.firstname1 = res1.get("firstname1").asText();
                savedPaper.lastname1 = res1.get("lastname1").asText();
                savedPaper.email1 = res1.get("email1").asText();
                savedPaper.affilation1 = res1.get("affilation1").asText();
                savedPaper.firstname2 = res1.get("firstname2").asText();
                savedPaper.lastname2 = res1.get("lastname2").asText();
                savedPaper.email2 = res1.get("email2").asText();
                savedPaper.affilation2 = res1.get("affilation2").asText();
                savedPaper.firstname3 = res1.get("firstname3").asText();
                savedPaper.lastname3 = res1.get("lastname3").asText();
                savedPaper.email3 = res1.get("email3").asText();
                savedPaper.affilation3 = res1.get("affilation3").asText();
                savedPaper.firstname4 = res1.get("firstname4").asText();
                savedPaper.lastname4 = res1.get("lastname4").asText();
                savedPaper.email4 = res1.get("email4").asText();
                savedPaper.affilation4 = res1.get("affilation4").asText();
                savedPaper.firstname5 = res1.get("firstname5").asText();
                savedPaper.lastname5 = res1.get("lastname5").asText();
                savedPaper.email5 = res1.get("email5").asText();
                savedPaper.affilation5 = res1.get("affilation5").asText();
                savedPaper.firstname6 = res1.get("firstname6").asText();
                savedPaper.lastname6 = res1.get("lastname6").asText();
                savedPaper.email6 = res1.get("email6").asText();
                savedPaper.affilation6 = res1.get("affilation6").asText();
                savedPaper.firstname7 = res1.get("firstname7").asText();
                savedPaper.lastname7 = res1.get("lastname7").asText();
                savedPaper.email7 = res1.get("email7").asText();
                savedPaper.affilation7 = res1.get("affilation7").asText();
                savedPaper.otherauthor = res1.get("otherauthor").asText();
                savedPaper.candidate = res1.get("candidate").asText();
                savedPaper.volunteer = res1.get("volunteer").asText();
                savedPaper.paperabstract = res1.get("paperabstract").asText();
                savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                savedPaper.format = res1.get("format").asText();
                savedPaper.papersize = res1.get("papersize").asText();
                savedPaper.conference = res1.get("conference").asText();
                savedPaper.topic = res1.get("topic").asText();
                savedPaper.status = res1.get("status").asText();
                savedPaper.date = res1.get("date").asText();

                savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                res.add(savedPaper);

            }
            return ok(
                    views.html.idpaper.render(paperForm,res,session,options,conferenceinfo));

        });

    }




    public Result downloadExcelPaper() throws FileNotFoundException, IOException,InterruptedException{
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        Paper paperInfo = paperForm.get();
        String status = paperInfo.status;
//        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);

        String confurl = conferenceinfo.replaceAll(" ","+");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + confurl).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        List<Paper> res = new ArrayList<Paper>();

     resofrest.thenAccept(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
//            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                savedPaper.title = res1.get("title").asText();
                savedPaper.contactemail = res1.get("contactemail").asText();
                savedPaper.authors = res1.get("authors").asText();
                savedPaper.firstname1 = res1.get("firstname1").asText();
                savedPaper.lastname1 = res1.get("lastname1").asText();
                savedPaper.email1 = res1.get("email1").asText();
                savedPaper.affilation1 = res1.get("affilation1").asText();
                savedPaper.firstname2 = res1.get("firstname2").asText();
                savedPaper.lastname2 = res1.get("lastname2").asText();
                savedPaper.email2 = res1.get("email2").asText();
                savedPaper.affilation2 = res1.get("affilation2").asText();
                savedPaper.firstname3 = res1.get("firstname3").asText();
                savedPaper.lastname3 = res1.get("lastname3").asText();
                savedPaper.email3 = res1.get("email3").asText();
                savedPaper.affilation3 = res1.get("affilation3").asText();
                savedPaper.firstname4 = res1.get("firstname4").asText();
                savedPaper.lastname4 = res1.get("lastname4").asText();
                savedPaper.email4 = res1.get("email4").asText();
                savedPaper.affilation4 = res1.get("affilation4").asText();
                savedPaper.firstname5 = res1.get("firstname5").asText();
                savedPaper.lastname5 = res1.get("lastname5").asText();
                savedPaper.email5 = res1.get("email5").asText();
                savedPaper.affilation5 = res1.get("affilation5").asText();
                savedPaper.firstname6 = res1.get("firstname6").asText();
                savedPaper.lastname6 = res1.get("lastname6").asText();
                savedPaper.email6 = res1.get("email6").asText();
                savedPaper.affilation6 = res1.get("affilation6").asText();
                savedPaper.firstname7 = res1.get("firstname7").asText();
                savedPaper.lastname7 = res1.get("lastname7").asText();
                savedPaper.email7 = res1.get("email7").asText();
                savedPaper.affilation7 = res1.get("affilation7").asText();
                savedPaper.otherauthor = res1.get("otherauthor").asText();
                savedPaper.candidate = res1.get("candidate").asText();
                savedPaper.volunteer = res1.get("volunteer").asText();
                savedPaper.paperabstract = res1.get("paperabstract").asText();
                savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                savedPaper.format = res1.get("format").asText();
                savedPaper.papersize = res1.get("papersize").asText();
                savedPaper.conference = res1.get("conference").asText();
                savedPaper.topic = res1.get("topic").asText();
                savedPaper.status = res1.get("status").asText();
                savedPaper.date = res1.get("date").asText();
                savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                res.add(savedPaper);

            }


        });

        TimeUnit.SECONDS.sleep(1);

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        int rowCount = 0;

        for (Paper paper : res) {
            Row row = sheet.createRow(++rowCount);
            paper.writePaper(paper, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream("/Users/shuang/uploads/allpaper.xls")) {
            workbook.write(outputStream);
        }

        System.out.println("downloading...");
        response().setContentType("application/x-download");
        String cmd = "attachment; filename="+"allpaper.xls";
        response().setHeader("Content-disposition",cmd);
        String path = "/Users/shuang/uploads/"+"allpaper.xls";
        //return ok(new File("/User/huiliangling/uploads/test.txt"));
        return ok(new java.io.File(path));

    }


    public Result downloadAcceptedPaper() throws FileNotFoundException, IOException,InterruptedException{
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        Paper paperInfo = paperForm.get();
        String status = paperInfo.status;
//        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);

        String confurl = conferenceinfo.replaceAll(" ","+");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + confurl).get();
//        List<Paper> restemp =new Arraylist<Paper>();

        List<Paper> res = new ArrayList<Paper>();

       resofrest.thenAccept(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                if(res1.get("status").asText().equals("Accept")) {
                    savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                    savedPaper.title = res1.get("title").asText();
                    savedPaper.contactemail = res1.get("contactemail").asText();
                    savedPaper.authors = res1.get("authors").asText();
                    savedPaper.firstname1 = res1.get("firstname1").asText();
                    savedPaper.lastname1 = res1.get("lastname1").asText();
                    savedPaper.email1 = res1.get("email1").asText();
                    savedPaper.affilation1 = res1.get("affilation1").asText();
                    savedPaper.firstname2 = res1.get("firstname2").asText();
                    savedPaper.lastname2 = res1.get("lastname2").asText();
                    savedPaper.email2 = res1.get("email2").asText();
                    savedPaper.affilation2 = res1.get("affilation2").asText();
                    savedPaper.firstname3 = res1.get("firstname3").asText();
                    savedPaper.lastname3 = res1.get("lastname3").asText();
                    savedPaper.email3 = res1.get("email3").asText();
                    savedPaper.affilation3 = res1.get("affilation3").asText();
                    savedPaper.firstname4 = res1.get("firstname4").asText();
                    savedPaper.lastname4 = res1.get("lastname4").asText();
                    savedPaper.email4 = res1.get("email4").asText();
                    savedPaper.affilation4 = res1.get("affilation4").asText();
                    savedPaper.firstname5 = res1.get("firstname5").asText();
                    savedPaper.lastname5 = res1.get("lastname5").asText();
                    savedPaper.email5 = res1.get("email5").asText();
                    savedPaper.affilation5 = res1.get("affilation5").asText();
                    savedPaper.firstname6 = res1.get("firstname6").asText();
                    savedPaper.lastname6 = res1.get("lastname6").asText();
                    savedPaper.email6 = res1.get("email6").asText();
                    savedPaper.affilation6 = res1.get("affilation6").asText();
                    savedPaper.firstname7 = res1.get("firstname7").asText();
                    savedPaper.lastname7 = res1.get("lastname7").asText();
                    savedPaper.email7 = res1.get("email7").asText();
                    savedPaper.affilation7 = res1.get("affilation7").asText();
                    savedPaper.otherauthor = res1.get("otherauthor").asText();
                    savedPaper.candidate = res1.get("candidate").asText();
                    savedPaper.volunteer = res1.get("volunteer").asText();
                    savedPaper.paperabstract = res1.get("paperabstract").asText();
                    savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                    savedPaper.format = res1.get("format").asText();
                    savedPaper.papersize = res1.get("papersize").asText();
                    savedPaper.conference = res1.get("conference").asText();
                    savedPaper.topic = res1.get("topic").asText();
                    savedPaper.status = res1.get("status").asText();
                    savedPaper.date = res1.get("date").asText();

                    savedPaper.reviewerid = Long.parseLong(res1.get("reviewerid").asText());
                    res.add(savedPaper);
                }
            }

        });

        TimeUnit.SECONDS.sleep(1);


        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        int rowCount = 0;

        for (Paper paper : res) {
            Row row = sheet.createRow(++rowCount);
            paper.writePaper(paper, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream("/Users/shuang/uploads/paper.xls")) {
            workbook.write(outputStream);
        }

        System.out.println("downloading...");
        response().setContentType("application/x-download");
        String cmd = "attachment; filename="+"paper.xls";
        response().setHeader("Content-disposition",cmd);
        String path = "/Users/shuang/uploads/"+"paper.xls";
        //return ok(new File("/User/huiliangling/uploads/test.txt"));
        return ok(new java.io.File(path));

    }




}
