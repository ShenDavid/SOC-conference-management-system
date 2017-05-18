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

public class ConfCount extends Model{

    public String conf;

    public String assigned;

    public String reviewed;

    public String left;



}
