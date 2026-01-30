package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.promotion.CreatePromotionRequest;
import vn.motoCare.domain.request.promotion.UpdatePromotionRequest;
import vn.motoCare.domain.response.promotion.CreatePromotionResponse;
import vn.motoCare.domain.response.promotion.GetPromotionResponse;
import vn.motoCare.domain.response.promotion.UpdatePromotionResponse;

public interface PromotionService {
    CreatePromotionResponse handleCreate(CreatePromotionRequest request);
    GetPromotionResponse handleGetPromotions(Pageable pageable);
    UpdatePromotionResponse handleUpdate(UpdatePromotionRequest request);
    void handleDelete(Long id);
}
