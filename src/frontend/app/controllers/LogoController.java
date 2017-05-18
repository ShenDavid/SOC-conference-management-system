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
import java.util.concurrent.TimeUnit;

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

/**
 * Created by shuang on 3/29/17.
 */
public class LogoController extends Controller {
    @Inject WSClient ws;
    private FormFactory formFactory;

    @Inject
    public LogoController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /*public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );*/
    public Result GO_HOME = Results.redirect(
            routes.ShowConferenceController.showMyConference()
    );
    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return GO_HOME;
    }


    public Result uploadFile() {
        Form<Logo> logoForm = formFactory.form(Logo.class);
        return ok(
                views.html.logo.render(logoForm)
        );
    }
    public Result selectFile() {
        Form<Logo> logoForm = formFactory.form(Logo.class).bindFromRequest();

        Logo savedLogo = new Logo();
        System.out.println("begin upload file");
//        if (savedPaper != null) {
            System.out.println("upload file");
            Http.MultipartFormData body = request().body().asMultipartFormData();

            Http.MultipartFormData.FilePart<File> filePart = body.getFile("file");
        try{
            File file1 = new File("/Users/shuang/Downloads/18655-Spring-2017-Team-3/frontend/public/images/logo.png");
            if(file1.delete()){
                System.out.println(file1.getName() + " deleted！");
            }else{
                System.out.println("delete fail！");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

            try {
                File file = filePart.getFile();
                File destination = new File("/Users/shuang/Downloads/18655-Spring-2017-Team-3/frontend/public/images", "logo.png");
                FileUtils.moveFile(file, destination);
//                savedPaper.ifsubmit = "Y";
//                savedPaper.format = filePart.getContentType();
//                savedPaper.papersize = String.valueOf(destination.length());
//                System.out.println("File length  " + destination.length());
//                savedPaper.ifsubmit = "Y";
//                savedPaper.format = filePart.getContentType();
//                System.out.println("file size" + destination.length());
//                savedPaper.papersize = String.valueOf(destination.length());


            } catch (Exception e){
                e.printStackTrace();
            }
        return ok(
                views.html.logoSubmitted.render()
        );
//        JsonNode json = Json.newObject()
//                .put("ifsubmit", savedPaper.ifsubmit)
//                .put("format", savedPaper.format)
//                .put("papersize", savedPaper.papersize);
//        Http.Session session = Http.Context.current().session();


//        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/papers/"+id +"/upload").post(json);
//        return res.thenApplyAsync(response -> {
//            String ret = response.getBody();
//            System.out.println("response from update "+ret);
//            if ("update successfully".equals(ret)) {
//
//                return GO_HOME;
//            }else{
//                return GO_HOME;
//            }
//
//        });
    }



}
