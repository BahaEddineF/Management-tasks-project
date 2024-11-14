package com.alibou.security.emailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public ResponseEntity<Object> sendWelcomeEmail(String recipientEmail, String firstName, String lastName, String defaultPassword) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                ".container { background-color: white; padding: 20px; border-radius: 8px; max-width: 600px; margin: auto; box-shadow: 0px 0px 15px rgba(0,0,0,0.1); }" +
                "h1 { color: #4CAF50; }" +
                "p { font-size: 16px; color: #333; }" +
                ".user-info { font-size: 18px; margin: 10px 0; }" +
                ".password { font-size: 20px; font-weight: bold; color: #333; background-color: #e7f3e7; padding: 10px; border-radius: 4px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Welcome to Our Service, " + firstName + " " + lastName + "!</h1>" +
                "<p>We are excited to have you on board.</p>" +
                "<p>Below are your login details:</p>" +
                "<p class='user-info'><strong>Email:</strong> " + recipientEmail + "</p>" +
                "<p class='user-info'><strong>Temporary Password:</strong> <span class='password'>" + defaultPassword + "</span></p>" +
                "<p>Please use this temporary password to log in and make sure to update it as soon as possible.</p>" +
                "<p>If you did not request this, please contact our support team immediately.</p>" +
                "<br>" +
                "<p>Best Regards,</p>" +
                "<p>Your Company Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setTo(recipientEmail);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("Welcome to Our Service!");
        helper.setText(htmlContent, true); // 'true' indicates HTML content

        mailSender.send(message);
        return ResponseEntity.ok().body("{\"message\": \"Welcome email sent to your email.\"}");
    }

    public ResponseEntity<Object> sendVerificationEmail(String recipientEmail, String verificationCode) throws MessagingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                ".container { background-color: white; padding: 20px; border-radius: 8px; max-width: 600px; margin: auto; box-shadow: 0px 0px 15px rgba(0,0,0,0.1); }" +
                "h1 { color: #4CAF50; }" +
                "p { font-size: 16px; color: #333; }" +
                ".verification-code { font-size: 24px; font-weight: bold; color: #333; background-color: #e7f3e7; padding: 10px; border-radius: 4px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Verify Your Email Address</h1>" +
                "<p>Hello,</p>" +
                "<p>Please use the following verification code to complete your login:</p>" +
                "<p class='verification-code'>" + verificationCode + "</p>" +
                "<p>If you did not request this code, please ignore this email.</p>" +
                "<br>" +
                "<p>Thanks,</p>" +
                "<p>Your Company</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setTo(recipientEmail);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("Email Verification");
        helper.setText(htmlContent, true); // 'true' indicates HTML content

        mailSender.send(message);
        return ResponseEntity.ok().body("{\"message\": \"Verification code sent to your email.\"}");

    }
}
