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


public class CriteriaController extends Controller {
    private FormFactory formFactory;

    @Inject
    public CriteriaController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result edit(Long id) {
        Form<Criteria> criteriaForm = formFactory.form(Criteria.class);
        Criteria newCriteria = Criteria.find.byId(id);

        JsonNode json = Json.newObject()
                .put("label", newCriteria.label)
                .put("explanations", newCriteria.explanations)
                .put("weight", newCriteria.weight);


        return ok(json);
    }

    public Result update(Long id) throws PersistenceException {
        Form<Criteria> criteriaForm = formFactory.form(Criteria.class).bindFromRequest();

        Transaction txn = Ebean.beginTransaction();
        try {
            Criteria oldCriteria = Criteria.find.byId(id);
            if (oldCriteria != null) {
                Criteria newCriteria = criteriaForm.get();

            oldCriteria.label = newCriteria.label;
            oldCriteria.explanations = newCriteria.explanations;
            oldCriteria.weight = newCriteria.weight;

            oldCriteria.update();
                flash("success", "Criteria " + criteriaForm.get().label + " has been updated");
                txn.commit();
            }
        } finally {
            txn.end();
        }

        return ok("update successfully");
    }

    public Result createCriteria() {
        Form<Criteria> criteriaForm = formFactory.form(Criteria.class).bindFromRequest();
        Criteria newCriteria = criteriaForm.get();
        criteriaForm.get().save();
        return ok("create a criteria successfully");
    }

    public Result deleteCriteria(Long id) {
        //Form<Criteria> criteriaForm = formFactory.form(Criteria.class).bindFromRequest();
        //Criteria newCriteria = criteriaForm.get();
        //criteriaForm.get().save();
        Criteria temp = Criteria.find.byId(id);
        if(temp == null) {
            return ok("Not found");
        } else {
            temp.delete();
            return ok("delete successfully");
        }

    }

    public Result retriveAll(String name){
        String conferenceinfo = name.replaceAll("\\+", " ");
        Form<Criteria> criteriaForm = formFactory.form(Criteria.class);
        String confname = conferenceinfo.replaceAll("\\+", " ");
        List<Criteria> temp = Criteria.GetMyConferenceCriteria(confname);
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonarray = new ArrayNode(factory);
        for(int i=0; i< temp.size(); i++){
            JsonNode json = Json.newObject()
                    .put("id", temp.get(i).id)
                    .put("label", temp.get(i).label)
                    .put("explanations", temp.get(i).explanations)
                    .put("weight", temp.get(i).weight);

            jsonarray.add(json);
        }
        JsonNode tempnode = (JsonNode) jsonarray;
        return ok(tempnode);
    }

}