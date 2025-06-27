package nova.hackathon.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nova.hackathon.util.mail.EmailDTO;
import nova.hackathon.util.mail.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<EmailDTO.Response> sendMail(@RequestBody @Valid EmailDTO.Request request) {
        EmailDTO.Response response = mailService.send(request);
        return ResponseEntity.ok(response);
    }
}
