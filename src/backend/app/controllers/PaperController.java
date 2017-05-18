package controllers;

import play.data.format.Formats;
import play.mvc.Controller;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.mvc.Result;
import play.mvc.Http;

import models.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.io.File;
import org.apache.commons.mail.*;
import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;

import play.libs.ws.*;
import java.util.concurrent.CompletionStage;

import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.libs.Json;
/**
 * Created by shuang on 3/29/17.
 */
public class PaperController extends Controller {
    private FormFactory formFactory;

    @Inject
    public PaperController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /*public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );*/
//    public Result GO_HOME = Results.redirect(
//            routes.ShowPaperController.showMyPaper("a")
//    );
    /**
     * Handle default path requests, redirect to computers list
     */
//    public Result index() {
//        return GO_HOME;
//    }

    public Result edit(Long id) {

        Form<Paper> paperForm = formFactory.form(Paper.class);
        Paper newPaper = Paper.find.byId(id);

        JsonNode json = Json.newObject()
                .put("title", newPaper.title)
                .put("contactemail",newPaper.contactemail)
                .put("firstname1",newPaper.firstname1)
                .put("lastname1",newPaper.lastname1)
                .put("email1",newPaper.email1)
                .put("affilation1",newPaper.affilation1)
                .put("firstname2",newPaper.firstname2)
                .put("lastname2",newPaper.lastname2)
                .put("email2",newPaper.email2)
                .put("affilation2",newPaper.affilation2)
                .put("firstname3",newPaper.firstname3)
                .put("lastname3",newPaper.lastname3)
                .put("email3",newPaper.email3)
                .put("affilation3",newPaper.affilation3)
                .put("firstname4",newPaper.firstname4)
                .put("lastname4",newPaper.lastname4)
                .put("email4",newPaper.email4)
                .put("affilation4",newPaper.affilation4)
                .put("firstname5",newPaper.firstname5)
                .put("lastname5",newPaper.lastname5)
                .put("email5",newPaper.email5)
                .put("affilation5",newPaper.affilation5)
                .put("firstname6",newPaper.firstname6)
                .put("lastname6",newPaper.lastname6)
                .put("email6",newPaper.email6)
                .put("affilation6",newPaper.affilation6)
                .put("firstname7",newPaper.firstname7)
                .put("lastname7",newPaper.lastname7)
                .put("email7",newPaper.email7)
                .put("affilation7",newPaper.affilation7)
                .put("otherauthor", newPaper.otherauthor)
                .put("candidate", newPaper.candidate)
                .put("volunteer", newPaper.volunteer)
                .put("paperabstract", newPaper.paperabstract)
                .put("topic", newPaper.topic)
                .put("reviewstatus", newPaper.reviewstatus)
                .put("status", newPaper.status)
                .put("reviewerid", newPaper.reviewerid)
                .put("review", newPaper.review);

        return ok(json);
    }
    public Result update(Long id) throws PersistenceException {
        System.out.println("this is beginning");
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();

        Transaction txn = Ebean.beginTransaction();
        try {
            Paper savedPaper = Paper.find.byId(id);
            if (savedPaper != null) {
                Paper newPaperData = paperForm.get();

                savedPaper.title = newPaperData.title;
                savedPaper.contactemail = newPaperData.contactemail;
                savedPaper.firstname1 = newPaperData.firstname1;
                savedPaper.lastname1 = newPaperData.lastname1;
                savedPaper.email1 = newPaperData.email1;
                savedPaper.affilation1 = newPaperData.affilation1;
                savedPaper.firstname2 = newPaperData.firstname2;
                savedPaper.lastname2 = newPaperData.lastname2;
                savedPaper.email2 = newPaperData.email2;
                savedPaper.affilation2 = newPaperData.affilation2;
                savedPaper.firstname3 = newPaperData.firstname3;
                savedPaper.lastname3 = newPaperData.lastname3;
                savedPaper.email3 = newPaperData.email3;
                savedPaper.affilation3 = newPaperData.affilation3;
                savedPaper.firstname4 = newPaperData.firstname4;
                savedPaper.lastname4 = newPaperData.lastname4;
                savedPaper.email4 = newPaperData.email4;
                savedPaper.affilation4 = newPaperData.affilation4;
                savedPaper.firstname5 = newPaperData.firstname5;
                savedPaper.lastname5 = newPaperData.lastname5;
                savedPaper.email5 = newPaperData.email5;
                savedPaper.affilation5 = newPaperData.affilation5;
                savedPaper.firstname6 = newPaperData.firstname6;
                savedPaper.lastname6 = newPaperData.lastname6;
                savedPaper.email6 = newPaperData.email6;
                savedPaper.affilation6 = newPaperData.affilation6;
                savedPaper.firstname7 = newPaperData.firstname7;
                savedPaper.lastname7 = newPaperData.lastname7;
                savedPaper.email7 = newPaperData.email7;
                savedPaper.affilation7 = newPaperData.affilation7;
                savedPaper.otherauthor = newPaperData.otherauthor;
                savedPaper.candidate = newPaperData.candidate;
                savedPaper.volunteer = newPaperData.volunteer;
                savedPaper.paperabstract = newPaperData.paperabstract;
//                savedPaper.ifsubmit = newPaperData.ifsubmit;
//                savedPaper.format = newPaperData.format;
//                savedPaper.papersize = newPaperData.papersize;
//                savedPaper.conference = newPaperData.conference;
                savedPaper.topic = newPaperData.topic;
//                savedPaper.status = newPaperData.status;
//                savedPaper.date = newPaperData.date;
                

                savedPaper.update();


                flash("success", "Paper " + paperForm.get().title + " has been updated");
                txn.commit();
            }
        } finally {
            txn.end();
        }

        return ok("update successfully");
    }

