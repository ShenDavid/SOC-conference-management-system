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
import play.libs.Json;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.util.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class ReviewQuestionController extends Controller {
    private FormFactory formFactory;

    @Inject
    public ReviewQuestionController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result edit(Long id) {
        Form<ReviewQuestion> questionForm = formFactory.form(ReviewQuestion.class);
        ReviewQuestion newQuestion = ReviewQuestion.find.byId(id);

        JsonNode json = Json.newObject()
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


        return ok(json);
    }

    public Result update(Long id) throws PersistenceException {
        Form<ReviewQuestion> questionForm = formFactory.form(ReviewQuestion.class).bindFromRequest();

        Transaction txn = Ebean.beginTransaction();
        try {
            ReviewQuestion oldQuestion = ReviewQuestion.find.byId(id);
            if (oldQuestion != null) {
                ReviewQuestion newQuestion = questionForm.get();

                oldQuestion.question = newQuestion.question;
                oldQuestion.isPublic =  newQuestion.isPublic;
                oldQuestion.listOfChoice1 =  newQuestion.listOfChoice1;
                oldQuestion.position1 =  newQuestion.position1;
                oldQuestion.listOfChoice2 =  newQuestion.listOfChoice2;
                oldQuestion.position2 =  newQuestion.position2;
                oldQuestion.listOfChoice3 =  newQuestion.listOfChoice3;
                oldQuestion.position3 =  newQuestion.position3;
                oldQuestion.listOfChoice4 =  newQuestion.listOfChoice4;
                oldQuestion.position4 =  newQuestion.position4;
                oldQuestion.listOfChoice5 =  newQuestion.listOfChoice5;
                oldQuestion.position5 =  newQuestion.position5;
                oldQuestion.listOfChoice6 =  newQuestion.listOfChoice6;
                oldQuestion.position6 =  newQuestion.position6;
                oldQuestion.listOfChoice7 =  newQuestion.listOfChoice7;
                oldQuestion.position7 =  newQuestion.position7;

                oldQuestion.update();
                flash("success", "Question " + questionForm.get().question + " has been updated");
                txn.commit();
            }
        } finally {
            txn.end();
        }

        return ok("update successfully");
    }

    public Result createQuestion() {
        Form<ReviewQuestion> questionForm = formFactory.form(ReviewQuestion.class).bindFromRequest();
        ReviewQuestion newQuestion = questionForm.get();
        questionForm.get().save();
        return ok("create a criteria successfully");
    }

    public Result deleteQuestion(Long id) {
        //Form<Criteria> criteriaForm = formFactory.form(Criteria.class).bindFromRequest();
        //Criteria newCriteria = criteriaForm.get();
        //criteriaForm.get().save();
        ReviewQuestion temp = ReviewQuestion.find.byId(id);
        if(temp == null) {
            return ok("Not found");
        } else {
            temp.delete();
            return ok("delete successfully");
        }

    }

    public Result retriveAll(String name){
        String conferenceinfo = name.replaceAll("\\+", " ");
        Form<ReviewQuestion> questionForm = formFactory.form(ReviewQuestion.class);
        //List<ReviewQuestion> temp = ReviewQuestion.find.all();
        String confname = conferenceinfo.replaceAll("\\+", " ");
        List<ReviewQuestion> temp = ReviewQuestion.GetMyConferenceQuestion(confname);
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonarray = new ArrayNode(factory);
        for(int i=0; i< temp.size(); i++){
            JsonNode json = Json.newObject()
                    .put("id", temp.get(i).id)
                    .put("question", temp.get(i).question)
                    .put("isPublic", temp.get(i).isPublic)
                    .put("listOfChoice1", temp.get(i).listOfChoice1)
                    .put("position1", temp.get(i).position1)
                    .put("listOfChoice2", temp.get(i).listOfChoice2)
                    .put("position2", temp.get(i).position2)
                    .put("listOfChoice3", temp.get(i).listOfChoice3)
                    .put("position3", temp.get(i).position3)
                    .put("listOfChoice4", temp.get(i).listOfChoice4)
                    .put("position4", temp.get(i).position4)
                    .put("listOfChoice5", temp.get(i).listOfChoice5)
                    .put("position5", temp.get(i).position5)
                    .put("listOfChoice6", temp.get(i).listOfChoice6)
                    .put("position6", temp.get(i).position6)
                    .put("listOfChoice7", temp.get(i).listOfChoice7)
                    .put("position7", temp.get(i).position7);

            System.out.println("here ====666" + json);
            jsonarray.add(json);
        }
        JsonNode tempnode = (JsonNode) jsonarray;

        return ok(tempnode);
    }

}