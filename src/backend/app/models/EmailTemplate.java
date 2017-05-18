package models;
import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

@Entity
public class EmailTemplate extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;

    public String chair_name;

    public String conference;

    public String email_type;

    public String subject;

    @Column(length=10000)
    public String template;

    public static Find<Long,EmailTemplate> find = new Find<Long,EmailTemplate>(){};

    public static final String AC_Template = "Dear {PAPER_AUTHORS},\\n\\nWe are happy to inform you that your above paper has been accepted for publication by {CONF_ACRONYM} at IEEE Congress on Services (SERVICES 2009-I). Your paper will be included in the IEEE Proceedings of Congress on Services (SERVICES 2009-I) (EI indexed) and will become available in IEEE digital library. SSMM is part of First International Conference on Cloud Computing (CLOUD 2009).\\n\\nBest regards,\\n\\n{CONF_ACRONYM} Chairs";

    public static final String REJ_Template = "Dear  {PAPER_AUTHORS}, \\nWe are sorry to inform you that your above paper is not accepted for publication by {CONF_ACRONYM} Track.\\nBelow, you will find attached the reports from the PC members. Our PC was tough, and you may find that some of the attached reviews are critical of your paper. We hope that you will accept these criticisms in the constructive sense that was intended and that they will help you improve your research in the future.\\nICWS/SERVICES-I 2009 will be held at Marriott Los Angeles Airport. Thanks a lot for your contribution and support of ICWS 2009!\\n\\nBest regards,\\n\\n{CONF_ACRONYM} Chairs";

    public static final String MOV_Template = "Dear {PAPER_AUTHORS},\\n\\nAll ICWS and SERVICES-I tracks and workshops have finished the review process. We are sorry to inform you that your paper was not selected to be included in ICWS or SERVICES-I tracks or workshops. Please enhance your paper based on pc members' review comments. We look forward to your contribution to ICWS 2010. \\nICWS 2009 Program Committee Chairs";

    public static final String INSTRUCTION_Template = "Dear {NAME_REVIEWER},\\nTime has come for the paper review process of the {CONF_ACRONYM} conference. So far {NUMBER_OF_PAPERS} papers have been submitted. \\nHere are the instruction on downloading the papers that you have been assigned, as well as how to provide you reviews:\\n\\n1. - Your list of papers is available at http://www.soc.com \\n2. - PLEASE CHECK that you can properly download and print each assigned paper. \\n3. - Each paper in your list comes with a 'Submit my review' link. Use it to access your review form. You can return latter and modify you review if needed. \\nIf we have assigned a paper to you which you feel is a conflict of interest, please let us know right away and we will correct the problem. \\nRecall that the deadline for submitting your review is the {REVIEW_DEADLINE} \nPlease don't be in late, since we must provide sufficient time for discussion between the reviewers in order to resolve any significant differences between the reviews.\\n\\n{CONF_ACRONYM} chairs";

    public static final String INVITATION_Template = "Dear {FIRST_NAME} {LAST_NAME},\\nWe are proud to host {CONFERENCE_NAME} at {CONFERENCE_WHERE} ON {CONFERENCE_WHEN}. \\nWe are cordially inviting you to review our papers collected online. \\nIf you accept our invitation, could you please click the following link to confirm it? {WWW_CONFHUB_COM_CONFIRM_LINK} \\nIf the link does not work, could you please paste it to a browser to handle it?  \\nThanks for your time. \\nBest Regards \\n{CONFERENCE_ACRONYM} Chairs";

    public static final String REMINDER_Template = "Dear {FIRST_NAME} {LAST_NAME},\\n\\nThis is to remind you that you still have {NUMBER} papers to review. \\nCould you please login to http://www.confhub.com to handle them? Thanks!\\n\\mBest Regards\\n\\n{CONFERENCE_ACRONYM} Chairs";

    public static final String AC_Subject = "Paper Accepted";

    public static final String REJ_Subject = "Paper Rejected";

    public static final String MOV_Subject = "Paper Moved";

    public static final String INSTRUCTION_Subject = "Paper Instruction";

    public static final String INVITATION_Subject = "Paper Invitation";

    public static final String REMINDER_Subject = "Paper Reminder";

    public static final String AC_EmailType = "AC_PAPER";

    public static final String REJ_EmailType = "REJ_PAPER";

    public static final String MOV_EmailType = "MOV_PAPER";

    public static final String INSTRUCTION_EmailType = "INSTRUCTION";

    public static final String INVITATION_EmailType = "INVITATION";

    public static final String REMINDER_EmailType = "REVIEWER_REMINDER";

    public static String getEmailTemplateByType(String email_type, String chair_name, String conf)
    {
        List<EmailTemplate> results =
                find.where()
                        .conjunction()
                        .eq("chair_name", chair_name)
                        .eq("email_type", email_type)
                        .eq("conference",conf)
                        .endJunction()
                        .findList();
        if(results.size()==0)return "";
        else
        return results.get(0).template;
    }

    public static String getEmailSubjectByType(String email_type, String chair_name, String conf)
    {
        List<EmailTemplate> results =
                find.where()
                        .conjunction()
                        .eq("chair_name", chair_name)
                        .eq("email_type", email_type)
                        .eq("conference",conf)
                        .endJunction()
                        .findList();
        if(results.size()==0)return "";
        else
            return results.get(0).subject;
    }

    public static boolean IfExist(String chair_name, String conf)
    {
        List<EmailTemplate> results =
                find.where()
                        .and(Expr.eq("chair_name", chair_name), Expr.eq("conference", conf))
                        .findList();
        if(results.size() == 0)
            return false;
        else
            return true;
    }

    public static void updateEmailTemplate(EmailTemplate new_template)
    {
        List<EmailTemplate> results =
                find.where()
                        .conjunction()
                        .eq("chair_name", new_template.chair_name)
                        .eq("email_type", new_template.email_type)
                        .eq("conference",new_template.conference)
                        .endJunction()
                        .findList();
        Long id =  results.get(0).id;
        EmailTemplate new_email = EmailTemplate.find.byId(id);
        new_email.template = new_template.template;
        new_email.subject = new_template.subject;
        new_email.update();
    }

    public static void createChairTemplate(String chair_name, String conf)
    {
        EmailTemplate new_one = new EmailTemplate();

        new_one.chair_name = chair_name;
        new_one.conference = conf;

        //create accepted paper email template
        System.out.println("3=====start inserting ac template");
        new_one.email_type = AC_EmailType;
        new_one.subject = AC_Subject;
        new_one.template = AC_Template;
        new_one.save();
        System.out.println("4=====finish inserting ac template");

        //create reject paper email template
        EmailTemplate new_one2 = new EmailTemplate();
        new_one2.chair_name = chair_name;
        new_one2.conference = conf;
        new_one2.email_type = REJ_EmailType;
        new_one2.subject = REJ_Subject;
        new_one2.template = REJ_Template;
        new_one2.save();

        //create move paper email template
        EmailTemplate new_one3 = new EmailTemplate();
        new_one3.chair_name = chair_name;
        new_one3.conference = conf;
        new_one3.email_type = MOV_EmailType;
        new_one3.subject = MOV_Subject;
        new_one3.template = MOV_Template;
        new_one3.save();

        //create instruction email template
        EmailTemplate new_one4 = new EmailTemplate();
        new_one4.chair_name = chair_name;
        new_one4.conference = conf;
        new_one4.email_type = INSTRUCTION_EmailType;
        new_one4.subject = INSTRUCTION_Subject;
        new_one4.template = INSTRUCTION_Template;
        new_one4.save();

        //create invitation email template
        EmailTemplate new_one5 = new EmailTemplate();
        new_one5.chair_name = chair_name;
        new_one5.conference = conf;
        new_one5.email_type = INVITATION_EmailType;
        new_one5.subject = INVITATION_Subject;
        new_one5.template = INVITATION_Template;
        new_one5.save();

        //create reminder template
        EmailTemplate new_one6 = new EmailTemplate();
        new_one6.chair_name = chair_name;
        new_one6.conference = conf;
        new_one6.email_type = REMINDER_EmailType;
        new_one6.subject = REMINDER_Subject;
        new_one6.template = REMINDER_Template;
        new_one6.save();
    }
}