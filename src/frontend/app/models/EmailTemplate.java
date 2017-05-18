package models;
import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import javax.persistence.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import com.avaje.ebean.Expr;
@Entity
public class EmailTemplate extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;

    public String chair_name;

    public String email_type;

    public String subject;

    public String template;
}