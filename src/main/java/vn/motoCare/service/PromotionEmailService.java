package vn.motoCare.service;

/**
 * Sends promotion-related emails (e.g. when a new active promotion is created).
 * Implementations should run email sending asynchronously and must not throw
 * in a way that affects the promotion creation transaction.
 */
public interface PromotionEmailService {

    /**
     * Sends one email per user who owns at least one vehicle at the promotion's agency.
     * Each user receives at most one email. Runs asynchronously; failures are logged only.
     *
     * @param promotionId id of the saved promotion (must be active and have an agency)
     */
    void sendPromotionEmailsAsync(Long promotionId);
}
