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

    public static boolean IfQACorrect(String username, String question, String answer){
        List<User> results =
                find.where()
                        .eq("username", username)
                        .findList();
        if(results.isEmpty()){
            return false;
        }else{
            String expected_q1 = results.get(0).security_question1;
            String expected_q2 = results.get(0).security_question2;
            String expected_a1 = results.get(0).security_answer1;
            String expected_a2 = results.get(0).security_answer2;

            if( (expected_q1.equals(question) && expected_a1.equals(answer) || (expected_q2.equals(question) && expected_a2.equals(answer))) ){
                return true;
            }
        }
        return false;
    }

    public static boolean VerifyUser(String username, String password) {

        try {
            String encry_password = MD5(password);
            List<User> results =
                    find.where()
                            .and(Expr.eq("username", username), Expr.eq("password", encry_password))
                            .findList();
            if(results.isEmpty()) return false;
            else return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private static String MD5(String password) throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(StandardCharsets.UTF_8.encode(password));
        return String.format("%032x", new BigInteger(1, md.digest()));
    }

    public static boolean IfUserExist(String username) {
        List<User> results =
                find.where()
                        .eq("username", username)
                        .findList();
        if(results.isEmpty()) return false;
        else return true;
    }

    public static Long GetUserID(String username) {
        List<User> results =
                find.where()
                        .eq("username", username)
                        .findList();
        return results.get(0).id;
    }

    public static String GetUserPrivilege(String username){
        List<User> results =
                find.where()
                        .eq("username", username)
                        .findList();
        return results.get(0).privilege;
    }

    public static String GetEmailByUsername(String username){
        List<User> results =
                find.where()
                        .eq("username", username)
                        .findList();
        return results.get(0).email;
    }

    public static String GetUsernameByEmail(String email){
        List<User> results =
                find.where()
                        .eq("email", email)
                        .findList();
        if(results.size()==0)
            return "error";
        else
            return results.get(0).username;
    }
}
