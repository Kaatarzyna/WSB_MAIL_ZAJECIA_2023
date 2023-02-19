package wsb.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/mails")
@RequiredArgsConstructor
public class MailController {

    final private MailService mailService;

    @GetMapping
    String showForm() {
        return "index";
    }

    @PostMapping
    String sendMail(@ModelAttribute Mail mail) {
        mailService.sendMail(mail);
        return "index";
    }

    @GetMapping("/inbox")
    ModelAndView receiveMails() throws MessagingException, IOException {
        List<Mail> mails = mailService.receiveMails();

        ModelAndView modelAndView = new ModelAndView("inbox");
        modelAndView.addObject("mails", mails);
        return modelAndView;
    }

}
