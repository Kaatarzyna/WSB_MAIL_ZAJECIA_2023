package wsb.mail;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
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

    List<Mail> receiveMails() throws MessagingException, IOException {
        Properties props = mailConfig.getMailConfig();
        Authenticator auth = mailConfig.getAuthenticator();

        Session session = Session.getDefaultInstance(props, auth);

        Store store = session.getStore(mailConfig.getUserProtocol());
        store.connect(mailConfig.getHost(), mailConfig.getEmail(), mailConfig.getPassword());

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();
        List<Mail> receivedMails = new LinkedList<>();
        for (Message message : messages) {
            Mail mail = new Mail(Arrays.toString(message.getFrom()), message.getSubject(), getTextFromMimeMultipart((MimeMultipart) message.getContent()));
            receivedMails.add(mail);
        }

        inbox.close(false);
        store.close();

        return receivedMails;
    }

    String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }

        return result.toString();
    }

}
