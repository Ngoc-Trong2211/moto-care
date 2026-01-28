package vn.motoCare.service;

import vn.motoCare.domain.request.vehiclePrd.CreateVehicleProductRequest;
import vn.motoCare.domain.request.vehiclePrd.UpdateVehicleProductRequest;
import vn.motoCare.domain.response.vehiclePrd.CreateVehicleProductResponse;
import vn.motoCare.domain.response.vehiclePrd.UpdateVehicleProductResponse;

public interface VehicleProductService {
    CreateVehicleProductResponse handleCreate(CreateVehicleProductRequest req);
    UpdateVehicleProductResponse handleUpdate(UpdateVehicleProductRequest req);
}
