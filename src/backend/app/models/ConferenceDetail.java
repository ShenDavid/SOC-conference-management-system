package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;
@Entity
public class ConferenceDetail extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;

    public String title;

    //conference details - configure system
    public String name;
    public String url;
    public String conference_email;
    public String chair_email;
    public String tag_title;
    public String config_content;
    public boolean canPDF;
    public boolean canPostscript;
    public boolean canWord;
    public boolean canZip;
    public String canMultitopics;
    public String isOpenAbstract;
    public String isOpenPaper;
    public String isOpenCamera;
    public String isBlindReview;
    public String discussMode;
    public String ballotMode;
    public String reviewer_number;
    public String isMailAbstract;
    public String isMailUpload;
    public String isMailReviewSubmission;
    public String phase;

    public static Find<Long,ConferenceDetail> find = new Find<Long,ConferenceDetail>(){};

    public static Long GetConferenceInfo(String conf_title)
    {
        List<ConferenceDetail> results =
                find.where()
                        .eq("title",conf_title)
                        .findList();
        System.out.println("in backend models title is "+conf_title+" find "+results.size());
        if(results.size()==0)
            return Long.valueOf(0);
        else
            return results.get(0).id;
    }

    public static String updateInfo(ConferenceDetail newconferenceData)
    {
        List<ConferenceDetail> results =
                find.where()
                        .eq("title",newconferenceData.title)
                        .findList();
        Long id =  results.get(0).id;
        ConferenceDetail new_Conference = ConferenceDetail.find.byId(id);

        new_Conference.title = newconferenceData.title;
        new_Conference.name = newconferenceData.name;
        new_Conference.url = newconferenceData.url;
        new_Conference.conference_email = newconferenceData.conference_email;
        new_Conference.chair_email = newconferenceData.chair_email;
        new_Conference.tag_title = newconferenceData.tag_title;
        new_Conference.config_content = newconferenceData.config_content;
        new_Conference.canPDF = newconferenceData.canPDF;
        new_Conference.canPostscript = newconferenceData.canPostscript;
        new_Conference.canWord = newconferenceData.canWord;
        new_Conference.canZip = newconferenceData.canZip;
        new_Conference.canMultitopics = newconferenceData.canMultitopics;
        new_Conference.isOpenAbstract = newconferenceData.isOpenAbstract;
        new_Conference.isOpenPaper = newconferenceData.isOpenPaper;
        new_Conference.isOpenCamera = newconferenceData.isOpenCamera;
        new_Conference.isBlindReview = newconferenceData.isBlindReview;
        new_Conference.discussMode = newconferenceData.discussMode;
        new_Conference.ballotMode = newconferenceData.ballotMode;
        new_Conference.reviewer_number = newconferenceData.reviewer_number;
        new_Conference.isMailAbstract = newconferenceData.isMailAbstract;
        new_Conference.isMailUpload = newconferenceData.isMailUpload;
        new_Conference.isMailReviewSubmission = newconferenceData.isMailReviewSubmission;

        new_Conference.update();

        return newconferenceData.title;
    }
    public static boolean ifInfoExist(String conf_id)
    {
        List<ConferenceDetail> results =
                find.where()
                        .eq("title",conf_id)
                        .findList();
        if(results.size()!=0)
            return true;
        else
            return false;
    }
}