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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import play.libs.Json;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.util.*;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class ReviewQuestionController extends Controller{
    @Inject WSClient ws;
    private FormFactory formFactory;

    @Inject
    public ReviewQuestionController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    //todo revise homepage
    public Result GO_HOME = Results.redirect(
            routes.ReviewQuestionController.retriveAll()
    );

    public Result index() {
        return GO_HOME;
    }
    //    public CompletionStage<Result> edit(Long id) {
    private ReviewQuestion editQuestion = new ReviewQuestion();
    public CompletionStage<Result> edit(Long id) {
        Form<ReviewQuestion> questionForm = formFactory.form(ReviewQuestion.class);
        //ReviewQuestion editQuestion = new ReviewQuestion();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        String tempstr = conferenceinfo.replaceAll(" ", "+");
        List<ReviewQuestion> restemp = new ArrayList<ReviewQuestion>();

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/reviewquestion/"+id).get();
        res.thenAccept(response -> {
            JsonNode ret = response.asJson();
            editQuestion.question = ret.get("question").asText();
            editQuestion.isPublic = ret.get("isPublic").asText();
            editQuestion.listOfChoice1 = ret.get("listOfChoice1").asText();
            editQuestion.position1 = ret.get("position1").asText();
            editQuestion.listOfChoice2 = ret.get("listOfChoice2").asText();
            editQuestion.position2 = ret.get("position2").asText();
            editQuestion.listOfChoice3 = ret.get("listOfChoice3").asText();
            editQuestion.position3 = ret.get("position3").asText();
            editQuestion.listOfChoice4 = ret.get("listOfChoice4").asText();
            editQuestion.position4 = ret.get("position4").asText();
            editQuestion.listOfChoice5 = ret.get("listOfChoice5").asText();
            editQuestion.position5 = ret.get("position5").asText();
            editQuestion.listOfChoice6 = ret.get("listOfChoice6").asText();
            editQuestion.position6 = ret.get("position6").asText();
            editQuestion.listOfChoice7 = ret.get("listOfChoice7").asText();
            editQuestion.position7 = ret.get("position7").asText();
        });

        CompletionStage<WSResponse> res2 = ws.url("http://localhost:9000/reviewquestions/all/"+tempstr).get();
        return res2.thenApply(response2 -> {
            JsonNode arr = response2.asJson();
            ArrayNode ret2 = (ArrayNode) arr;

            for (JsonNode res1 : ret2) {

                ReviewQuestion editQuestion2 = new ReviewQuestion();
                editQuestion2.id = Long.parseLong(res1.get("id").asText());
                editQuestion2.question = res1.get("question").asText();
                editQuestion2.isPublic = res1.get("isPublic").asText();
                editQuestion2.listOfChoice1 = res1.get("listOfChoice1").asText();
                editQuestion2.position1 = res1.get("position1").asText();
                editQuestion2.listOfChoice2 = res1.get("listOfChoice2").asText();
                editQuestion2.position2 = res1.get("position2").asText();
                editQuestion2.listOfChoice3 = res1.get("listOfChoice3").asText();
                editQuestion2.position3 = res1.get("position3").asText();
                editQuestion2.listOfChoice4 = res1.get("listOfChoice4").asText();
                editQuestion2.position4 = res1.get("position4").asText();
                editQuestion2.listOfChoice5 = res1.get("listOfChoice5").asText();
                editQuestion2.position5 = res1.get("position5").asText();
                editQuestion2.listOfChoice6 = res1.get("listOfChoice6").asText();
                editQuestion2.position6 = res1.get("position6").asText();
                editQuestion2.listOfChoice7 = res1.get("listOfChoice7").asText();
                editQuestion2.position7 = res1.get("position7").asText();

                System.out.println("here ===" + res1);
                restemp.add(editQuestion2);
            }
            //System.out.println("666========"+editCriteria.label);
            return ok(
                    views.html.reviewquestion.render(id,restemp, questionForm,editQuestion, true)
            );
//                                    return ok(
//                                        views.html.criteria.render(id,restemp, criteriaForm,editCriteria, true)
//                                        );
        });

//        return ok(views.html.criteria.render(id,restemp, criteriaForm,editCriteria, true));
    }
    //update a criteria
    public CompletionStage<Result> update(Long id){
        Form<ReviewQuestion> questionForm = formFactory.form(ReviewQuestion.class).bindFromRequest();
        ReviewQuestion newQuestion = questionForm.get();
        JsonNode json = Json.newObject()
                //.put("question", temp.get(i).question)
                .put("question", newQuestion.question)
                .put("isPublic", newQuestion.isPublic)
                .put("listOfChoice1", newQuestion.listOfChoice1)
                .put("position1", newQuestion.position1)
                .put("listOfChoice2", newQuestion.listOfChoice2)
                .put("position2", newQuestion.position2)
                .put("listOfChoice3", newQuestion.listOfChoice3)
                .put("position3", newQuestion.position3)
                .put("listOfChoice4", newQuestion.listOfChoice4)
                .put("position4", newQuestion.position4)
                .put("listOfChoice5", newQuestion.listOfChoice5)
                .put("position5", newQuestion.position5)
                .put("listOfChoice6", newQuestion.listOfChoice6)
                .put("position6", newQuestion.position6)
                .put("listOfChoice7", newQuestion.listOfChoice7)
                .put("position7", newQuestion.position7);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/reviewquestion/"+id).post(json);
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from update "+ret);

            return GO_HOME;

        });
    }


    public CompletionStage<Result> createQuestion(){
        Form<ReviewQuestion> questionForm = formFactory.form(ReviewQuestion.class).bindFromRequest();
        ReviewQuestion newQuestion = questionForm.get();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        String tempstr = conferenceinfo.replaceAll(" ", "+");
        JsonNode json = Json.newObject()
                //.put("question", temp.get(i).question)
                .put("question", newQuestion.question)
                .put("isPublic", newQuestion.isPublic)
                .put("conferenceinfo",conferenceinfo)
                .put("listOfChoice1", newQuestion.listOfChoice1)
                .put("position1", newQuestion.position1)
                .put("listOfChoice2", newQuestion.listOfChoice2)
                .put("position2", newQuestion.position2)
                .put("listOfChoice3", newQuestion.listOfChoice3)
                .put("position3", newQuestion.position3)
                .put("listOfChoice4", newQuestion.listOfChoice4)
                .put("position4", newQuestion.position4)
                .put("listOfChoice5", newQuestion.listOfChoice5)
                .put("position5", newQuestion.position5)
                .put("listOfChoice6", newQuestion.listOfChoice6)
                .put("position6", newQuestion.position6)
                .put("listOfChoice7", newQuestion.listOfChoice7)
                .put("position7", newQuestion.position7);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/reviewquestion").post(json);
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from create "+ret);

            return GO_HOME;

        });
    }

    public CompletionStage<Result> deleteQuestion(Long id){
        Form<ReviewQuestion> questionForm = formFactory.form(ReviewQuestion.class).bindFromRequest();
        ReviewQuestion newQuestion = questionForm.get();
//        JsonNode json = Json.newObject()
//                .put("label", newCriteria.label)
//                .put("explanations", newCriteria.explanations)
//                .put("weight", newCriteria.weight);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/reviewquestion/deleting/"+id).get();
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from delete "+ret);

            return GO_HOME;

        });
    }

    public CompletionStage<Result> retriveAll(){
        Form<ReviewQuestion> questionForm = formFactory.form(ReviewQuestion.class);
        //System.out.println("here ===11");
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        String tempstr = conferenceinfo.replaceAll(" ", "+");
        CompletionStage<WSResponse> resws = ws.url("http://localhost:9000/reviewquestions/all/"+tempstr).get();
        //System.out.println("here ===22");
        List<ReviewQuestion> res = new ArrayList<ReviewQuestion>();
        return resws.thenApply(response -> {
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;

            for(JsonNode res1 : ret) {
                //System.out.println("here ===22"+res1);
                ReviewQuestion editQuestion = new ReviewQuestion();
                editQuestion.id = Long.parseLong(res1.get("id").asText());
                editQuestion.question = res1.get("question").asText();
                editQuestion.isPublic = res1.get("isPublic").asText();
                editQuestion.listOfChoice1 = res1.get("listOfChoice1").asText();
                editQuestion.position1 = res1.get("position1").asText();
                editQuestion.listOfChoice2 = res1.get("listOfChoice2").asText();
                editQuestion.position2 = res1.get("position2").asText();
                editQuestion.listOfChoice3 = res1.get("listOfChoice3").asText();
                editQuestion.position3 = res1.get("position3").asText();
                editQuestion.listOfChoice4 = res1.get("listOfChoice4").asText();
                editQuestion.position4 = res1.get("position4").asText();
                editQuestion.listOfChoice5 = res1.get("listOfChoice5").asText();
                editQuestion.position5 = res1.get("position5").asText();
                editQuestion.listOfChoice6 = res1.get("listOfChoice6").asText();
                editQuestion.position6 = res1.get("position6").asText();
                editQuestion.listOfChoice7 = res1.get("listOfChoice7").asText();
                editQuestion.position7 = res1.get("position7").asText();
                //System.out.println("here ===" + res1);
                res.add(editQuestion);

            }
            ReviewQuestion editQestion = new ReviewQuestion();
            //Long id;
            return ok(
                    views.html.reviewquestion.render(Long.valueOf(0),res, questionForm, editQestion, false)
            );
        });
    }

}