package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.vehicle.CreateVehicleRequest;
import vn.motoCare.domain.request.vehicle.UpdateVehicleRequest;
import vn.motoCare.domain.response.vehicle.CreateVehicleResponse;
import vn.motoCare.domain.response.vehicle.GetVehicleResponse;
import vn.motoCare.domain.response.vehicle.UpdateVehicleResponse;

public interface VehicleService {
    CreateVehicleResponse handleCreate(CreateVehicleRequest request);
    GetVehicleResponse handleGetVehicles(Pageable pageable);
    UpdateVehicleResponse handleUpdate(UpdateVehicleRequest request);
    void handleDelete(Long id);
}
