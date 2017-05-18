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
public class ShowConferenceController extends Controller{

    private FormFactory formFactory;

    @Inject
    public ShowConferenceController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }



    /**
     * Handle profile deletion
     */
    public Result showMyConference(String username) {
        Form<Conference> conferenceForm = formFactory.form(Conference.class).bindFromRequest();
        Conference conferenceInfo = new Conference();


        List<Conference> res = new ArrayList<Conference>();
        res = conferenceInfo.GetMyConference(username);

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonarray = new ArrayNode(factory);
        for(int i=0; i< res.size(); i++){
            JsonNode json = Json.newObject()
                    .put("id", res.get(i).id)
//                    .put("username", username)
                    .put("title", res.get(i).title)
                    .put("location", res.get(i).location)
                    .put("date", res.get(i).date)
                    .put("status",res.get(i).status)
                    .put("ifreviewer",res.get(i).ifreviewer)
                    .put("ifadmin",res.get(i).ifadmin)
                    .put("ifchair",res.get(i).ifchair);
             jsonarray.add(json);

        }
        //jsonarray.add({"status": "successful"});
        System.out.println(jsonarray);
        JsonNode temp = (JsonNode) jsonarray;
        return ok(temp);

    }


}
