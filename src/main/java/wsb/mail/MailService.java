package wsb.mail;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailService {

    final private JavaMailSender javaMailSender;
    final private MailConfig mailConfig;

    void sendMail(Mail mail) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(mail.recipient);
            mimeMessageHelper.setSubject(mail.subject);
            mimeMessageHelper.setText(mail.content);
            mimeMessageHelper.addAttachment(mail.attachment.getOriginalFilename(), mail.attachment);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println("Wysyłanie mejla nie powiodło się!");
        }
    }

    Message[] receiveMails() throws MessagingException {
        Properties props = mailConfig.getMailConfig();
        Authenticator auth = mailConfig.getAuthenticator();

        Session session = Session.getDefaultInstance(props, auth);

        Store store = session.getStore(mailConfig.getUserProtocol());
        store.connect(mailConfig.getHost(), mailConfig.getEmail(), mailConfig.getPassword());

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();

        // TODO zmapować message na mail

        inbox.close(false);
        store.close();

        return messages;
    }

}
