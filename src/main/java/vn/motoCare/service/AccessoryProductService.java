package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.AccessoryPrd.AccessoryProductSpecificationRequest;
import vn.motoCare.domain.request.AccessoryPrd.CreateAccessoryProductRequest;
import vn.motoCare.domain.request.AccessoryPrd.UpdateAccessoryProductRequest;
import vn.motoCare.domain.response.AccessoryPrd.CreateAccessoryProductResponse;
import vn.motoCare.domain.response.AccessoryPrd.GetAccessoryProductResponse;
import vn.motoCare.domain.response.AccessoryPrd.UpdateAccessoryProductResponse;
import vn.motoCare.util.exception.IdInvalidException;

public interface AccessoryProductService {
    CreateAccessoryProductResponse handleCreate(CreateAccessoryProductRequest req)
            throws IdInvalidException;
    UpdateAccessoryProductResponse handleUpdate(UpdateAccessoryProductRequest req)
            throws IdInvalidException;
    GetAccessoryProductResponse handleGetAccessoryProduct(
            Pageable pageable,
            AccessoryProductSpecificationRequest req
    );
    void handleDeleteAccessoryProduct(Long id) throws IdInvalidException;
}