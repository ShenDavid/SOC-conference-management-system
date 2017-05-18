package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;


/**
 * Created by sxh on 17/3/26.
 */
@Entity
public class Profile extends Model{
    public static List<String> titles = Arrays.asList(" ", "Mr.", "Ms.", "Dr.");

    private static final long serialVersionUID = 1L;

    @Id
    public Long userid;

    @ManyToOne
    public String title;

    public String research;

    public String firstname;

    public String lastname;

    public String position;

    public String affiliation;

    public String email;

    public String phone;

    public String fax;

    public String address;

    public String city;

    public String country;

    public String region;

    public Long zipcode;

    public String comment;

    /**
     * Generic query helper for entity Profile with id Long
     */
    public static Find<Long,Profile> find = new Find<Long,Profile>(){};

}
