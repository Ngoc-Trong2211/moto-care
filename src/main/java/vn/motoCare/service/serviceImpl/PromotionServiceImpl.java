package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.AgencyEntity;
import vn.motoCare.domain.PromotionEntity;
import vn.motoCare.domain.request.promotion.CreatePromotionRequest;
import vn.motoCare.domain.request.promotion.PromotionSpecificationRequest;
import vn.motoCare.domain.request.promotion.UpdatePromotionRequest;
import vn.motoCare.domain.response.promotion.CreatePromotionResponse;
import vn.motoCare.domain.response.promotion.GetPromotionResponse;
import vn.motoCare.domain.response.promotion.UpdatePromotionResponse;
import vn.motoCare.repository.AgencyRepository;
import vn.motoCare.repository.PromotionRepository;
import vn.motoCare.service.NotificationService;
import vn.motoCare.service.PromotionEmailService;
import vn.motoCare.service.PromotionService;
import vn.motoCare.service.specification.PromotionSpecification;
import vn.motoCare.util.enumEntity.EnumTypeNotification;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "PROMOTION-SERVICE")
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final AgencyRepository agencyRepository;
    private final NotificationService notificationService;
    private final PromotionEmailService promotionEmailService;

    @Override
    public CreatePromotionResponse handleCreate(CreatePromotionRequest request) {
        AgencyEntity agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại agency này!"));

        PromotionEntity promotion = new PromotionEntity();
        promotion.setTitle(request.getTitle());
        promotion.setDescription(request.getDescription());
        promotion.setActive(request.isActive());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setAgencyEntity(agency);

        PromotionEntity saved = promotionRepository.save(promotion);

        // In-app notification for all users if promotion is active
        if (saved.isActive()) {
            String title = "New Promotion Available";
            String content = saved.getTitle() + ": " + (saved.getDescription() != null ? saved.getDescription() : "");
            notificationService.createNotificationForAllUsers(title, content, EnumTypeNotification.PROMOTION);
            // Email notification to users who own vehicles at this promotion's agency (async, non-blocking)
            promotionEmailService.sendPromotionEmailsAsync(saved.getId());
        }

        return toCreateResponse(saved);
    }

    private static CreatePromotionResponse toCreateResponse(PromotionEntity entity) {
        CreatePromotionResponse res = new CreatePromotionResponse();
        res.setId(entity.getId());
        res.setTitle(entity.getTitle());
        res.setDescription(entity.getDescription());
        res.setActive(entity.isActive());
        res.setStartDate(entity.getStartDate());
        res.setEndDate(entity.getEndDate());
        if (entity.getAgencyEntity() != null) {
            res.setAgencyId(entity.getAgencyEntity().getId());
            res.setAgencyName(entity.getAgencyEntity().getName());
        }
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        return res;
    }

    @Override
    public GetPromotionResponse handleGetPromotions(Pageable pageable, PromotionSpecificationRequest req) {
        Specification<PromotionEntity> spec = PromotionSpecification.specPromotion(req);
        Page<PromotionEntity> promotionPage = promotionRepository.findAll(spec, pageable);

        GetPromotionResponse response = new GetPromotionResponse();
        response.setPage(
                new GetPromotionResponse.DataPage(
                        promotionPage.getNumber(),
                        promotionPage.getSize(),
                        promotionPage.getNumberOfElements(),
                        promotionPage.getTotalPages()
                )
        );

        List<GetPromotionResponse.Promotion> promotionList = promotionPage.getContent()
                .stream()
                .map(PromotionServiceImpl::mapToPromotion)
                .toList();
        response.setPromotions(promotionList);

        return response;
    }

    private static GetPromotionResponse.Promotion mapToPromotion(PromotionEntity entity) {
        GetPromotionResponse.Promotion p = new GetPromotionResponse.Promotion();
        p.setId(entity.getId());
        p.setTitle(entity.getTitle());
        p.setDescription(entity.getDescription());
        p.setActive(entity.isActive());
        p.setStartDate(entity.getStartDate());
        p.setEndDate(entity.getEndDate());
        if (entity.getAgencyEntity() != null) {
            p.setAgencyId(entity.getAgencyEntity().getId());
            p.setAgencyName(entity.getAgencyEntity().getName());
        }
        p.setCreatedAt(entity.getCreatedAt());
        p.setCreatedBy(entity.getCreatedBy());
        return p;
    }

    @Override
    public UpdatePromotionResponse handleUpdate(UpdatePromotionRequest request) {
        PromotionEntity promotion = promotionRepository.findById(request.getId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại khuyến mãi này!"));

        AgencyEntity agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại agency này!"));

        promotion.setTitle(request.getTitle());
        promotion.setDescription(request.getDescription());
        promotion.setActive(request.isActive());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setAgencyEntity(agency);

        PromotionEntity saved = promotionRepository.save(promotion);
        return toUpdateResponse(saved);
    }

    private static UpdatePromotionResponse toUpdateResponse(PromotionEntity entity) {
        UpdatePromotionResponse res = new UpdatePromotionResponse();
        res.setId(entity.getId());
        res.setTitle(entity.getTitle());
        res.setDescription(entity.getDescription());
        res.setActive(entity.isActive());
        res.setStartDate(entity.getStartDate());
        res.setEndDate(entity.getEndDate());
        if (entity.getAgencyEntity() != null) {
            res.setAgencyId(entity.getAgencyEntity().getId());
            res.setAgencyName(entity.getAgencyEntity().getName());
        }
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        return res;
    }

    @Override
    public void handleDelete(Long id) {
        promotionRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại khuyến mãi này!"));
        promotionRepository.deleteById(id);
    }
}
