package org.example.zarp_back.service.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class NotificacionService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private ClienteRepository clienteRepository;

    //TODO: AGREGAR NOTIFICACIONES PARA VERIFICACION DOCUMENTO Y PARA ACTIVACION DE PROPIEDADES

    public void notificarReservaCliente(Reserva reserva) throws MessagingException {
        String para = reserva.getCliente().getCorreoElectronico();
        String asunto = "Confirmación de Reserva";
        String cuerpo="Adjunto encontrará la confirmación de su reserva.";
        String nombrePdf="reserva_cliente.pdf";

        byte[] pdf = generarPdfReservaCliente(reserva);
        emailService.enviarMailConAdjunto(para, asunto, cuerpo, pdf, nombrePdf);
    }

    public void notificarReservaPropietario(Reserva reserva) throws MessagingException {
        String para = reserva.getPropiedad().getPropietario().getCorreoElectronico();
        String asunto = "Nueva Reserva en su Propiedad";
        String cuerpo = "Adjunto encontrará los detalles de la nueva reserva en su propiedad.";
        String nombrePdf="reserva_propietario.pdf";
        byte[] pdf = generarPdfReservaPropietario(reserva);
        emailService.enviarMailConAdjunto(para, asunto, cuerpo, pdf, nombrePdf);
    }
    private byte[] generarPdfReservaCliente(Reserva reserva) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Logo
            InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");
            if (logoStream != null) {
                byte[] logoBytes = logoStream.readAllBytes();
                Image logo = Image.getInstance(logoBytes);
                logo.scaleToFit(100, 100);
                logo.setAlignment(Image.ALIGN_CENTER);
                document.add(logo);
            }

            // Contenido
            document.add(new Paragraph("Confirmación de Reserva"));
            document.add(new Paragraph("Estimado/a " + reserva.getCliente().getNombreCompleto() + ","));
            document.add(new Paragraph("Su reserva ha sido confirmada:"));
            document.add(new Paragraph("Fecha Inicio: " + reserva.getFechaInicio()));
            document.add(new Paragraph("Fecha Fin: " + reserva.getFechaFin()));
            document.add(new Paragraph("\nGracias por elegirnos.\nZarp Team"));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de reserva para cliente", e);
        }
    }
    private byte[] generarPdfReservaPropietario(Reserva reserva) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Logo
            InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");
            if (logoStream != null) {
                byte[] logoBytes = logoStream.readAllBytes();
                Image logo = Image.getInstance(logoBytes);
                logo.scaleToFit(100, 100);
                logo.setAlignment(Image.ALIGN_CENTER);
                document.add(logo);
            }

            // Contenido
            document.add(new Paragraph("Nueva Reserva en su Propiedad"));
            document.add(new Paragraph("Estimado/a " + reserva.getPropiedad().getPropietario().getNombreCompleto() + ","));
            document.add(new Paragraph("Se ha realizado una nueva reserva:"));
            document.add(new Paragraph("Fecha Inicio: " + reserva.getFechaInicio()));
            document.add(new Paragraph("Fecha Fin: " + reserva.getFechaFin()));
            document.add(new Paragraph("Cliente: " + reserva.getCliente().getNombreCompleto()));
            document.add(new Paragraph("Monto: $" + reserva.getPrecioTotal()));
            document.add(new Paragraph("\nGracias por confiar en nosotros.\nZarp Team"));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de reserva para propietario", e);
        }
    }



}
