package controllers;

import com.avaje.ebeaninternal.server.type.ScalarTypeYear;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;

import models.*;
import java.util.*;

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

import javax.ws.rs.core.MediaType;
import play.mvc.Http.Session;
import play.mvc.Http;

//import play.libs.Mail;
import org.apache.commons.mail.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;


import java.lang.reflect.Array;
/**
 * Created by keqinli on 3/29/17.
 */
public class ShowPaperController extends Controller{

    private FormFactory formFactory;

    @Inject
    public ShowPaperController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }


    /**
     * Handle profile deletion
     */
    public Result showMyPaper(String username) {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();


        List<Paper> res = new ArrayList<Paper>();
        res = paperInfo.GetMyPaper(username);


         String authors = "";
        for(int i =0; i <res.size(); i++){
            res.get(i).authors = "";
            if(!res.get(i).firstname1.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname1 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname1 + ", ";
            }
            if(!res.get(i).firstname2.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname2 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname2 + ", ";
            }
            if(!res.get(i).firstname3.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname3 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname3 + ", ";
            }
            if(!res.get(i).firstname4.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname4 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname4 + ", ";
            }
            if(!res.get(i).firstname5.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname5 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname5 + ", ";
            }
            if(!res.get(i).firstname6.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname6 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname6 + ", ";
            }
            if(!res.get(i).firstname7.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname7 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname7 + ", ";
            }
            if(!res.get(i).contactemail.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).contactemail + " ";
            }
        }

