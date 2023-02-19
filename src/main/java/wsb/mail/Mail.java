package wsb.mail;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class Mail {

    String recipient;

    String subject;

    String content;

    MultipartFile attachment;

    public Mail(String from, String subject) {
        this.recipient = from;
        this.subject = subject;
    }

}
