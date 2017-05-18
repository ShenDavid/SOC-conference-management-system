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


}