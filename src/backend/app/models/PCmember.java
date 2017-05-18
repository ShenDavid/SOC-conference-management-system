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

    public String email;

    public String conference;

    public String firstname;

    public String lastname;

    public String affiliation;

    public String phone;

    public String address;

    //public String username;

    public String ifChair;

    public String ifReviewer;

    public static Find<Long,PCmember> find = new Find<Long,PCmember>(){};

    public static List<PCmember> GetAllmember(String conf)
    {
        List<PCmember> results =
                find.where()
                        .eq("conference",conf)
                        .findList();
        return results;
    }

    public static List<PCmember> GetAllReviewer(String conf)
    {
        List<PCmember> results =
                find.where()
                        .and(Expr.eq("if_reviewer", "Y"), Expr.eq("conference", conf))
                        .findList();
        return results;
    }

    public static boolean IfEXIST(String email, String conf)
    {
        List<PCmember> results =
                find.where()
                        .and(Expr.eq("email", email), Expr.eq("conference", conf))
                        .findList();
        if(results.size()>0)
            return true;
        else
            return false;
    }

    public static void updatemember(PCmember new_member)
    {
        List<PCmember> results =
                find.where()
                        .and(Expr.eq("email", new_member.email), Expr.eq("conference", new_member.conference))
                        .findList();
        Long id = results.get(0).id;
        PCmember member = PCmember.find.byId(id);

        member.email = new_member.email;
        member.address = new_member.address;
        member.affiliation = new_member.affiliation;
        member.firstname = new_member.firstname;
        member.lastname = new_member.lastname;
        member.ifChair = new_member.ifChair;
        member.ifReviewer =new_member.ifReviewer;
        member.phone = new_member.phone;

        member.update();
    }

    public static void createmember(PCmember new_member)
    {
        PCmember member = new PCmember();

        member.email = new_member.email;
        member.address = new_member.address;
        member.affiliation = new_member.affiliation;
        member.firstname = new_member.firstname;
        member.lastname = new_member.lastname;
        member.ifChair = new_member.ifChair;
        member.ifReviewer =new_member.ifReviewer;
        member.phone = new_member.phone;
        member.conference = new_member.conference;

        member.save();
    }

    public static Long GetmemberID(String email, String conf)
    {
        List<PCmember> results =
                find.where()
                        .and(Expr.eq("email", email), Expr.eq("conference", conf))
                        .findList();
        return results.get(0).id;
    }
}