package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;


/**
 * Created by sxh.
 */

public class ReviewCount extends Model{

    public String reviewerid;

    public String reviewed;

    public String left;

    public String email;

    public String name;

}
