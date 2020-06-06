package men.doku.donation.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import io.github.jhipster.config.JHipsterProperties;
import men.doku.donation.domain.User;
import men.doku.donation.security.AuthoritiesConstants;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;
    private final GmailService gmailService;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine, GmailService gmailService) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.gmailService = gmailService;
    }

    @Async
    public void sendEmail(String to, List<String> cc, String subject, String content, boolean isMultipart,
            boolean isHtml, String attachmentName, String attachmentPath) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart,
                isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            if (cc != null)
                message.setCc(cc.toArray(new String[0]));
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            if (isMultipart)
                message.addAttachment(attachmentName, new File(attachmentPath));
            if (gmailService.isAvailable()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mimeMessage.writeTo(baos);
                byte[] bytes = baos.toByteArray();
                String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
                Message gmailMessage = new Message();
                gmailMessage.setRaw(encodedEmail);
                gmailMessage = gmailService.send(gmailMessage);
            } else {
                javaMailSender.send(mimeMessage);
            }
            log.debug("Email sent to {} with subject {} ", to, subject);
        } catch (MailException | MessagingException | IOException | GeneralSecurityException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), null, subject, content, false, true, null, null);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        if (!user.getAuthorities().stream()
                .filter(authority -> authority.getName().equals(AuthoritiesConstants.OFFLINE_STORE)).findFirst()
                .isPresent()) {
                    sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
        } else {
            log.debug("Offline Store no need to be sent activation email.");
        }
    }

    @Async
    public void sendOfflineStoreCreationEmail(User user, File attachment) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("mail/offlineEmail", context);
        String subject = "DOKU Contactless - Payment Beyond Covid-19";
        sendEmail(user.getEmail(), null, subject, content, true, true, attachment.getName(), attachment.getAbsolutePath());
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }
}
