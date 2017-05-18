package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

@Entity
public class Criteria extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public long id;
    public String label;
    public String explanations;
    public String weight;
    public String conferenceinfo;

    public static List<String> criteria_options = Arrays.asList("Strongly Accept", "Accept", "Weak Accept", "Neutral", "Strongly Reject", "Reject", "Weak Reject");

    public static Find<Long,Criteria> find = new Find<Long,Criteria>(){};

    public static List<Criteria> GetMyCriteria(Long criteriaId){
        List<Criteria> results =
                find.where()
                        .eq("id",criteriaId)
                        .findList();
        return results;
    }

    public static List<Criteria> GetMyConferenceCriteria(String conferenceinfo){
        List<Criteria> results =
                find.where()
                        .eq("conferenceinfo",conferenceinfo)
                        .findList();
        return results;
    }

}