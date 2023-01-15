package wsb.mail;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mail {

    String recipient;

    String subject;

    String content;

}
