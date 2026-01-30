package vn.motoCare.service.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vn.motoCare.domain.AgencyEntity;
import vn.motoCare.domain.PromotionEntity;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.repository.PromotionRepository;
import vn.motoCare.repository.VehicleRepository;
import vn.motoCare.service.PromotionEmailService;
import vn.motoCare.service.dto.PromotionEmailContext;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j(topic = "PROMOTION-EMAIL-SERVICE")
@RequiredArgsConstructor
public class PromotionEmailServiceImpl implements PromotionEmailService {

    private static final String TEMPLATE_NAME = "email/promotion";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

    private final PromotionRepository promotionRepository;
    private final VehicleRepository vehicleRepository;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Override
    @Async
    @Transactional(readOnly = true)
    public void sendPromotionEmailsAsync(Long promotionId) {
        try {
            PromotionEntity promotion = promotionRepository.findById(promotionId).orElse(null);
            if (promotion == null || !promotion.isActive()) {
                log.debug("Promotion {} not found or not active, skipping email send", promotionId);
                return;
            }
            AgencyEntity agency = promotion.getAgencyEntity();
            if (agency == null) {
                log.warn("Promotion {} has no agency, skipping email send", promotionId);
                return;
            }
            Long agencyId = agency.getId();
            List<UserEntity> recipients = vehicleRepository.findDistinctUsersByAgencyId(agencyId);
            if (recipients.isEmpty()) {
                log.debug("No users with vehicles at agency {} for promotion {}", agencyId, promotionId);
                return;
            }
            PromotionEmailContext context = buildContext(promotion, agency);
            String htmlBody = renderTemplate(context);
            String subject = "New promotion: " + (promotion.getTitle() != null ? promotion.getTitle() : "MotoCare");

            for (UserEntity user : recipients) {
                String email = user.getEmail();
                if (email == null || email.isBlank()) continue;
                try {
                    sendEmail(email, subject, htmlBody);
                    log.debug("Promotion email sent to {} for promotion {}", email, promotionId);
                } catch (Exception e) {
                    log.error("Failed to send promotion email to {} for promotion {}: {}", email, promotionId, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error sending promotion emails for promotion {}: {}", promotionId, e.getMessage(), e);
        }
    }

    private PromotionEmailContext buildContext(PromotionEntity promotion, AgencyEntity agency) {
        String startDate = promotion.getStartDate() != null ? promotion.getStartDate().format(DATE_FORMATTER) : null;
        String endDate = promotion.getEndDate() != null ? promotion.getEndDate().format(DATE_FORMATTER) : null;
        return PromotionEmailContext.builder()
                .promotionTitle(promotion.getTitle())
                .promotionDescription(promotion.getDescription())
                .discountValue(null)
                .startDate(startDate)
                .endDate(endDate)
                .agencyName(agency.getName())
                .build();
    }

    private String renderTemplate(PromotionEmailContext ctx) {
        Context context = new Context(Locale.ENGLISH);
        context.setVariable("promotionTitle", ctx.getPromotionTitle());
        context.setVariable("promotionDescription", ctx.getPromotionDescription());
        context.setVariable("discountValue", ctx.getDiscountValue());
        context.setVariable("startDate", ctx.getStartDate());
        context.setVariable("endDate", ctx.getEndDate());
        context.setVariable("agencyName", ctx.getAgencyName());
        return templateEngine.process(TEMPLATE_NAME, context);
    }

    private void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail != null && !fromEmail.isBlank() ? fromEmail : "noreply@motocare.local");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }
}
