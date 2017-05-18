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
/*
 * a pc member may not be a user of this system
 */
@Entity
public class PCmember extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;
    @Constraints.Required
    public String email;

    public String conference;
    @Constraints.Required
    public String firstname;
    @Constraints.Required
    public String lastname;

    public String affiliation;

    public String phone;

    public String address;

    //public String username;

    public String ifChair;

    public String ifReviewer;
}