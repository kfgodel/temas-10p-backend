package convention.rest.api.tos;

import org.h2.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.QueryParam;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * This type represents the answer form the backoffice as the user information for authenticated users
 * Created by kfgodel on 14/01/17.
 */
public class BackofficeUserTo {

  @QueryParam("uid")
  public String uid;

  @QueryParam("email")
  public String email;

  @QueryParam("username")
  public String username;

  @QueryParam("full_name")
  public String fullName;

  @QueryParam("root")
  public String root;

  @QueryParam("denied")
  public String denied;

  @QueryParam("hmac")
  public String hmac;

  public Boolean isRoot() {
    return root.equals("true");
  }

  public Boolean hasGrantedAccess() {
    return StringUtils.isNullOrEmpty(denied);
  }

  public Boolean isValid() {
    // Esto no debería estar hardcodeado. Debería venir del ambiente.
    String key = "CHANGE_ME";
    String algorithm = "HmacSHA256";
    String data =
      "uid=" + uid + "&email=" + email + "&username=" + username + "&full_name=" + fullName + "&root=" + root;
    Mac mac;
    try {
      mac = Mac.getInstance(algorithm);
      mac.init(new SecretKeySpec(key.getBytes(), algorithm));
      byte[] generatedHmacBytes = mac.doFinal(data.getBytes("UTF-8"));
      StringBuilder sb = new StringBuilder();
      for (byte b : generatedHmacBytes) {
        sb.append(String.format("%02x", b));
      }
      String generatedHmac = sb.toString();
      return hmac.equals(generatedHmac);
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
      return false;
    }
  }

  public static BackofficeUserTo create(String uid, String email, String username, String fullName, String root, String denied, String hmac) {
    BackofficeUserTo backofficeUserTo = new BackofficeUserTo();
    backofficeUserTo.uid = uid;
    backofficeUserTo.username = username;
    backofficeUserTo.fullName = fullName;
    backofficeUserTo.root = root;
    backofficeUserTo.denied = denied;
    backofficeUserTo.email = email;
    backofficeUserTo.hmac = hmac;
    return backofficeUserTo;
  }

}
