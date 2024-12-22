package com.alibou.security.emailSender;

import com.alibou.security.project.Project;
import com.alibou.security.task.Task;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
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

    //region emailstoManager

    public void sendNewProjectNotificationEmail(String to, String name, Project project) throws MessagingException {
        if (to == null || name == null || project == null) {
            throw new IllegalArgumentException("Email parameters cannot be null.");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("project", project);
        String htmlContent = templateEngine.process("emails/manager/new-project", context);

        helper.setTo(to);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("New Project Assigned: " + project.getTitle());
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendUpdateProjectNotificationEmail(String to, String name, Project project,String originalTitle) throws MessagingException {
        if (to == null || name == null || project == null) {
            throw new IllegalArgumentException("Email parameters cannot be null.");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("project", project);
        String htmlContent = templateEngine.process("emails/manager/update-project", context);

        helper.setTo(to);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("Project Has been Updated: " + originalTitle);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendNoLongerTheManagerEmail(String to, String name, String originalTitle) throws MessagingException {
        if (to == null || name == null || originalTitle == null) {
            throw new IllegalArgumentException("Email parameters cannot be null.");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("projectTitle", originalTitle);
        String htmlContent = templateEngine.process("emails/manager/no-longer-the-manager", context);

        helper.setTo(to);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("Manager Role Update: Project" + originalTitle);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendProjectDeletedEmailToManager(String to, String name, String title) throws MessagingException {
        if (to == null || name == null || title == null) {
            throw new IllegalArgumentException("Email parameters cannot be null.");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("title", title);
        String htmlContent = templateEngine.process("emails/manager/project-deletion", context);

        helper.setTo(to);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("Project " + title + "Has been deleted!");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
    //endregion

    //region emailstoEmployee
    public void sendNewTaskNotificationEmail(String to, String name, Task task) throws MessagingException {
        if (to == null || name == null || task == null) {
            throw new IllegalArgumentException("Email parameters cannot be null.");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("task", task);
        String htmlContent = templateEngine.process("emails/employee/new-task", context);

        helper.setTo(to);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("New task Assigned: " + task.getTitle());
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendUpdateTaskNotificationEmail(String to, String name, Task task,String originalTitle) throws MessagingException {
        if (to == null || name == null || task == null) {
            throw new IllegalArgumentException("Email parameters cannot be null.");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("task", task);
        String htmlContent = templateEngine.process("emails/employee/update-task", context);

        helper.setTo(to);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("Task Has been Updated: " + originalTitle);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendNoLongerTheEmployeeEmail(String to, String name, String originalTitle) throws MessagingException {
        if (to == null || name == null || originalTitle == null) {
            throw new IllegalArgumentException("Email parameters cannot be null.");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("taskTitle", originalTitle);
        String htmlContent = templateEngine.process("emails/employee/no-longer-the-employee", context);

        helper.setTo(to);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("Employee Role Update: Task" + originalTitle);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendTaskDeletedEmailToManager(String to, String name, String title) throws MessagingException {
        if (to == null || name == null || title == null) {
            throw new IllegalArgumentException("Email parameters cannot be null.");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("title", title);
        String htmlContent = templateEngine.process("emails/manager/task-deletion", context);

        helper.setTo(to);
        helper.setFrom("khalil.smairi@tek-up.de");
        helper.setSubject("Task " + title + "Has been deleted!");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
    //endregion



}
