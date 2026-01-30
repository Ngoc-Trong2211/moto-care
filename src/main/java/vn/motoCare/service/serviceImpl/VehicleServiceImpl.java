package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.AgencyEntity;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.domain.VehicleEntity;
import vn.motoCare.domain.request.vehicle.CreateVehicleRequest;
import vn.motoCare.domain.request.vehicle.UpdateVehicleRequest;
import vn.motoCare.domain.request.vehicle.VehicleSpecificationRequest;
import vn.motoCare.domain.response.vehicle.CreateVehicleResponse;
import vn.motoCare.domain.response.vehicle.GetVehicleResponse;
import vn.motoCare.domain.response.vehicle.UpdateVehicleResponse;
import vn.motoCare.repository.AgencyRepository;
import vn.motoCare.repository.UserRepository;
import vn.motoCare.repository.VehicleRepository;
import vn.motoCare.service.VehicleService;
import vn.motoCare.service.specification.VehicleSpecification;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "VEHICLE-SERVICE")
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;

    @Override
    public CreateVehicleResponse handleCreate(CreateVehicleRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại user này!"));
        AgencyEntity agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại agency này!"));

        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setUser(user);
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setAgency(agency);

        VehicleEntity saved = vehicleRepository.save(vehicle);
        return toCreateResponse(saved);
    }

    private static CreateVehicleResponse toCreateResponse(VehicleEntity entity) {
        CreateVehicleResponse res = new CreateVehicleResponse();
        res.setId(entity.getId());
        if (entity.getUser() != null) {
            res.setUserId(entity.getUser().getId());
            res.setUserEmail(entity.getUser().getEmail());
        }
        res.setBrand(entity.getBrand());
        res.setModel(entity.getModel());
        res.setLicensePlate(entity.getLicensePlate());
        if (entity.getAgency() != null) {
            res.setAgencyId(entity.getAgency().getId());
            res.setAgencyName(entity.getAgency().getName());
        }
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        return res;
    }

    @Override
    public GetVehicleResponse handleGetVehicles(Pageable pageable, VehicleSpecificationRequest req) {
        Specification<VehicleEntity> spec = VehicleSpecification.specVehicle(req);
        Page<VehicleEntity> pageData = vehicleRepository.findAll(spec, pageable);

        GetVehicleResponse response = new GetVehicleResponse();
        response.setPage(
                new GetVehicleResponse.DataPage(
                        pageData.getNumber(),
                        pageData.getSize(),
                        pageData.getNumberOfElements(),
                        pageData.getTotalPages()
                )
        );

        List<GetVehicleResponse.Vehicle> vehicles = pageData.getContent()
                .stream()
                .map(VehicleServiceImpl::mapToVehicle)
                .toList();
        response.setVehicles(vehicles);

        return response;
    }

    private static GetVehicleResponse.Vehicle mapToVehicle(VehicleEntity entity) {
        GetVehicleResponse.Vehicle v = new GetVehicleResponse.Vehicle();
        v.setId(entity.getId());
        if (entity.getUser() != null) {
            v.setUserId(entity.getUser().getId());
            v.setUserEmail(entity.getUser().getEmail());
        }
        v.setBrand(entity.getBrand());
        v.setModel(entity.getModel());
        v.setLicensePlate(entity.getLicensePlate());
        if (entity.getAgency() != null) {
            v.setAgencyId(entity.getAgency().getId());
            v.setAgencyName(entity.getAgency().getName());
        }
        v.setCreatedAt(entity.getCreatedAt());
        v.setCreatedBy(entity.getCreatedBy());
        return v;
    }

    @Override
    public UpdateVehicleResponse handleUpdate(UpdateVehicleRequest request) {
        VehicleEntity vehicle = vehicleRepository.findById(request.getId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại xe này!"));
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại user này!"));
        AgencyEntity agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại agency này!"));

        vehicle.setUser(user);
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setAgency(agency);

        VehicleEntity saved = vehicleRepository.save(vehicle);
        return toUpdateResponse(saved);
    }

    private static UpdateVehicleResponse toUpdateResponse(VehicleEntity entity) {
        UpdateVehicleResponse res = new UpdateVehicleResponse();
        res.setId(entity.getId());
        if (entity.getUser() != null) {
            res.setUserId(entity.getUser().getId());
            res.setUserEmail(entity.getUser().getEmail());
        }
        res.setBrand(entity.getBrand());
        res.setModel(entity.getModel());
        res.setLicensePlate(entity.getLicensePlate());
        if (entity.getAgency() != null) {
            res.setAgencyId(entity.getAgency().getId());
            res.setAgencyName(entity.getAgency().getName());
        }
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        return res;
    }

    @Override
    public void handleDelete(Long id) {
        vehicleRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại xe này!"));
        vehicleRepository.deleteById(id);
    }
}