    public Result save() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();

        Paper newPaper = paperForm.get();
        newPaper.save();

        if(Review.find.where().eq("paperid", newPaper.id).findList().size() == 0) {
            System.out.println("I am here");
            Review review = new Review();
            review.paperid = newPaper.id;
            review.reviewerid = newPaper.reviewerid;
            review.reviewstatus = "assigned";
            review.iscriteria = "NA";
            review.insert();
        }


        paperForm.get().save();


        return ok(String.valueOf(newPaper.id));
    }


    /**
     * Get authors and papers
     */
    public Result showAuthorPaper() {

        List<Paper> paperList = Paper.find.all();

        Map<String, String> affmap = new HashMap<String, String>();
        affmap.put(" ", "Just for space");
        Map<String, List<Paper>> papermap = new HashMap<String, List<Paper>>();

        for(Paper paper : paperList){
            if(!affmap.containsKey(paper.firstname1 + " " + paper.lastname1)){
                affmap.put(paper.firstname1 + " " + paper.lastname1, paper.affilation1);
                List<Paper> tempList = new ArrayList<Paper>();
                tempList.add(paper);
                papermap.put(paper.firstname1 + " " + paper.lastname1, tempList);
            }
            else if(!(paper.firstname1 + " " + paper.lastname1).equals(" ")){
                List<Paper> tempList = papermap.get(paper.firstname1 + " " + paper.lastname1);
                tempList.add(paper);
                papermap.put(paper.firstname1 + " " + paper.lastname1, tempList);
            }

            if(!affmap.containsKey(paper.firstname2 + " " + paper.lastname2)){
                affmap.put(paper.firstname2 + " " + paper.lastname2, paper.affilation2);
                List<Paper> tempList = new ArrayList<Paper>();
                tempList.add(paper);
                papermap.put(paper.firstname2 + " " + paper.lastname2, tempList);
            }
            else if(!(paper.firstname2 + " " + paper.lastname2).equals(" ")){
                List<Paper> tempList = papermap.get(paper.firstname2 + " " + paper.lastname2);
                tempList.add(paper);
                papermap.put(paper.firstname2 + " " + paper.lastname2, tempList);
            }

            if(!affmap.containsKey(paper.firstname3 + " " + paper.lastname3)){
                affmap.put(paper.firstname3 + " " + paper.lastname3, paper.affilation3);
                List<Paper> tempList = new ArrayList<Paper>();
                tempList.add(paper);
                papermap.put(paper.firstname3 + " " + paper.lastname3, tempList);
            }
            else if(!(paper.firstname3 + " " + paper.lastname3).equals(" ")){
                List<Paper> tempList = papermap.get(paper.firstname3 + " " + paper.lastname3);
                tempList.add(paper);
                papermap.put(paper.firstname3 + " " + paper.lastname3, tempList);
            }

            if(!affmap.containsKey(paper.firstname4 + " " + paper.lastname4)){
                affmap.put(paper.firstname4 + " " + paper.lastname4, paper.affilation4);
                List<Paper> tempList = new ArrayList<Paper>();
                tempList.add(paper);
                papermap.put(paper.firstname4 + " " + paper.lastname4, tempList);
            }
            else if(!(paper.firstname4 + " " + paper.lastname4).equals(" ")){
                List<Paper> tempList = papermap.get(paper.firstname4 + " " + paper.lastname4);
                tempList.add(paper);
                papermap.put(paper.firstname4 + " " + paper.lastname4, tempList);
            }

            if(!affmap.containsKey(paper.firstname5 + " " + paper.lastname5)){
                affmap.put(paper.firstname5 + " " + paper.lastname5, paper.affilation5);
                List<Paper> tempList = new ArrayList<Paper>();
                tempList.add(paper);
                papermap.put(paper.firstname5 + " " + paper.lastname5, tempList);
            }
            else if(!(paper.firstname5 + " " + paper.lastname5).equals(" ")){
                List<Paper> tempList = papermap.get(paper.firstname5 + " " + paper.lastname5);
                tempList.add(paper);
                papermap.put(paper.firstname5 + " " + paper.lastname5, tempList);
            }

            if(!affmap.containsKey(paper.firstname6 + " " + paper.lastname6)){
                affmap.put(paper.firstname6 + " " + paper.lastname6, paper.affilation6);
                List<Paper> tempList = new ArrayList<Paper>();
                tempList.add(paper);
                papermap.put(paper.firstname6 + " " + paper.lastname6, tempList);
            }
            else if(!(paper.firstname6 + " " + paper.lastname6).equals(" ")){
                List<Paper> tempList = papermap.get(paper.firstname6 + " " + paper.lastname6);
                tempList.add(paper);
                papermap.put(paper.firstname6 + " " + paper.lastname6, tempList);
            }

            if(!affmap.containsKey(paper.firstname7 + " " + paper.lastname7)){
                affmap.put(paper.firstname7 + " " + paper.lastname7, paper.affilation7);
                List<Paper> tempList = new ArrayList<Paper>();
                tempList.add(paper);
                papermap.put(paper.firstname7 + " " + paper.lastname7, tempList);
            }
            else if(!(paper.firstname7 + " " + paper.lastname7).equals(" ")){
                List<Paper> tempList = papermap.get(paper.firstname7 + " " + paper.lastname7);
                tempList.add(paper);
                papermap.put(paper.firstname7 + " " + paper.lastname7, tempList);
            }
        }

        System.out.println("this is middle----------");


        int i = 0;
        //ObjectNode node = Json.newObject();

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arr = new ArrayNode(factory);

        for(Map.Entry<String, List<Paper>> entry : papermap.entrySet()){
            String key = entry.getKey();
            if(key.equals("null null"))     continue;
            System.out.println(key);

            String paperstr = "";

            ArrayNode temparr = new ArrayNode(factory);
            for(Paper paper : entry.getValue()){
                paperstr += paper.id + "#";
//                JsonNode json = Json.newObject().put("paperid", paper.id);
//                temparr.add(json);
//                System.out.println(json);
            }

            JsonNode json = Json.newObject()
                    .put("author", key)
                    .put("affiliation", affmap.get(key))
                    .put("papers", paperstr);
            //node.put(Integer.toString(i++), json);
            System.out.println("-----");
            System.out.println(json);
            System.out.println("-----");
            arr.add(json);
        }

        JsonNode temp = (JsonNode)arr;

        return ok(temp);
    }


    public Result selectFile(Long id) throws PersistenceException {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();

        Transaction txn = Ebean.beginTransaction();
        try {
            Paper savedPaper = Paper.find.byId(id);
            if (savedPaper != null) {
                Paper newPaperData = paperForm.get();


                savedPaper.ifsubmit = newPaperData.ifsubmit;
                savedPaper.format = newPaperData.format;
                savedPaper.papersize = newPaperData.papersize;

                savedPaper.update();
                txn.commit();
            }
        } finally {
            txn.end();
        }

        return ok("update successfully");
    }
}
