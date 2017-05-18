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

    private FormFactory formFactory;

    @Inject
    public GenerateCSVController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result generateCSV(String confname) {
        //get all paper of this conference
        try {
            String name = confname.replaceAll("\\+", " ");

            List<Paper> paperList = Paper.ConfPapers(name);
            System.out.println("11===="+paperList);

            String outputFile = "/Users/shuang/uploads/paper.csv";

            File fout = new File(outputFile);
            FileOutputStream fos = new FileOutputStream(fout);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write("paper_name"+"\t"+"reviewid"+"\t"+"reviewstatus"+"\n");
            for (int i = 0; i < paperList.size(); i++) {
                Paper one = paperList.get(i);
                long paperid = one.id;
                String paper_name = one.title;
                System.out.println("10===="+paper_name);
                List<Review> paper_reviews = Review.find.where().eq("paperid", paperid).findList();
                System.out.println("11===="+paper_reviews);
                for(int j = 0 ; j < paper_reviews.size() ; j++){
                    Review paper_review = paper_reviews.get(j);
                    String reviewid = Long.toString(paper_review.reviewerid);
                    String reviewstatus = paper_review.reviewstatus;
                    System.out.println(paper_name+"\t"+reviewid+"\t"+reviewstatus+"\n");
                    bw.write(paper_name+"\t"+reviewid+"\t"+reviewstatus);
                    bw.newLine();
                }
            }

            bw.close();
            //download("paper.csv");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return ok();
    }


}