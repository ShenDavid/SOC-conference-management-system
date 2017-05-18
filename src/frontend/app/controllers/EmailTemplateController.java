package controllers;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.server.type.ScalarTypeYear;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import java.text.SimpleDateFormat;
import java.text.ParseException;

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

import com.avaje.ebeaninternal.server.type.ScalarTypeYear;

import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import play.libs.Json;
import org.apache.commons.mail.*;

public class EmailTemplateController extends Controller {
    @Inject
    WSClient ws;

    private FormFactory formFactory;

    @Inject
    public EmailTemplateController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GO_HOME = Results.redirect(
            routes.ShowConferenceController.showMyConference()
    );

    public Result emailTemplate(){
        //SendTemplateEmail("helen");
            return ok(views.html.emailTemplate.render());
    }

    /*
     * When get request send to view paper email template
     */
    public CompletionStage<Result> TypeEmailTemplate(String email_type){
        //String email_type = "AC_PAPER";
        Form<EmailTemplate> EmailTemplateForm = formFactory.form(EmailTemplate.class);

        //Session session = Http.Context.current().session();
        Http.Session session = Http.Context.current().session();
        String chair_name = session.get("username");
        String conf = session.get("conferenceinfo");
        String conf_url = conf.replace(" ","%20");

        //get old emailTemplate
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/emailTemplate/"+email_type+"/"+chair_name+"/"+conf_url).get();
        return res.thenApplyAsync(response -> {
            JsonNode ret = response.asJson();
            if ("successful".equals(ret.get("status").asText())) {
                String template_html = ConvertedToHtmlVersion(ret.get("template").asText());
                return ok(views.html.TypePaperEmailTemplate.render(EmailTemplateForm, template_html, ret.get("subject").asText()));
            }else{
                return ok(views.html.emailTemplate.render());
            }
        });
    }

    /*
     * get request send to modify ac paper template
     */
    public CompletionStage<Result> GetModifyEmailTemplate(String email_type){
        //String email_type = "AC_PAPER";
        Form<EmailTemplate> EmailTemplateForm = formFactory.form(EmailTemplate.class);

        //Session session = Http.Context.current().session();
        Http.Session session = Http.Context.current().session();
        String chair_name = session.get("username");
        String conf = session.get("conferenceinfo");
        String conf_url = conf.replace(" ","%20");

        //get old emailTemplate
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/emailTemplate/"+email_type+"/"+chair_name+"/"+conf_url).get();
        return res.thenApplyAsync(response -> {
            JsonNode ret = response.asJson();
            if ("successful".equals(ret.get("status").asText())) {
                String template_html = ConvertedToTextarea(ret.get("template").asText());
                return ok(views.html.ModifyPaperEmailTemplate.render(EmailTemplateForm, template_html, ret.get("subject").asText(), email_type));
            }else{
                return ok(views.html.emailTemplate.render());
            }
        });

        //return ok(views.html.emailTemplate.render(EmailTemplateForm, emailTemplate));
    }

    private static String ConvertedToHtmlVersion(String template)
    {
        String result = template.replace("\\n","<br>");
        return result;
    }

    private static String ConvertedToTextarea(String template)
    {
        return template.replace("\\n", "\n");
    }

    private static String ConvertedToRaw(String template)
    {
        return template.replace("\n", "\\n");
    }

    /* post request send to ac paper email template
     */
    public Result updateEmailTemplate()
    {
        //Session session = Http.Context.current().session();
        Http.Session session = Http.Context.current().session();
        String pcchair_name = session.get("username");
        String conf = session.get("conferenceinfo");
        Form<EmailTemplate> EmailTemplateForm = formFactory.form(EmailTemplate.class).bindFromRequest();
        EmailTemplate new_emailtemplate = EmailTemplateForm.get();
        new_emailtemplate.chair_name = pcchair_name;
        System.out.println("6666=======updating "+new_emailtemplate.email_type);

        JsonNode json = Json.newObject()
                .put("chair_name", pcchair_name)
                .put("email_type", new_emailtemplate.email_type)
                .put("conference",conf)
                .put("subject", new_emailtemplate.subject)
                .put("template", ConvertedToRaw(new_emailtemplate.template));
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/emailTemplate").post(json);
        return ok(views.html.emailTemplate.render());
    }

//    public Result updateEmailTemplate()
//    {
//        Session session = Http.Context.current().session();
//        String pcchair_name = session.get("username");
//        Form<EmailTemplate> EmailTemplateForm = formFactory.form(EmailTemplate.class).bindFromRequest();
//        EmailTemplate new_emailtemplate = EmailTemplateForm.get();
//        new_emailtemplate.chair_name = pcchair_name;
//
//        JsonNode json = Json.newObject()
//                .put("pcchair_name", pcchair_name)
//                .put("template", new_emailtemplate.template);
//        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/emailTemplate").post(json);
//        return GO_HOME;
//    }

