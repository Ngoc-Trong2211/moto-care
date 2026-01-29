package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.energyPrd.CreateEnergyProductRequest;
import vn.motoCare.domain.request.energyPrd.UpdateEnergyProductRequest;
import vn.motoCare.domain.request.energyPrd.EnergyProductSpecificationRequest;
import vn.motoCare.domain.response.energyPrd.CreateEnergyProductResponse;
import vn.motoCare.domain.response.energyPrd.UpdateEnergyProductResponse;
import vn.motoCare.domain.response.energyPrd.GetEnergyProductResponse;
import vn.motoCare.util.exception.IdInvalidException;

public interface EnergyProductService {
    CreateEnergyProductResponse handleCreate(CreateEnergyProductRequest req)
            throws IdInvalidException;
    UpdateEnergyProductResponse handleUpdate(UpdateEnergyProductRequest req)
            throws IdInvalidException;
    GetEnergyProductResponse handleGetEnergyProduct(
            Pageable pageable,
            EnergyProductSpecificationRequest req
    );
    void handleDeleteEnergyProduct(Long id)
            throws IdInvalidException;
}