package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.agency.AgencySpecificationRequest;
import vn.motoCare.domain.request.agency.CreateAgencyRequest;
import vn.motoCare.domain.request.agency.UpdateAgencyRequest;
import vn.motoCare.domain.response.agency.CreateAgencyResponse;
import vn.motoCare.domain.response.agency.GetAgencyResponse;
import vn.motoCare.domain.response.agency.UpdateAgencyResponse;
import vn.motoCare.util.exception.IdInvalidException;

public interface AgencyService {
    CreateAgencyResponse handleCreate(CreateAgencyRequest request);
    UpdateAgencyResponse handleUpdateAgency(UpdateAgencyRequest req) throws IdInvalidException;
    GetAgencyResponse handleGetAgency(Pageable pageable, AgencySpecificationRequest req);
    void handleDeleteAgency(Long id) throws IdInvalidException;
    void handleUpdateAgencyActive(Long id) throws IdInvalidException;
}
