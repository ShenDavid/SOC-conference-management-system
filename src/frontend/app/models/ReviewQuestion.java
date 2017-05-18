package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

@Entity
public class ReviewQuestion extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public long id;
    public String question;
    public String isPublic;
    public String conferenceinfo;
    public String listOfChoice1;
    public String position1;
    public String listOfChoice2;
    public String position2;
    public String listOfChoice3;
    public String position3;
    public String listOfChoice4;
    public String position4;
    public String listOfChoice5;
    public String position5;
    public String listOfChoice6;
    public String position6;
    public String listOfChoice7;
    public String position7;

    public static Find<Long,ReviewQuestion> find = new Find<Long,ReviewQuestion>(){};

    public static List<ReviewQuestion> GetMyQuestion(Long questionId){
        List<ReviewQuestion> results =
                find.where()
                        .eq("id",questionId)
                        .findList();
        return results;
    }

    public static List<ReviewQuestion> GetMyConferenceQuestion(String conferenceinfo){
        List<ReviewQuestion> results =
                find.where()
                        .eq("conferenceinfo",conferenceinfo)
                        .findList();
        return results;
    }
}