package mx.edu.utez.integradora.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // Método principal para enviar un correo
    public void sendVerificationEmail(String to, String verificationLink) {
        String subject = "Verifica tu correo electrónico";
        String body = generateVerificationEmailBody(verificationLink);

        sendEmail(to, subject, body);
    }

    // Genera el cuerpo del correo de verificación
    private String generateVerificationEmailBody(String verificationLink) {
        return String.format(
                "¡Gracias por registrarte en nuestra plataforma!\n\n" +
                        "Por favor, haz clic en el siguiente enlace para verificar tu cuenta y activarla:\n\n" +
                        "%s\n\n" +
                        "Si no has solicitado este registro, por favor ignora este mensaje.\n\n" +
                        "¡Esperamos verte pronto!",
                verificationLink
        );
    }

    // Método para enviar el correo electrónico
    private void sendEmail(String recipient, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
