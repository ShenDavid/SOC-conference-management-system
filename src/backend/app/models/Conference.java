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
public class Conference extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;
    public String username;
//    @Constraints.Required
    public String title;//conference acronym
//    @Constraints.Required
    public String location;
    public String date;
    public String status;
    public String searchstatus;
    public String ifreviewer;
    public String ifadmin;
    public String ifchair;
    public String keyword;


    public static Find<Long,Conference> find = new Find<Long,Conference>(){};

    public static List<Conference> GetMyConference(String username){
        List<Conference> results =
                find.where()
                        .eq("username",username)
                        .findList();
        return results;
    }

    public static void updateIfReviewer(String username, String conf, String ifreviewer, String ifchair)
    {
        List<Conference> results =
                find.where()
                        .and(Expr.eq("username", username), Expr.eq("title", conf))
                        .findList();
        if(results.size()>0) {
            Long new_id = results.get(0).id;

            Conference new_conf = Conference.find.byId(new_id);

            System.out.println("3=====add member before update if reviewer"+new_conf.ifreviewer+" if chair "+new_conf.ifchair);
            new_conf.ifreviewer = ifreviewer;
            new_conf.ifchair = ifchair;
            System.out.println("4=====add member after update if reviewer"+new_conf.ifreviewer+" if chair "+new_conf.ifchair);

            new_conf.update();
        }else{
            Conference new_conf = new Conference();

            List<Conference> results2 =
                    find.where()
                            .eq("title", conf)
                            .findList();
            String loc = "";
            String date = "";
            String status = "";
            if(results2.size()>0){
                loc = results2.get(0).location;
                date = results2.get(0).date;
                status = results2.get(0).status;
            }

            new_conf.username = username;
            new_conf.title = conf;
            new_conf.location = loc;
            new_conf.status = status;
            new_conf.date = date;
            new_conf.ifreviewer = ifreviewer;
            new_conf.ifchair = ifchair;
            new_conf.save();
        }
    }

    public static void deleteReviewer(String username, String conf){
        List<Conference> results =
                find.where()
                        .and(Expr.eq("username", username), Expr.eq("title", conf))
                        .findList();
        Long id = results.get(0).id;
        Conference.find.ref(id).delete();
    }

    /* Get the privilege of the user of certain conference
     * @return: (ifadmin,ifchair)->(1/0,1/0)
     * (isadmin,notchair)->(1,0), (isadmin,ischair)->(1,1), (notadmin, notchair)->(0,0), (notadmin, ischair)->(0,1)
     */
    public static int getrole(String username, String conf)
    {
        List<Conference> results =
                find.where()
                        .and(Expr.eq("username", username), Expr.eq("title", conf))
                        .findList();
        int admin_score = 2;
        int chair_score = 1;
        int score = 0;
        if(results.size()>0) {
            if ("Y".equals(results.get(0).ifadmin))
                score += admin_score;
            if ("Y".equals(results.get(0).ifchair))
                score += chair_score;
        }
        return score;
    }
}
