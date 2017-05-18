package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.server.type.ScalarTypeYear;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import models.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

import com.avaje.ebeaninternal.server.type.ScalarTypeYear;

import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import play.libs.Json;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Ling on 2017/3/27.
 */
public class AdminController extends Controller {
    @Inject
    WSClient ws;

    private FormFactory formFactory;

    @Inject
    public AdminController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GO_HOME = Results.redirect(
            routes.ShowConferenceController.showMyConference()
    );

    public CompletionStage<Result> adminPage(String conferenceinfo){
        Http.Session session = Http.Context.current().session();
//        conferenceinfo = conferenceinfo.replaceAll(" ","+");
        session.put("conferenceinfo", conferenceinfo);

        String conf_url = conferenceinfo.replace(" ","%20");
        String username = session.get("username");
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/role/"+conf_url+"/"+username).get();
        return res.thenApply(response -> {
            String ret = response.getBody();
            int role = Integer.parseInt(ret);
            System.out.println("===admin page role is "+role);
            session.put("role",Integer.toString(role));
            return ok(views.html.admin.render(conferenceinfo, role));
        });
    }

    public Result download()throws FileNotFoundException, IOException,InterruptedException{
        Http.Session session = Http.Context.current().session();
        String username = session.get("username");
        String conferenceinfo = session.get("conferenceinfo");
//        conferenceinfo = conferenceinfo.replaceAll("\\+"," ");
        final String conferenceinfo1 = conferenceinfo;
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/paper/" + username).get();
        List<Long> res = new ArrayList<>();
        List<String> ifsubmitlist = new ArrayList<>();

        resofrest.thenAccept(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                if(res1.get("conference").asText().equals(conferenceinfo1)){
                    savedPaper.id = Long.parseLong(res1.get("id").asText());
                    res.add(savedPaper.id);
                    ifsubmitlist.add(res1.get("ifsubmit").asText());
                }
            }
            System.out.println("complete");

        });


        TimeUnit.SECONDS.sleep(1);

            FileOutputStream fos = new FileOutputStream("/Users/shuang/uploads/"+conferenceinfo+".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            for(int i =0; i<res.size(); i++){
                if(ifsubmitlist.get(i).equals("Y")){
                    String path = "/Users/shuang/uploads/"+Long.toString(res.get(i));
                    addToZipFile(path, zos);
                }

            }
            zos.close();
            fos.close();



        System.out.println("downloading...");
        response().setContentType("application/x-download");
        String cmd = "attachment; filename="+conferenceinfo+".zip";
        response().setHeader("Content-disposition",cmd);
        String path = "/Users/shuang/uploads/"+conferenceinfo+".zip";
        //return ok(new File("/User/huiliangling/uploads/test.txt"));
        return ok(new java.io.File(path));

    }


    public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

        System.out.println("Writing '" + fileName + "' to zip file");

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }





}