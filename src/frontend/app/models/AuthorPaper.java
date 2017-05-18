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

public class AuthorPaper extends Model{

    public String author;

    public String affiliation;

    public List<Long> papers;



}
