package wsb.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@Getter
public class MailConfig {

    @Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    private final String userProtocol = "imap";
    private final String host = "imap.gmail.com";

    Properties getMailConfig() {
        Properties properties = new Properties();

        properties.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.imap.socketFactory.fallback", "false");
        properties.put("mail.imap.socketFactory.port", "993");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.user", email);
        properties.put("mail.imap.host", host);
        properties.put("mail.store.protocol", userProtocol);
        properties.put("mail.imap.ssl.protocols", "TLSv1.2");

        return properties;
    }

    Authenticator getAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        };
    }
}
