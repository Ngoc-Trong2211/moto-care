package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.AgencyEntity;
import vn.motoCare.domain.request.agency.AgencySpecificationRequest;
import vn.motoCare.domain.request.agency.CreateAgencyRequest;
import vn.motoCare.domain.request.agency.UpdateAgencyRequest;
import vn.motoCare.domain.response.agency.CreateAgencyResponse;
import vn.motoCare.domain.response.agency.GetAgencyResponse;
import vn.motoCare.domain.response.agency.UpdateAgencyResponse;
import vn.motoCare.repository.AgencyRepository;
import vn.motoCare.service.AgencyService;
import vn.motoCare.service.specification.AgencySpecification;
import vn.motoCare.util.exception.EmailAlreadyExistsException;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "AGENCY-SERVICE")
@RequiredArgsConstructor
public class AgencyServiceImpl implements AgencyService {
    private final AgencyRepository agencyRepository;

    @Override
    public CreateAgencyResponse handleCreate(CreateAgencyRequest request) {

        if (this.agencyRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email đã tồn tại!");
        }

        AgencyEntity agency = new AgencyEntity();
        agency.setName(request.getName());
        agency.setEmail(request.getEmail());
        agency.setAddress(request.getAddress());
        agency.setPhone(request.getPhone());
        agency.setActive(true);
        AgencyEntity saved = this.agencyRepository.save(agency);

        return toCreateResponse(saved);
    }

    public static CreateAgencyResponse toCreateResponse(AgencyEntity entity) {
        CreateAgencyResponse res = new CreateAgencyResponse();
        res.setId(entity.getId());
        res.setName(entity.getName());
        res.setEmail(entity.getEmail());
        res.setAddress(entity.getAddress());
        res.setPhone(entity.getPhone());
        res.setActive(entity.isActive());
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        return res;
    }

    @Override
    public UpdateAgencyResponse handleUpdateAgency(UpdateAgencyRequest req)
            throws IdInvalidException {

        AgencyEntity agency = agencyRepository.findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy agency!"));

        if (agencyRepository.existsByEmailAndIdNot(req.getEmail(), req.getId())) {
            throw new IdInvalidException("Email agency đã tồn tại!");
        }

        agency.setName(req.getName());
        agency.setEmail(req.getEmail());
        agency.setAddress(req.getAddress());
        agency.setPhone(req.getPhone());

        agencyRepository.save(agency);

        return convertToUpdateResponse(agency);
    }

    private UpdateAgencyResponse convertToUpdateResponse(
            AgencyEntity agency
    ) {
        UpdateAgencyResponse res = new UpdateAgencyResponse();
        res.setName(agency.getName());
        res.setEmail(agency.getEmail());
        res.setAddress(agency.getAddress());
        res.setPhone(agency.getPhone());
        res.setActive(agency.isActive());
        res.setUpdatedAt(agency.getUpdatedAt());
        res.setUpdatedBy(agency.getUpdatedBy());
        return res;
    }

    @Override
    public GetAgencyResponse handleGetAgency(Pageable pageable, AgencySpecificationRequest req) {
        Specification<AgencyEntity> spec = AgencySpecification.specAgency(req);
        Page<AgencyEntity> pageAgency = this.agencyRepository.findAll(spec, pageable);

        GetAgencyResponse res = new GetAgencyResponse();
        GetAgencyResponse.DataPage resPage = new GetAgencyResponse.DataPage();

        resPage.setTotalPages(pageAgency.getTotalPages());
        resPage.setSize(pageAgency.getSize());
        resPage.setNumber(pageAgency.getNumber() + 1);
        resPage.setNumberOfElements(pageAgency.getNumberOfElements());
        resPage.setTotalElements(pageAgency.getTotalElements());

        res.setPage(resPage);

        List<GetAgencyResponse.Agency> agencies =
                pageAgency.getContent()
                        .stream()
                        .map(agency -> {
                            GetAgencyResponse.Agency a = new GetAgencyResponse.Agency();
                            a.setId(agency.getId());
                            a.setName(agency.getName());
                            a.setEmail(agency.getEmail());
                            a.setAddress(agency.getAddress());
                            a.setPhone(agency.getPhone());
                            a.setActive(agency.isActive());
                            a.setCreatedAt(agency.getCreatedAt());
                            a.setCreatedBy(agency.getCreatedBy());
                            return a;
                        })
                        .toList();

        res.setAgencies(agencies);
        return res;
    }

    @Override
    public void handleDeleteAgency(Long id) throws IdInvalidException {
        AgencyEntity agency = this.agencyRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Agency không tồn tại!"));
        agency.setActive(false);
        this.agencyRepository.save(agency);
    }

    @Override
    public void handleUpdateAgencyActive(Long id) throws IdInvalidException {
        AgencyEntity agency = this.agencyRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Agency không tồn tại!"));
        agency.setActive(true);
        this.agencyRepository.save(agency);
    }
}