//        ObjectMapper mapper= new ObjectMapper();
//        ArrayNode jsonarray = mapper.createArrayNode();
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonarray = new ArrayNode(factory);
        for(int i=0; i< res.size(); i++){
            JsonNode json = Json.newObject()
                    .put("id", res.get(i).id)
//                    .put("username", username)
                    .put("title", res.get(i).title)
                    .put("authors", res.get(i).authors)
                    .put("confirmemail", res.get(i).confirmemail)
                    .put("contactemail",res.get(i).contactemail)
                    .put("firstname1",res.get(i).firstname1)
                    .put("lastname1",res.get(i).lastname1)
                    .put("email1",res.get(i).email1)
                    .put("affilation1",res.get(i).affilation1)
                    .put("firstname2",res.get(i).firstname2)
                    .put("lastname2",res.get(i).lastname2)
                    .put("email2",res.get(i).email2)
                    .put("affilation2",res.get(i).affilation2)
                    .put("firstname3",res.get(i).firstname3)
                    .put("lastname3",res.get(i).lastname3)
                    .put("email3",res.get(i).email3)
                    .put("affilation3",res.get(i).affilation3)
                    .put("firstname4",res.get(i).firstname4)
                    .put("lastname4",res.get(i).lastname4)
                    .put("email4",res.get(i).email4)
                    .put("affilation4",res.get(i).affilation4)
                    .put("firstname5",res.get(i).firstname5)
                    .put("lastname5",res.get(i).lastname5)
                    .put("email5",res.get(i).email5)
                    .put("affilation5",res.get(i).affilation5)
                    .put("firstname6",res.get(i).firstname6)
                    .put("lastname6",res.get(i).lastname6)
                    .put("email6",res.get(i).email6)
                    .put("affilation6",res.get(i).affilation6)
                    .put("firstname7",res.get(i).firstname7)
                    .put("lastname7",res.get(i).lastname7)
                    .put("email7",res.get(i).email7)
                    .put("affilation7",res.get(i).affilation7)
                    .put("otherauthor", res.get(i).otherauthor)
                    .put("candidate", res.get(i).candidate)
                    .put("volunteer", res.get(i).volunteer)
                    .put("paperabstract", res.get(i).paperabstract)
                    .put("topic", res.get(i).topic)
                    .put("ifsubmit", res.get(i).ifsubmit)
                    .put("format", res.get(i).format)
                    .put("papersize", res.get(i).papersize)
                    .put("date", res.get(i).date)
                    .put("conference", res.get(i).conference)
                    .put("file", res.get(i).file)
                    .put("status", res.get(i).status)
                    .put("reviewerid", res.get(i).reviewerid);

             jsonarray.add(json);

        }
        //jsonarray.add({"status": "successful"});
        System.out.println(jsonarray);
        JsonNode temp = (JsonNode) jsonarray;
        return ok(temp);
    }

    public Result getAllConferencePaper(String confname) {
        String name = confname.replaceAll("\\+", " ");

        List<Paper> paperList = Paper.ConfPapers(name);

//        int i = 0;
        //ObjectNode node = Json.newObject();
//        List<Paper> res = paperList;
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arr = new ArrayNode(factory);
//        String authors = "";
//        for(int i =0; i <paperList.size(); i++){
//            paperList.get(i).authors = "";
//            if(!paperList.get(i).firstname1.isEmpty()){
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).firstname1 + " ";
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).lastname1 + ", ";
//            }
//            if(!paperList.get(i).firstname2.isEmpty()){
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).firstname2 + " ";
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).lastname2 + ", ";
//            }
//            if(!paperList.get(i).firstname3.isEmpty()){
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).firstname3 + " ";
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).lastname3 + ", ";
//            }
//            if(!paperList.get(i).firstname4.isEmpty()){
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).firstname4 + " ";
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).lastname4 + ", ";
//            }
//            if(!paperList.get(i).firstname5.isEmpty()){
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).firstname5 + " ";
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).lastname5 + ", ";
//            }
//            if(!paperList.get(i).firstname6.isEmpty()){
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).firstname6 + " ";
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).lastname6 + ", ";
//            }
//            if(!paperList.get(i).firstname7.isEmpty()){
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).firstname7 + " ";
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).lastname7 + ", ";
//            }
//            if(!paperList.get(i).contactemail.isEmpty()){
//                paperList.get(i).authors = paperList.get(i).authors + paperList.get(i).contactemail + " ";
//            }
//        }
        for(Paper newPaper : paperList){
                JsonNode json = Json.newObject()
                        .put("id", newPaper.id)
                        .put("title", newPaper.title)
                        .put("contactemail", newPaper.contactemail)
                        .put("authors", newPaper.authors)
                        .put("firstname1", newPaper.firstname1)
                        .put("lastname1", newPaper.lastname1)
                        .put("email1", newPaper.email1)
                        .put("affilation1", newPaper.affilation1)
                        .put("firstname2", newPaper.firstname2)
                        .put("lastname2", newPaper.lastname2)
                        .put("email2", newPaper.email2)
                        .put("affilation2", newPaper.affilation2)
                        .put("firstname3", newPaper.firstname3)
                        .put("lastname3", newPaper.lastname3)
                        .put("email3", newPaper.email3)
                        .put("affilation3", newPaper.affilation3)
                        .put("firstname4", newPaper.firstname4)
                        .put("lastname4", newPaper.lastname4)
                        .put("email4", newPaper.email4)
                        .put("affilation4", newPaper.affilation4)
                        .put("firstname5", newPaper.firstname5)
                        .put("lastname5", newPaper.lastname5)
                        .put("email5", newPaper.email5)
                        .put("affilation5", newPaper.affilation5)
                        .put("firstname6", newPaper.firstname6)
                        .put("lastname6", newPaper.lastname6)
                        .put("email6", newPaper.email6)
                        .put("affilation6", newPaper.affilation6)
                        .put("firstname7", newPaper.firstname7)
                        .put("lastname7", newPaper.lastname7)
                        .put("email7", newPaper.email7)
                        .put("affilation7", newPaper.affilation7)
                        .put("otherauthor", newPaper.otherauthor)
                        .put("candidate", newPaper.candidate)
                        .put("volunteer", newPaper.volunteer)
                        .put("paperabstract", newPaper.paperabstract)
                        .put("topic", newPaper.topic)
                        .put("ifsubmit", newPaper.ifsubmit)
                    .put("format", newPaper.format)
                        .put("status", newPaper.status)
                    .put("papersize", newPaper.papersize)
                    .put("date", newPaper.date)
                    .put("conference", newPaper.conference)
                    .put("file", newPaper.file)
                        .put("reviewstatus", newPaper.reviewstatus)
                        .put("reviewerid", newPaper.reviewerid)
                        .put("review", newPaper.review);
                //node.put(Integer.toString(i++), json);
                arr.add(json);

        }