    //TODO uncomment and finish it
    /*
    private static String emailTemplate_onlyForSendReviewerReminder = "";//put ouside
    public CompletionStage<Result> SendUnreviewerReminder()
    {
        Session session = Http.Context.current().session();
        String chair_name = session.get("username");
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/emailTemplate/"+email_type+"/"+chair_name).get();
        res.thenAccept(response -> {
            JsonNode ret = response.asJson();
            if ("successful".equals(ret.get("status").asText())) {
                emailTemplate_onlyForSendReviewerReminder = ret.get("template").asText();
                //System.out.println("2======="+emailTemplate_onlyForSendTemplateEmail);
                //try put inside
                CompletionStage<WSResponse> res2 = ws.url("http://localhost:9000/unreviewerInfo").get();
                res2.thenAccept(response2 -> {
                    JsonNode arr = response.asJson();
                    ArrayNode ret2 = (ArrayNode) arr;
                    for(JsonNode info : ret2){
                        String final_template = replace_template(emailTemplate_onlyForSendReviewerReminder, info);

                    }
                });

                //return ok(views.html.emailTemplate.render(EmailTemplateForm, ));
            }else{
                //System.out.println("3=====get template failed");
                //return GO_HOME;
            }
        });
    }
    */
    /* @param: name of the receiver
     * 1. get email of receiver and email template of this sender
     * 2. send email
     */
    private static String emailTemplate_onlyForSendTemplateEmail = "";//put ouside
    public void SendTemplateEmail(String receiver_name)
    {
        //String emailTemplate = "";
        //Session session = Http.Context.current().session();
        Http.Session session = Http.Context.current().session();
        String pcchair_name = session.get("username");
        //System.out.println("=======1 username is "+pcchair_name);
        String email_type = "AC_PAPER";

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/emailTemplate/"+email_type+"/"+pcchair_name).get();
        res.thenAccept(response -> {
            JsonNode ret = response.asJson();
            if ("successful".equals(ret.get("status").asText())) {
                emailTemplate_onlyForSendTemplateEmail = ConvertedToTextarea(ret.get("template").asText());
                //System.out.println("2======="+emailTemplate_onlyForSendTemplateEmail);
                //try put inside
                CompletionStage<WSResponse> res2 = ws.url("http://localhost:9000/email/"+receiver_name).get();
                res2.thenAccept(response2 -> {
                    JsonNode ret2 = response2.asJson();
                    if ("successful".equals(ret.get("status").asText())) {
                        String email = ret2.get("email").asText();
                        //System.out.println("===6In sending template email "+email);
                        //System.out.println("===7In sending template template is "+ emailTemplate_onlyForSendTemplateEmail);

                        SendEmail(email, emailTemplate_onlyForSendTemplateEmail);
                        //return ok(views.html.emailTemplate.render(EmailTemplateForm, ));
                    }else{
                        //return GO_HOME;
                    }
                });

                //return ok(views.html.emailTemplate.render(EmailTemplateForm, ));
            }else{
                //System.out.println("3=====get template failed");
                //return GO_HOME;
            }
        });

        //System.out.println("===5In sending template email template is "+emailTemplate_onlyForSendTemplateEmail);

//        CompletionStage<WSResponse> res2 = ws.url("http://localhost:9000/email/"+receiver_name).get();
//        res2.thenAccept(response -> {
//            JsonNode ret = response.asJson();
//            if ("successful".equals(ret.get("status").asText())) {
//                String email = ret.get("email").asText();
//                System.out.println("===6In sending template email "+email);
//                System.out.println("===7In sending template template is "+ emailTemplate_onlyForSendTemplateEmail);
//
//                SendEmail(email, emailTemplate_onlyForSendTemplateEmail);
//                //return ok(views.html.emailTemplate.render(EmailTemplateForm, ));
//            }else{
//                //return GO_HOME;
//            }
//        });
    }

    public Result SendReviewerReminder()
    {
        //Session session = Http.Context.current().session();
        Http.Session session = Http.Context.current().session();
        String pcchair_name = session.get("username");
        String conf = session.get("conferenceinfo");
        int role = Integer.parseInt(session.get("role"));

        JsonNode json = Json.newObject()
                .put("chair_name", pcchair_name)
                .put("conference",conf);
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/reviewerreminder").post(json);
        return ok(views.html.admin.render(conf, role));
    }

    public CompletionStage<Result> SendAuthorReminder()
    {
        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
//        JsonNode json = Json.newObject()
//                .put("username", username);

        String confurl = conferenceinfo.replaceAll(" ","+");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/allpaper/conference/" + confurl).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is " + response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for (JsonNode res1 : ret) {
                Paper savedPaper = new Paper();
                savedPaper.email1 = res1.get("email1").asText();
                System.out.println("1333====="+savedPaper.email1);
                SendAuthorEmail(savedPaper.email1);
                savedPaper.email2 = res1.get("email2").asText();
                SendAuthorEmail(savedPaper.email2);
                savedPaper.email2 = res1.get("email3").asText();
                SendAuthorEmail(savedPaper.email3);
                savedPaper.email2 = res1.get("email4").asText();
                SendAuthorEmail(savedPaper.email4);
                savedPaper.email2 = res1.get("email5").asText();
                SendAuthorEmail(savedPaper.email5);
                savedPaper.email2 = res1.get("email6").asText();
                SendAuthorEmail(savedPaper.email6);
                savedPaper.email2 = res1.get("email7").asText();
                SendAuthorEmail(savedPaper.email7);


            }
            return ok(views.html.admin.render(conferenceinfo, 3));
        });
    }

    private static void SendAuthorEmail(String emailto){
        try {
            if(emailto!=null && !emailto.equals("null") && !emailto.equals("")) {
                Email email = new SimpleEmail();
                email.setHostName("smtp.googlemail.com");
                email.setSmtpPort(465);
                email.setAuthenticator(new DefaultAuthenticator("socandrew2017@gmail.com", "ling0915"));
                email.setSSLOnConnect(true);
                email.setFrom("socandrew2017@gmail.com");
                email.setSubject("YOUR PAPER IS SUBMITTED");
                email.setMsg("hello~ your paper is submitted successfully!");
                email.addTo(emailto);
                email.send();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void SendEmail(String emailto, String pwd){
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("socandrew2017@gmail.com", "ling0915"));
            email.setSSLOnConnect(true);
            email.setFrom("socandrew2017@gmail.com");
            email.setSubject("Temporary password");
            email.setMsg(pwd);
            email.addTo(emailto);
            email.send();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}