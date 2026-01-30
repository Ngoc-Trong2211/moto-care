package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.EnergyProductEntity;
import vn.motoCare.domain.ProductEntity;
import vn.motoCare.domain.request.energyPrd.CreateEnergyProductRequest;
import vn.motoCare.domain.request.energyPrd.UpdateEnergyProductRequest;
import vn.motoCare.domain.request.energyPrd.EnergyProductSpecificationRequest;
import vn.motoCare.domain.response.energyPrd.CreateEnergyProductResponse;
import vn.motoCare.domain.response.energyPrd.UpdateEnergyProductResponse;
import vn.motoCare.domain.response.energyPrd.GetEnergyProductResponse;
import vn.motoCare.repository.EnergyProductRepository;
import vn.motoCare.repository.ProductRepository;
import vn.motoCare.service.EnergyProductService;
import vn.motoCare.service.specification.EnergyProductSpecification;
import vn.motoCare.util.enumEntity.EnumStatusProduct;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "ENERGY-PRODUCT-SERVICE")
@RequiredArgsConstructor
public class EnergyProductServiceImpl implements EnergyProductService {

    private final EnergyProductRepository energyProductRepository;
    private final ProductRepository productRepository;

    /* ===================== CREATE ===================== */

    @Override
    public CreateEnergyProductResponse handleCreate(CreateEnergyProductRequest req)
            throws IdInvalidException {

        if (energyProductRepository.existsByName(req.getName())) {
            throw new IdInvalidException("Energy product đã tồn tại!");
        }

        ProductEntity product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IdInvalidException("Product không tồn tại!"));

        EnergyProductEntity entity = new EnergyProductEntity();
        entity.setName(req.getName());
        entity.setDescription(req.getDescription());
        entity.setPrice(req.getPrice());
        entity.setQuantity(req.getQuantity());
        entity.setCapacity(req.getCapacity());
        entity.setStatus(EnumStatusProduct.AVAILABLE);
        entity.setProduct(product);

        energyProductRepository.save(entity);

        return convertToCreateResponse(entity);
    }

    /* ===================== UPDATE ===================== */

    @Override
    public UpdateEnergyProductResponse handleUpdate(UpdateEnergyProductRequest req)
            throws IdInvalidException {
        EnergyProductEntity entity = energyProductRepository.findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Energy product không tồn tại!"));

        if (entity.getStatus() == EnumStatusProduct.DELETED) {
            throw new IdInvalidException("Energy product đã bị xoá!");
        }

        if (energyProductRepository.existsByNameAndIdNot(req.getName(), req.getId())) {
            throw new IdInvalidException("Energy product đã tồn tại!");
        }

        ProductEntity product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IdInvalidException("Product không tồn tại!"));

        entity.setName(req.getName());
        entity.setDescription(req.getDescription());
        entity.setPrice(req.getPrice());
        entity.setQuantity(req.getQuantity());
        entity.setCapacity(req.getCapacity());
        entity.setStatus(req.getStatus());
        entity.setProduct(product);

        energyProductRepository.save(entity);

        return convertToUpdateResponse(entity);
    }

    /* ===================== GET LIST ===================== */

    @Override
    public GetEnergyProductResponse handleGetEnergyProduct(
            Pageable pageable,
            EnergyProductSpecificationRequest req
    ) {
        Specification<EnergyProductEntity> spec =
                EnergyProductSpecification.specEnergyProduct(req);

        Page<EnergyProductEntity> page = energyProductRepository.findAll(spec, pageable);

        GetEnergyProductResponse res = new GetEnergyProductResponse();
        GetEnergyProductResponse.DataPage pageRes = new GetEnergyProductResponse.DataPage();

        pageRes.setTotalPages(page.getTotalPages());
        pageRes.setSize(page.getSize());
        pageRes.setNumber(page.getNumber() + 1);
        pageRes.setNumberOfElements(page.getNumberOfElements());
        res.setPage(pageRes);

        List<GetEnergyProductResponse.EnergyProduct> energies =
                page.getContent().stream().map(entity -> {
                    GetEnergyProductResponse.EnergyProduct e =
                            new GetEnergyProductResponse.EnergyProduct();
                    e.setId(entity.getId());
                    e.setName(entity.getName());
                    e.setDescription(entity.getDescription());
                    e.setPrice(entity.getPrice());
                    e.setQuantity(entity.getQuantity());
                    e.setCapacity(entity.getCapacity());
                    e.setStatus(entity.getStatus());
                    e.setCreatedAt(entity.getCreatedAt());
                    e.setCreatedBy(entity.getCreatedBy());
                    e.setType(entity.getProduct().getType());
                    return e;
                }).toList();

        res.setEnergies(energies);
        return res;
    }

    /* ===================== DELETE (SOFT) ===================== */

    @Override
    public void handleDeleteEnergyProduct(Long id) throws IdInvalidException {

        EnergyProductEntity entity = energyProductRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Energy product không tồn tại!"));

        if (entity.getStatus() == EnumStatusProduct.DELETED) {
            throw new IdInvalidException("Energy product đã bị xoá!");
        }

        entity.setStatus(EnumStatusProduct.DELETED);
        energyProductRepository.save(entity);
    }

    /* ===================== CONVERTER ===================== */

    private CreateEnergyProductResponse convertToCreateResponse(EnergyProductEntity entity) {
        CreateEnergyProductResponse res = new CreateEnergyProductResponse();
        res.setId(entity.getId());
        res.setName(entity.getName());
        res.setDescription(entity.getDescription());
        res.setPrice(entity.getPrice());
        res.setQuantity(entity.getQuantity());
        res.setCapacity(entity.getCapacity());
        res.setStatus(entity.getStatus());
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        res.setType(entity.getProduct().getType());
        return res;
    }

    private UpdateEnergyProductResponse convertToUpdateResponse(EnergyProductEntity entity) {
        UpdateEnergyProductResponse res = new UpdateEnergyProductResponse();
        res.setId(entity.getId());
        res.setName(entity.getName());
        res.setDescription(entity.getDescription());
        res.setPrice(entity.getPrice());
        res.setQuantity(entity.getQuantity());
        res.setCapacity(entity.getCapacity());
        res.setStatus(entity.getStatus());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        res.setType(entity.getProduct().getType());
        return res;
    }
}