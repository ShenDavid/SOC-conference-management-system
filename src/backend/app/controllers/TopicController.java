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
 * Created by shuang on 3/29/17.
 */
public class TopicController extends Controller {
    private FormFactory formFactory;

    @Inject
    public TopicController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }



    /**
     * Handle default path requests, redirect to computers list
     */
//    public Result index() {
//        return GO_HOME;
//    }

    public Result edit(Long id) {

        Form<Topic> topicForm = formFactory.form(Topic.class);
        Topic newTopic = Topic.find.byId(id);

        JsonNode json = Json.newObject()
                .put("topic", newTopic.topic)
                .put("id", newTopic.id);

        return ok(json);
    }
    public Result update(Long id) throws PersistenceException {
        Form<Topic> topicForm = formFactory.form(Topic.class).bindFromRequest();

        Transaction txn = Ebean.beginTransaction();
        try {
            Topic savedTopic = Topic.find.byId(id);
            if (savedTopic != null) {
                Topic newTopicData = topicForm.get();

                savedTopic.topic = newTopicData.topic;

                savedTopic.update();
                txn.commit();
            }
        } finally {
            txn.end();
        }

        return ok("update successfully");
    }

    public Result save() {
        Form<Topic> topicForm = formFactory.form(Topic.class).bindFromRequest();

        Topic newTopic = topicForm.get();

        topicForm.get().save();


        return ok("save successfully");
    }

    public Result delete(Long id) {

        Topic temp = Topic.find.byId(id);

        temp.delete();
        return ok("delete successfully");


    }

    public Result showMyTopic(String conference) {
        Form<Topic> topicForm = formFactory.form(Topic.class).bindFromRequest();
        Topic topicInfo = new Topic();
        conference = conference.replaceAll("\\+"," ");

        List<Topic> res = new ArrayList<Topic>();
        res = topicInfo.GetMyTopic(conference);

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonarray = new ArrayNode(factory);
        for(int i=0; i< res.size(); i++){
            JsonNode json = Json.newObject()
                    .put("id", res.get(i).id)
                    .put("topic", res.get(i).topic);

            jsonarray.add(json);

        }
        //jsonarray.add({"status": "successful"});
        System.out.println(jsonarray);
        JsonNode temp = (JsonNode) jsonarray;
        return ok(temp);
    }

}




