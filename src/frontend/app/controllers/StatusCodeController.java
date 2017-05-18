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


public class StatusCodeController extends Controller{
    @Inject WSClient ws;
    private FormFactory formFactory;

    @Inject
    public StatusCodeController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    //todo revise homepage
    public Result GO_HOME = Results.redirect(
            routes.StatusCodeController.retriveAllStatusCode()
    );

    public Result index() {
        return GO_HOME;
    }
    //    public CompletionStage<Result> edit(Long id) {
    private StatusCode editStatusCode = new StatusCode();
    public CompletionStage<Result> edit(Long id) {
        Form<StatusCode> statuscodeForm = formFactory.form(StatusCode.class);
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        String temp = conferenceinfo.replaceAll(" ", "+");
        List<StatusCode> restemp = new ArrayList<StatusCode>();

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/statuscode/"+id).get();
        res.thenAccept(response -> {
            JsonNode ret = response.asJson();
            editStatusCode.label = ret.get("label").asText();
            editStatusCode.mailtemplate = ret.get("mailtemplate").asText();
            editStatusCode.camerareadyrequired = ret.get("camerareadyrequired").asText();
        });

        CompletionStage<WSResponse> res2 = ws.url("http://localhost:9000/statuscodes/all/"+temp).get();
        return res2.thenApply(response2 -> {
            JsonNode arr = response2.asJson();
            ArrayNode ret2 = (ArrayNode) arr;

            for (JsonNode res1 : ret2) {

                StatusCode editStatusCode2 = new StatusCode();
                editStatusCode2.id = Long.parseLong(res1.get("id").asText());
                editStatusCode2.label = res1.get("label").asText();
                editStatusCode2.mailtemplate = res1.get("mailtemplate").asText();
                editStatusCode2.camerareadyrequired = res1.get("camerareadyrequired").asText();
                //System.out.println("here ===" + res1);
                restemp.add(editStatusCode2);
            }
            //System.out.println("666========"+editStatusCode.label);
            return ok(
                    views.html.statuscode.render(id,restemp, statuscodeForm,editStatusCode, true)
            );
//                                    return ok(
//                                        views.html.statuscode.render(id,restemp, statuscodeForm,editStatusCode, true)
//                                        );
        });//second then apply

//        return ok(views.html.statuscode.render(id,restemp, statuscodeForm,editStatusCode, true));
    }
    //update a statuscode
    public CompletionStage<Result> update(Long id){
        Form<StatusCode> statuscodeForm = formFactory.form(StatusCode.class).bindFromRequest();
        StatusCode newStatusCode = statuscodeForm.get();
        JsonNode json = Json.newObject()
                .put("label", newStatusCode.label)
                .put("mailtemplate", newStatusCode.mailtemplate)
                .put("camerareadyrequired", newStatusCode.camerareadyrequired);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/statuscode/"+id).post(json);
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from update "+ret);

            return GO_HOME;

        });
    }


    public CompletionStage<Result> createStatusCode(){
        Form<StatusCode> statuscodeForm = formFactory.form(StatusCode.class).bindFromRequest();
        StatusCode newStatusCode = statuscodeForm.get();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        JsonNode json = Json.newObject()
                .put("label", newStatusCode.label)
                .put("mailtemplate", newStatusCode.mailtemplate)
                .put("camerareadyrequired", newStatusCode.camerareadyrequired)
                .put("conferenceinfo", conferenceinfo);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/statuscode").post(json);
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from create "+ret);

            return GO_HOME;

        });
    }

    public CompletionStage<Result> deleteStatusCode(Long id){
        Form<StatusCode> statuscodeForm = formFactory.form(StatusCode.class).bindFromRequest();
        StatusCode newStatusCode = statuscodeForm.get();
//        JsonNode json = Json.newObject()
//                .put("label", newStatusCode.label)
//                .put("explanations", newStatusCode.explanations)
//                .put("weight", newStatusCode.weight);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/statuscode/deleting/"+id).get();
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from delete "+ret);

            return GO_HOME;

        });
    }

    public CompletionStage<Result> retriveAllStatusCode(){
        Form<StatusCode> statuscodeForm = formFactory.form(StatusCode.class);
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        String temp = conferenceinfo.replaceAll(" ", "+");
        CompletionStage<WSResponse> resws = ws.url("http://localhost:9000/statuscodes/all/"+temp).get();
        List<StatusCode> res = new ArrayList<StatusCode>();
        return resws.thenApply(response -> {
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;

            for(JsonNode res1 : ret) {

                StatusCode editStatusCode = new StatusCode();
                editStatusCode.id = Long.parseLong(res1.get("id").asText());
                editStatusCode.label = res1.get("label").asText();
                editStatusCode.mailtemplate = res1.get("mailtemplate").asText();
                editStatusCode.camerareadyrequired = res1.get("camerareadyrequired").asText();
                System.out.println("here ===" + res1);
                res.add(editStatusCode);

            }
            StatusCode editStatusCode = new StatusCode();
            //Long id;
            return ok(
                    views.html.statuscode.render(Long.valueOf(0),res, statuscodeForm, editStatusCode, false)
            );
        });
    }

}