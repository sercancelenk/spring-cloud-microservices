package byzas.io.microservice.authservice.extensions;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public interface CryptoSupport {
    default String encrypt(String... keys) {
        try {
            String secret = "secret";
            String message = StringUtils.join(keys, ":");

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            return Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));

        } catch (Exception e) {
            System.out.println("Error");
            return "";
        }
    }
}
