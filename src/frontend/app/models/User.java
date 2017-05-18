package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import com.avaje.ebean.Expr;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Ling on 2017/3/27.
 */
@Entity
public class User extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;

    public String username;

    public String password;

    public String email;

    public String security_question1;

    public String security_answer1;

    public String security_question2;

    public String security_answer2;

    public String privilege;

    public static Find<Long,User> find = new Find<Long,User>(){};

    private static HashMap<String, String> temporary_pwds = new HashMap<String, String>();



    public static List<String> questions = Arrays.asList("What is the lastname of your best friend?", "What is your favourite color?", "What is the name of your pet?", "What is your dream in childhood?", "Where did you parents meet?");

    public static void AddTemporaryPwd(String username, String pwd){
        temporary_pwds.put(username, pwd);
        System.out.println("User "+username+" tmp pwd "+pwd+" add");
    }

    public static boolean IfTemporaryPwdCorrect(String username, String pwd){
        if(temporary_pwds.containsKey(username)){
            if(temporary_pwds.get(username).equals(pwd)) {
                System.out.println("User "+username+" tmp pwd "+pwd+" equals");
                temporary_pwds.remove(username);
                return true;
            }
            System.out.println("User "+username+" tmp pwd "+pwd+" and "+temporary_pwds.get(username)+" not equal");
            temporary_pwds.remove(username);
        }
        return false;
    }

}
