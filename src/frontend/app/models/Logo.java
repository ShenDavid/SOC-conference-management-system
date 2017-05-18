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
public class Logo extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public long id;
    public String file;

}
