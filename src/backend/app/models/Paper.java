package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

/**
 * Created by shuang on 3/28/17.
 */

@Entity
public class Paper extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public long id;
    public String username;
    //    @Constraints.Required
    public String title;
    //    @Constraints.Required
    public String contactemail;
    public String authors;
    //    @Constraints.Required
    public String confirmemail;
    //    @Constraints.Required
    public String firstname1;
    //    @Constraints.Required
    public String lastname1;
    //    @Constraints.Required
    public String email1;
    //    @Constraints.Required
    public String affilation1;
    public String firstname2;
    public String lastname2;
    public String email2;
    public String affilation2;
    public String firstname3;
    public String lastname3;
    public String email3;
    public String affilation3;
    public String firstname4;
    public String lastname4;
    public String email4;
    public String affilation4;
    public String firstname5;
    public String lastname5;
    public String email5;
    public String affilation5;
    public String firstname6;
    public String lastname6;
    public String email6;
    public String affilation6;
    public String firstname7;
    public String lastname7;
    public String email7;
    public String affilation7;
    public String otherauthor;
    public String candidate;
    public String volunteer;
    //    @Constraints.Required
    public String paperabstract;
    public String ifsubmit;
    public String format;
    public String papersize;
    public String file;
    public String conference;
    public String topic;
    public String status;


    public String reviewstatus;
    public Long reviewerid;
    public String review;

    public String date;

    public static Find<Long,Paper> find = new Find<Long,Paper>(){};

    public static List<Paper> GetMyPaper(String username){
        List<Paper> results =
                find.where()
                        .eq("username",username)
                        .findList();
        return results;
    }

    public static List<Paper> ReviewPapers(Long userid){
        List<Paper> results =
                find.where()
                        .eq("reviewerid",userid)
                        .findList();
        return results;
    }

    public static List<Paper> ConfPapers(String conf){
        List<Paper> results =
                find.where()
                        .eq("conference",conf)
                        .findList();
        return results;
    }
    public static List<Paper> ConfUserPapers(String conf,String username){
        List<Paper> results =
                find.where()
                        .eq("conference",conf)
                        .eq("username",username)
                        .findList();
        return results;
    }
}
