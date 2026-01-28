package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.VehicleProductEntity;
import vn.motoCare.domain.request.vehiclePrd.CreateVehicleProductRequest;
import vn.motoCare.domain.request.vehiclePrd.UpdateVehicleProductRequest;
import vn.motoCare.domain.response.vehiclePrd.CreateVehicleProductResponse;
import vn.motoCare.domain.response.vehiclePrd.UpdateVehicleProductResponse;
import vn.motoCare.repository.VehicleProductRepository;
import vn.motoCare.service.VehicleProductService;
import vn.motoCare.util.exception.IdInvalidException;

@Service
@Slf4j(topic = "VEHICLE-PRODUCT-SERVICE")
@RequiredArgsConstructor
public class VehicleProductServiceImpl implements VehicleProductService {
    private final VehicleProductRepository vehicleProductRepository;

    @Override
    public CreateVehicleProductResponse handleCreate(CreateVehicleProductRequest req)
            throws IdInvalidException {

        boolean exists = vehicleProductRepository
                .existsByBrandAndModelAndName(req.getBrand(), req.getModel(), req.getName());

        if (exists) {
            throw new IdInvalidException("Vehicle product đã tồn tại!");
        }

        VehicleProductEntity entity = new VehicleProductEntity();
        entity.setBrand(req.getBrand());
        entity.setModel(req.getModel());
        entity.setName(req.getName());
        entity.setColors(req.getColors());
        entity.setStatus(req.getStatus());
        entity.setType(req.getType());
        entity.setPrice(req.getPrice());
        entity.setQuantity(req.getQuantity());

        vehicleProductRepository.save(entity);

        return convertToCreateResponse(entity);
    }

    private CreateVehicleProductResponse convertToCreateResponse(VehicleProductEntity entity) {
        CreateVehicleProductResponse res = new CreateVehicleProductResponse();
        res.setId(entity.getId());
        res.setBrand(entity.getBrand());
        res.setModel(entity.getModel());
        res.setName(entity.getName());
        res.setColors(entity.getColors());
        res.setStatus(entity.getStatus());
        res.setType(entity.getType());
        res.setPrice(entity.getPrice());
        res.setQuantity(entity.getQuantity());
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        return res;
    }

    @Override
    public UpdateVehicleProductResponse handleUpdate(UpdateVehicleProductRequest req)
            throws IdInvalidException {
        VehicleProductEntity entity = vehicleProductRepository
                .findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Vehicle product không tồn tại!"));

        boolean exists = vehicleProductRepository
                .existsByBrandAndModelAndNameAndIdNot(
                        req.getBrand(), req.getModel(), req.getName(), req.getId()
                );

        if (exists) {
            throw new IdInvalidException("Vehicle product đã tồn tại!");
        }

        entity.setBrand(req.getBrand());
        entity.setModel(req.getModel());
        entity.setName(req.getName());
        entity.setColors(req.getColors());
        entity.setStatus(req.getStatus());
        entity.setType(req.getType());
        entity.setPrice(req.getPrice());
        entity.setQuantity(req.getQuantity());

        vehicleProductRepository.save(entity);

        return convertToUpdateResponse(entity);
    }

    private UpdateVehicleProductResponse convertToUpdateResponse(VehicleProductEntity entity) {
        UpdateVehicleProductResponse res = new UpdateVehicleProductResponse();
        res.setId(entity.getId());
        res.setBrand(entity.getBrand());
        res.setModel(entity.getModel());
        res.setName(entity.getName());
        res.setColors(entity.getColors());
        res.setStatus(entity.getStatus());
        res.setType(entity.getType());
        res.setPrice(entity.getPrice());
        res.setQuantity(entity.getQuantity());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        return res;
    }
}
