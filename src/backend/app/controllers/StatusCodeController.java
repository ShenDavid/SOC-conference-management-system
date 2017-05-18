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


public class StatusCodeController extends Controller {
    private FormFactory formFactory;

    @Inject
    public StatusCodeController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result edit(Long id) {
        Form<StatusCode> statusForm = formFactory.form(StatusCode.class);
        StatusCode newStatusCode = StatusCode.find.byId(id);

        JsonNode json = Json.newObject()
                .put("label", newStatusCode.label)
                .put("mailtemplate", newStatusCode.mailtemplate)
                .put("camerareadyrequired", newStatusCode.camerareadyrequired);


        return ok(json);
    }

    public Result update(Long id) throws PersistenceException {
        Form<StatusCode> statusForm = formFactory.form(StatusCode.class).bindFromRequest();

        Transaction txn = Ebean.beginTransaction();
        try {
            StatusCode oldStatusCode = StatusCode.find.byId(id);
            if (oldStatusCode != null) {
                StatusCode newStatusCode = statusForm.get();

                oldStatusCode.label = newStatusCode.label;
                oldStatusCode.mailtemplate = newStatusCode.mailtemplate;
                oldStatusCode.camerareadyrequired = newStatusCode.camerareadyrequired;

                oldStatusCode.update();
                flash("success", "StatusCode " + statusForm.get().label + " has been updated");
                txn.commit();
            }
        } finally {
            txn.end();
        }

        return ok("update successfully");
    }

    public Result createStatusCode() {
        Form<StatusCode> statusForm = formFactory.form(StatusCode.class).bindFromRequest();
        StatusCode newStatusCode = statusForm.get();
        statusForm.get().save();
        return ok("create a statuscode successfully");
    }

    public Result deleteStatusCode(Long id) {
        //Form<StatusCode> statusForm = formFactory.form(StatusCode.class).bindFromRequest();
        //StatusCode newStatusCode = statusForm.get();
        //statusForm.get().save();
        StatusCode temp = StatusCode.find.byId(id);
        if(temp == null) {
            return ok("Not found");
        } else {
            temp.delete();
            return ok("delete successfully");
        }

    }

    public Result retriveAll(String conferenceinfo){
        Form<StatusCode> statusForm = formFactory.form(StatusCode.class);
        String confname = conferenceinfo.replaceAll("\\+", " ");
        System.out.println("=====me"+confname);
        List<StatusCode> temp = StatusCode.GetMyConferenceStatusCode(confname);
        //List<StatusCode> temp = StatusCode.find.all();
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonarray = new ArrayNode(factory);
        for(int i=0; i< temp.size(); i++){
            JsonNode json = Json.newObject()
                    .put("id", temp.get(i).id)
                    .put("label", temp.get(i).label)
                    .put("mailtemplate", temp.get(i).mailtemplate)
                    .put("camerareadyrequired", temp.get(i).camerareadyrequired);

            jsonarray.add(json);
        }
        JsonNode tempnode = (JsonNode) jsonarray;
        return ok(tempnode);
    }

}