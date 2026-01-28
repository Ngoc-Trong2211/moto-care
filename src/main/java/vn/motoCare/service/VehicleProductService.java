package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.vehiclePrd.CreateVehicleProductRequest;
import vn.motoCare.domain.request.vehiclePrd.UpdateVehicleProductRequest;
import vn.motoCare.domain.request.vehiclePrd.VehicleProductSpecificationRequest;
import vn.motoCare.domain.response.vehiclePrd.CreateVehicleProductResponse;
import vn.motoCare.domain.response.vehiclePrd.GetVehicleProductResponse;
import vn.motoCare.domain.response.vehiclePrd.UpdateVehicleProductResponse;

public interface VehicleProductService {
    CreateVehicleProductResponse handleCreate(CreateVehicleProductRequest req);
    UpdateVehicleProductResponse handleUpdate(UpdateVehicleProductRequest req);
    GetVehicleProductResponse handleGetVehicleProduct(Pageable pageable, VehicleProductSpecificationRequest spec);
    void handleDeleteVehicleProduct(Long id);
}
