package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.motoCare.domain.MaintenanceTypeEntity;
import vn.motoCare.domain.request.maintenanceType.CreateMaintenanceTypeRequest;
import vn.motoCare.domain.request.maintenanceType.UpdateMaintenanceTypeRequest;
import vn.motoCare.domain.response.maintenanceType.CreateMaintenanceTypeResponse;
import vn.motoCare.domain.response.maintenanceType.GetMaintenanceTypeResponse;
import vn.motoCare.domain.response.maintenanceType.UpdateMaintenanceTypeResponse;
import vn.motoCare.repository.MaintenanceTypeRepository;
import vn.motoCare.service.MaintenanceTypeService;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "MAINTENANCE-TYPE-SERVICE")
@RequiredArgsConstructor
@Transactional
public class MaintenanceTypeServiceImpl implements MaintenanceTypeService {
    private final MaintenanceTypeRepository maintenanceTypeRepository;

    @Override
    public CreateMaintenanceTypeResponse handleCreate(CreateMaintenanceTypeRequest request) {
        MaintenanceTypeEntity maintenanceType = new MaintenanceTypeEntity();
        maintenanceType.setName(request.getName());
        maintenanceType.setPeriodKm(request.getPeriodKm());
        maintenanceType.setPeriodMonth(request.getPeriodMonth());
        maintenanceType.setDescription(request.getDescription());

        MaintenanceTypeEntity saved = maintenanceTypeRepository.save(maintenanceType);
        return toCreateResponse(saved);
    }

    @Override
    public GetMaintenanceTypeResponse handleGetAll() {
        List<MaintenanceTypeEntity> entities = maintenanceTypeRepository.findAll();
        GetMaintenanceTypeResponse response = new GetMaintenanceTypeResponse();
        List<GetMaintenanceTypeResponse.MaintenanceType> types = entities.stream()
                .map(MaintenanceTypeServiceImpl::mapToMaintenanceType)
                .toList();
        response.setMaintenanceTypes(types);
        return response;
    }

    @Override
    public CreateMaintenanceTypeResponse handleGetById(Long id) {
        MaintenanceTypeEntity entity = maintenanceTypeRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại loại bảo dưỡng này!"));
        return toCreateResponse(entity);
    }

    @Override
    public UpdateMaintenanceTypeResponse handleUpdate(UpdateMaintenanceTypeRequest request) {
        MaintenanceTypeEntity maintenanceType = maintenanceTypeRepository.findById(request.getId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại loại bảo dưỡng này!"));

        maintenanceType.setName(request.getName());
        maintenanceType.setPeriodKm(request.getPeriodKm());
        maintenanceType.setPeriodMonth(request.getPeriodMonth());
        maintenanceType.setDescription(request.getDescription());

        MaintenanceTypeEntity saved = maintenanceTypeRepository.save(maintenanceType);
        return toUpdateResponse(saved);
    }

    @Override
    public void handleDelete(Long id) {
        maintenanceTypeRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại loại bảo dưỡng này!"));
        maintenanceTypeRepository.deleteById(id);
    }

    private static CreateMaintenanceTypeResponse toCreateResponse(MaintenanceTypeEntity entity) {
        CreateMaintenanceTypeResponse res = new CreateMaintenanceTypeResponse();
        res.setId(entity.getId());
        res.setName(entity.getName());
        res.setPeriodKm(entity.getPeriodKm());
        res.setPeriodMonth(entity.getPeriodMonth());
        res.setDescription(entity.getDescription());
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        return res;
    }

    private static GetMaintenanceTypeResponse.MaintenanceType mapToMaintenanceType(MaintenanceTypeEntity entity) {
        GetMaintenanceTypeResponse.MaintenanceType type = new GetMaintenanceTypeResponse.MaintenanceType();
        type.setId(entity.getId());
        type.setName(entity.getName());
        type.setPeriodKm(entity.getPeriodKm());
        type.setPeriodMonth(entity.getPeriodMonth());
        type.setDescription(entity.getDescription());
        type.setCreatedAt(entity.getCreatedAt());
        type.setCreatedBy(entity.getCreatedBy());
        return type;
    }

    private static UpdateMaintenanceTypeResponse toUpdateResponse(MaintenanceTypeEntity entity) {
        UpdateMaintenanceTypeResponse res = new UpdateMaintenanceTypeResponse();
        res.setId(entity.getId());
        res.setName(entity.getName());
        res.setPeriodKm(entity.getPeriodKm());
        res.setPeriodMonth(entity.getPeriodMonth());
        res.setDescription(entity.getDescription());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        return res;
    }
}
