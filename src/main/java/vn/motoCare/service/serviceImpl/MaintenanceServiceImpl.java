package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.motoCare.domain.AgencyEntity;
import vn.motoCare.domain.MaintenanceEntity;
import vn.motoCare.domain.MaintenanceTypeEntity;
import vn.motoCare.domain.VehicleEntity;
import vn.motoCare.domain.request.maintenance.CreateMaintenanceRequest;
import vn.motoCare.domain.request.maintenance.MaintenanceSpecificationRequest;
import vn.motoCare.domain.request.maintenance.UpdateMaintenanceRequest;
import vn.motoCare.domain.response.maintenance.CreateMaintenanceResponse;
import vn.motoCare.domain.response.maintenance.GetMaintenanceResponse;
import vn.motoCare.domain.response.maintenance.UpdateMaintenanceResponse;
import vn.motoCare.repository.AgencyRepository;
import vn.motoCare.repository.MaintenanceRepository;
import vn.motoCare.repository.MaintenanceTypeRepository;
import vn.motoCare.repository.VehicleRepository;
import vn.motoCare.service.MaintenanceService;
import vn.motoCare.service.specification.MaintenanceSpecification;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "MAINTENANCE-SERVICE")
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final AgencyRepository agencyRepository;

    @Override
    public CreateMaintenanceResponse handleCreate(CreateMaintenanceRequest request) {
        VehicleEntity vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại xe này!"));
        MaintenanceTypeEntity maintenanceType = maintenanceTypeRepository.findById(request.getMaintenanceTypeId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại loại bảo dưỡng này!"));
        AgencyEntity agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại agency này!"));

        MaintenanceEntity maintenance = new MaintenanceEntity();
        maintenance.setVehicle(vehicle);
        maintenance.setMaintenanceType(maintenanceType);
        maintenance.setAgency(agency);
        maintenance.setMaintenanceDate(request.getMaintenanceDate());
        maintenance.setKm(request.getKm());
        maintenance.setNote(request.getNote());
        maintenance.setDueDate(request.getDueDate());

        MaintenanceEntity saved = maintenanceRepository.save(maintenance);
        return toCreateResponse(saved);
    }

    @Override
    public CreateMaintenanceResponse handleGetById(Long id) {
        MaintenanceEntity entity = maintenanceRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại bảo dưỡng này!"));
        return toCreateResponse(entity);
    }

    @Override
    public GetMaintenanceResponse handleGetByVehicleId(Long vehicleId) {
        // Validate vehicle exists
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại xe này!"));

        List<MaintenanceEntity> entities = maintenanceRepository.findByVehicleId(vehicleId);
        GetMaintenanceResponse response = new GetMaintenanceResponse();
        List<GetMaintenanceResponse.Maintenance> maintenances = entities.stream()
                .map(MaintenanceServiceImpl::mapToMaintenance)
                .toList();
        response.setMaintenances(maintenances);
        return response;
    }

    @Override
    public GetMaintenanceResponse handleGetMaintenances(Pageable pageable, MaintenanceSpecificationRequest req) {
        Specification<MaintenanceEntity> spec = MaintenanceSpecification.specMaintenance(req);
        Page<MaintenanceEntity> pageData = maintenanceRepository.findAll(spec, pageable);

        GetMaintenanceResponse response = new GetMaintenanceResponse();
        response.setPage(
                new GetMaintenanceResponse.DataPage(
                        pageData.getNumber(),
                        pageData.getSize(),
                        pageData.getNumberOfElements(),
                        pageData.getTotalPages()
                )
        );

        List<GetMaintenanceResponse.Maintenance> maintenances = pageData.getContent()
                .stream()
                .map(MaintenanceServiceImpl::mapToMaintenance)
                .toList();
        response.setMaintenances(maintenances);

        return response;
    }

    @Override
    public UpdateMaintenanceResponse handleUpdate(UpdateMaintenanceRequest request) {
        MaintenanceEntity maintenance = maintenanceRepository.findById(request.getId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại bảo dưỡng này!"));
        VehicleEntity vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại xe này!"));
        MaintenanceTypeEntity maintenanceType = maintenanceTypeRepository.findById(request.getMaintenanceTypeId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại loại bảo dưỡng này!"));
        AgencyEntity agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại agency này!"));

        maintenance.setVehicle(vehicle);
        maintenance.setMaintenanceType(maintenanceType);
        maintenance.setAgency(agency);
        maintenance.setMaintenanceDate(request.getMaintenanceDate());
        maintenance.setKm(request.getKm());
        maintenance.setNote(request.getNote());
        maintenance.setDueDate(request.getDueDate());

        MaintenanceEntity saved = maintenanceRepository.save(maintenance);
        return toUpdateResponse(saved);
    }

    @Override
    public void handleDelete(Long id) {
        maintenanceRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại bảo dưỡng này!"));
        maintenanceRepository.deleteById(id);
    }

    private static CreateMaintenanceResponse toCreateResponse(MaintenanceEntity entity) {
        CreateMaintenanceResponse res = new CreateMaintenanceResponse();
        res.setId(entity.getId());
        if (entity.getVehicle() != null) {
            res.setVehicleId(entity.getVehicle().getId());
            res.setVehicleLicensePlate(entity.getVehicle().getLicensePlate());
        }
        if (entity.getMaintenanceType() != null) {
            res.setMaintenanceTypeId(entity.getMaintenanceType().getId());
            res.setMaintenanceTypeName(entity.getMaintenanceType().getName());
        }
        if (entity.getAgency() != null) {
            res.setAgencyId(entity.getAgency().getId());
            res.setAgencyName(entity.getAgency().getName());
        }
        res.setMaintenanceDate(entity.getMaintenanceDate());
        res.setKm(entity.getKm());
        res.setNote(entity.getNote());
        res.setDueDate(entity.getDueDate());
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        return res;
    }

    private static GetMaintenanceResponse.Maintenance mapToMaintenance(MaintenanceEntity entity) {
        GetMaintenanceResponse.Maintenance m = new GetMaintenanceResponse.Maintenance();
        m.setId(entity.getId());
        if (entity.getVehicle() != null) {
            m.setVehicleId(entity.getVehicle().getId());
            m.setVehicleLicensePlate(entity.getVehicle().getLicensePlate());
        }
        if (entity.getMaintenanceType() != null) {
            m.setMaintenanceTypeId(entity.getMaintenanceType().getId());
            m.setMaintenanceTypeName(entity.getMaintenanceType().getName());
        }
        if (entity.getAgency() != null) {
            m.setAgencyId(entity.getAgency().getId());
            m.setAgencyName(entity.getAgency().getName());
        }
        m.setMaintenanceDate(entity.getMaintenanceDate());
        m.setKm(entity.getKm());
        m.setNote(entity.getNote());
        m.setDueDate(entity.getDueDate());
        m.setCreatedAt(entity.getCreatedAt());
        m.setCreatedBy(entity.getCreatedBy());
        return m;
    }

    private static UpdateMaintenanceResponse toUpdateResponse(MaintenanceEntity entity) {
        UpdateMaintenanceResponse res = new UpdateMaintenanceResponse();
        res.setId(entity.getId());
        if (entity.getVehicle() != null) {
            res.setVehicleId(entity.getVehicle().getId());
            res.setVehicleLicensePlate(entity.getVehicle().getLicensePlate());
        }
        if (entity.getMaintenanceType() != null) {
            res.setMaintenanceTypeId(entity.getMaintenanceType().getId());
            res.setMaintenanceTypeName(entity.getMaintenanceType().getName());
        }
        if (entity.getAgency() != null) {
            res.setAgencyId(entity.getAgency().getId());
            res.setAgencyName(entity.getAgency().getName());
        }
        res.setMaintenanceDate(entity.getMaintenanceDate());
        res.setKm(entity.getKm());
        res.setNote(entity.getNote());
        res.setDueDate(entity.getDueDate());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        return res;
    }
}
