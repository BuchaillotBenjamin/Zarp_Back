package org.example.zarp_back.service.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarMail(String para, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mailSender.send(mensaje);
    }

    public void enviarMailConAdjunto(String para, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
        helper.setTo(para);
        helper.setSubject(asunto);
        helper.setText(cuerpo);
        helper.addAttachment(nombreAdjunto, new ByteArrayResource(adjunto));
        mailSender.send(mensaje);
    }

    public void enviarMailConDosAdjuntos(
            String para,
            String asunto,
            String cuerpo,
            byte[] adjunto1,
            String nombreAdjunto1,
            byte[] adjunto2,
            String nombreAdjunto2
    ) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
        helper.setTo(para);
        helper.setSubject(asunto);
        helper.setText(cuerpo);

        // Primer PDF
        helper.addAttachment(nombreAdjunto1, new ByteArrayResource(adjunto1));

        // Segundo PDF
        helper.addAttachment(nombreAdjunto2, new ByteArrayResource(adjunto2));

        mailSender.send(mensaje);
    }

}