//        System.out.println(arr);
        JsonNode temp = (JsonNode)arr;

        return ok(temp);
    }



    public Result getConferencePaper(String confname,String username) {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();

        confname = confname.replaceAll("\\+"," ");
        List<Paper> res = new ArrayList<Paper>();
        res = paperInfo.ConfUserPapers(confname,username);


        String authors = "";
        for(int i =0; i <res.size(); i++){
            res.get(i).authors = "";
            if(!res.get(i).firstname1.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname1 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname1 + ", ";
            }
            if(!res.get(i).firstname2.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname2 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname2 + ", ";
            }
            if(!res.get(i).firstname3.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname3 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname3 + ", ";
            }
            if(!res.get(i).firstname4.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname4 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname4 + ", ";
            }
            if(!res.get(i).firstname5.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname5 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname5 + ", ";
            }
            if(!res.get(i).firstname6.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname6 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname6 + ", ";
            }
            if(!res.get(i).firstname7.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname7 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname7 + ", ";
            }
            if(!res.get(i).contactemail.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).contactemail + " ";
            }
        }

//        ObjectMapper mapper= new ObjectMapper();
//        ArrayNode jsonarray = mapper.createArrayNode();
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonarray = new ArrayNode(factory);
        for(int i=0; i< res.size(); i++){

                JsonNode json = Json.newObject()
                    .put("id", res.get(i).id)
//                    .put("username", username)
                    .put("title", res.get(i).title)
                    .put("authors", res.get(i).authors)
                    .put("confirmemail", res.get(i).confirmemail)
                    .put("contactemail",res.get(i).contactemail)
                    .put("firstname1",res.get(i).firstname1)
                    .put("lastname1",res.get(i).lastname1)
                    .put("email1",res.get(i).email1)
                    .put("affilation1",res.get(i).affilation1)
                    .put("firstname2",res.get(i).firstname2)
                    .put("lastname2",res.get(i).lastname2)
                    .put("email2",res.get(i).email2)
                    .put("affilation2",res.get(i).affilation2)
                    .put("firstname3",res.get(i).firstname3)
                    .put("lastname3",res.get(i).lastname3)
                    .put("email3",res.get(i).email3)
                    .put("affilation3",res.get(i).affilation3)
                    .put("firstname4",res.get(i).firstname4)
                    .put("lastname4",res.get(i).lastname4)
                    .put("email4",res.get(i).email4)
                    .put("affilation4",res.get(i).affilation4)
                    .put("firstname5",res.get(i).firstname5)
                    .put("lastname5",res.get(i).lastname5)
                    .put("email5",res.get(i).email5)
                    .put("affilation5",res.get(i).affilation5)
                    .put("firstname6",res.get(i).firstname6)
                    .put("lastname6",res.get(i).lastname6)
                    .put("email6",res.get(i).email6)
                    .put("affilation6",res.get(i).affilation6)
                    .put("firstname7",res.get(i).firstname7)
                    .put("lastname7",res.get(i).lastname7)
                    .put("email7",res.get(i).email7)
                    .put("affilation7",res.get(i).affilation7)
                    .put("otherauthor", res.get(i).otherauthor)
                    .put("candidate", res.get(i).candidate)
                    .put("volunteer", res.get(i).volunteer)
                    .put("paperabstract", res.get(i).paperabstract)
                    .put("topic", res.get(i).topic)
                    .put("ifsubmit", res.get(i).ifsubmit)
                    .put("format", res.get(i).format)
                    .put("papersize", res.get(i).papersize)
                    .put("date", res.get(i).date)
                    .put("conference", res.get(i).conference)
                    .put("file", res.get(i).file)
                    .put("status", res.get(i).status)
                    .put("reviewerid", res.get(i).reviewerid);

            jsonarray.add(json);

        }
        //jsonarray.add({"status": "successful"});
        System.out.println(jsonarray);
        JsonNode temp = (JsonNode) jsonarray;
        return ok(temp);
    }
}
