package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

/**
 * Created by sxh on 4/26/17.
 */

@Entity
public class Review extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;
    public Long paperid;
    public Long reviewerid;
    public String iscriteria;
    public String label;
    public String review_content;
    public String reviewstatus;

    public static Find<Long,Review> find = new Find<Long,Review>(){};
}