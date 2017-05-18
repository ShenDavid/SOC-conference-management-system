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


public class CriteriaController extends Controller{
    @Inject WSClient ws;
    private FormFactory formFactory;

    @Inject
    public CriteriaController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    //todo revise homepage
    public Result GO_HOME = Results.redirect(
            routes.CriteriaController.retriveAllCriteria()
    );

    public Result index() {
        return GO_HOME;
    }
//    public CompletionStage<Result> edit(Long id) {
    private Criteria editCriteria = new Criteria();
    public CompletionStage<Result> edit(Long id) {
        Form<Criteria> criteriaForm = formFactory.form(Criteria.class);
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        String tempstr = conferenceinfo.replaceAll(" ", "+");
        List<Criteria> restemp = new ArrayList<Criteria>();

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/criteria/"+id).get();
        res.thenAccept(response -> {
            JsonNode ret = response.asJson();
            editCriteria.label = ret.get("label").asText();
            editCriteria.explanations = ret.get("explanations").asText();
            editCriteria.weight = ret.get("weight").asText();
        });

        CompletionStage<WSResponse> res2 = ws.url("http://localhost:9000/criterias/all/"+tempstr).get();
        return res2.thenApply(response2 -> {
            JsonNode arr = response2.asJson();
            ArrayNode ret2 = (ArrayNode) arr;

            for (JsonNode res1 : ret2) {

                Criteria editCriteria2 = new Criteria();
                editCriteria2.id = Long.parseLong(res1.get("id").asText());
                editCriteria2.label = res1.get("label").asText();
                editCriteria2.explanations = res1.get("explanations").asText();
                editCriteria2.weight = res1.get("weight").asText();
                System.out.println("here ===" + res1);
                restemp.add(editCriteria2);
            }
            System.out.println("666========"+editCriteria.label);
            return ok(
                    views.html.criteria.render(id,restemp, criteriaForm,editCriteria, true)
            );
//                                    return ok(
//                                        views.html.criteria.render(id,restemp, criteriaForm,editCriteria, true)
//                                        );
        });//second then apply

//        return ok(views.html.criteria.render(id,restemp, criteriaForm,editCriteria, true));
    }
    //update a criteria
    public CompletionStage<Result> update(Long id){
        Form<Criteria> criteriaForm = formFactory.form(Criteria.class).bindFromRequest();
        Criteria newCriteria = criteriaForm.get();
        JsonNode json = Json.newObject()
                .put("label", newCriteria.label)
                .put("explanations", newCriteria.explanations)
                .put("weight", newCriteria.weight);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/criteria/"+id).post(json);
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from update "+ret);

            return GO_HOME;

        });
    }


    public CompletionStage<Result> createCriteria(){
        Form<Criteria> criteriaForm = formFactory.form(Criteria.class).bindFromRequest();
        Criteria newCriteria = criteriaForm.get();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        JsonNode json = Json.newObject()
                .put("label", newCriteria.label)
                .put("explanations", newCriteria.explanations)
                .put("weight", newCriteria.weight)
                .put("conferenceinfo", conferenceinfo);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/criteria").post(json);
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from create "+ret);

            return GO_HOME;

        });
    }

    public CompletionStage<Result> deleteCriteria(Long id){
        Form<Criteria> criteriaForm = formFactory.form(Criteria.class).bindFromRequest();
        Criteria newCriteria = criteriaForm.get();
//        JsonNode json = Json.newObject()
//                .put("label", newCriteria.label)
//                .put("explanations", newCriteria.explanations)
//                .put("weight", newCriteria.weight);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/criteria/deleting/"+id).get();
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from delete "+ret);

            return GO_HOME;

        });
    }

    public CompletionStage<Result> retriveAllCriteria(){
        Form<Criteria> criteriaForm = formFactory.form(Criteria.class);
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        String tempstr = conferenceinfo.replaceAll(" ", "+");
        CompletionStage<WSResponse> resws = ws.url("http://localhost:9000/criterias/all/"+tempstr).get();
        List<Criteria> res = new ArrayList<Criteria>();
        return resws.thenApply(response -> {
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;

            for(JsonNode res1 : ret) {

                Criteria editCriteria = new Criteria();
                editCriteria.id = Long.parseLong(res1.get("id").asText());
                editCriteria.label = res1.get("label").asText();
                editCriteria.explanations = res1.get("explanations").asText();
                editCriteria.weight = res1.get("weight").asText();
                System.out.println("here ===" + res1);
                res.add(editCriteria);

            }
            Criteria editCriteria = new Criteria();
            //Long id;
            return ok(
                    views.html.criteria.render(Long.valueOf(0),res, criteriaForm, editCriteria, false)
            );
        });
    }

}