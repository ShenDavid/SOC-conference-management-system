package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

@Entity
public class StatusCode extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;
    public String label;
    public String mailtemplate;
    public String camerareadyrequired;
    public String conferenceinfo;

    public static Finder<Long,StatusCode> find = new Finder<Long,StatusCode>(StatusCode.class);

    public static List<StatusCode> GetMyStatusCode(Long statusId){
        List<StatusCode> results =
                find.where()
                        .eq("id",statusId)
                        .findList();
        return results;
    }

    public static List<StatusCode> GetMyConferenceStatusCode(String conferenceinfo){
        List<StatusCode> results =
                find.where()
                        .eq("conferenceinfo",conferenceinfo)
                        .findList();
        return results;
    }

}