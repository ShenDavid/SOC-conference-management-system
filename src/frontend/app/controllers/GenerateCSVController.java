package controllers;

import models.Profile;
//import org.hibernate.validator.constraints.Email;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;

import javax.inject.Inject;
import models.User;
import play.mvc.Result;
import play.mvc.Results;

import java.util.*;
import play.data.validation.ValidationError;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import javax.ws.rs.core.MediaType;
import play.mvc.Http.Session;
import java.util.concurrent.CompletableFuture;
import play.mvc.Http;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

import java.util.Random;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import play.mvc.Http.Session;

import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class GenerateCSVController extends Controller {
    @Inject
    WSClient ws;

    private FormFactory formFactory;

    @Inject
    public GenerateCSVController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GO_HOME = Results.redirect(
            routes.ShowConferenceController.showMyConference()
    );

    public CompletionStage<Result> generateCSV() {

        ///Users/shuang/uploads
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        String confurl = conferenceinfo.replaceAll(" ","+");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/generatecsv/" + confurl).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            return GO_HOME;
        });
    }
    public Result downloadCSV(){
        String filename = "paper.csv";
        System.out.println("downloading...");
        response().setContentType("application/x-download");
        String cmd = "attachment; filename="+filename;
        response().setHeader("Content-disposition",cmd);
        String path = "/Users/shuang/uploads/"+filename;
        //return ok(new File("/User/huiliangling/uploads/test.txt"));
        return ok(new java.io.File(path));

    }
}