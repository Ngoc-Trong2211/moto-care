package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.AccessoryProductEntity;
import vn.motoCare.domain.ProductEntity;
import vn.motoCare.repository.AccessoryProductRepository;
import vn.motoCare.repository.ProductRepository;
import vn.motoCare.service.AccessoryProductService;
import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.AccessoryPrd.AccessoryProductSpecificationRequest;
import vn.motoCare.domain.request.AccessoryPrd.CreateAccessoryProductRequest;
import vn.motoCare.domain.request.AccessoryPrd.UpdateAccessoryProductRequest;
import vn.motoCare.domain.response.AccessoryPrd.CreateAccessoryProductResponse;
import vn.motoCare.domain.response.AccessoryPrd.GetAccessoryProductResponse;
import vn.motoCare.domain.response.AccessoryPrd.UpdateAccessoryProductResponse;
import vn.motoCare.service.specification.AccessoryProductSpecification;
import vn.motoCare.util.enumEntity.EnumStatusProduct;
import vn.motoCare.util.exception.IdInvalidException;

@Service
@Slf4j(topic = "ACCESSORY-PRODUCT-SERVICE")
@RequiredArgsConstructor
public class AccessoryProductServiceImpl implements AccessoryProductService {

    private final AccessoryProductRepository accessoryProductRepository;
    private final ProductRepository productRepository;

    @Override
    public CreateAccessoryProductResponse handleCreate(CreateAccessoryProductRequest req)
            throws IdInvalidException {

        boolean exists = this.accessoryProductRepository
                .existsByName(req.getName());

        if (exists) {
            throw new IdInvalidException("Accessory product đã tồn tại!");
        }

        ProductEntity product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IdInvalidException("Product không tồn tại!"));

        AccessoryProductEntity entity = new AccessoryProductEntity();
        entity.setName(req.getName());
        entity.setDescription(req.getDescription());
        entity.setPrice(req.getPrice());
        entity.setQuantity(req.getQuantity());
        entity.setStatus(EnumStatusProduct.AVAILABLE);
        entity.setProduct(product);

        accessoryProductRepository.save(entity);

        return convertToCreateResponse(entity);
    }

    @Override
    public UpdateAccessoryProductResponse handleUpdate(UpdateAccessoryProductRequest req)
            throws IdInvalidException {

        AccessoryProductEntity entity = accessoryProductRepository.findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Accessory product không tồn tại!"));

        if (accessoryProductRepository.existsByIdAndStatus(req.getId(), EnumStatusProduct.DELETED)) {
            throw new IdInvalidException("Accessory product đã bị xoá!");
        }

        if (accessoryProductRepository.existsByNameAndIdNot(req.getName(), req.getId())) {
            throw new IdInvalidException("Accessory product đã tồn tại!");
        }

        entity.setName(req.getName());
        entity.setDescription(req.getDescription());
        entity.setPrice(req.getPrice());
        entity.setQuantity(req.getQuantity());
        entity.setStatus(req.getStatus());

        ProductEntity product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IdInvalidException("Product không tồn tại!"));
        entity.setProduct(product);

        accessoryProductRepository.save(entity);

        return convertToUpdateResponse(entity);
    }

    @Override
    public GetAccessoryProductResponse handleGetAccessoryProduct(
            Pageable pageable,
            AccessoryProductSpecificationRequest req
    ) {
        Specification<AccessoryProductEntity> spec =
                AccessoryProductSpecification.specAccessoryProduct(req);

        Page<AccessoryProductEntity> page = accessoryProductRepository.findAll(spec, pageable);

        GetAccessoryProductResponse res = new GetAccessoryProductResponse();
        GetAccessoryProductResponse.DataPage pageRes = new GetAccessoryProductResponse.DataPage();

        pageRes.setTotalPages(page.getTotalPages());
        pageRes.setSize(page.getSize());
        pageRes.setNumber(page.getNumber() + 1);
        pageRes.setNumberOfElements(page.getNumberOfElements());
        res.setPage(pageRes);

        res.setAccessories(
                page.getContent().stream().map(entity -> {
                    GetAccessoryProductResponse.AccessoryProduct a =
                            new GetAccessoryProductResponse.AccessoryProduct();
                    a.setId(entity.getId());
                    a.setName(entity.getName());
                    a.setDescription(entity.getDescription());
                    a.setPrice(entity.getPrice());
                    a.setQuantity(entity.getQuantity());
                    a.setStatus(entity.getStatus());
                    a.setCreatedAt(entity.getCreatedAt());
                    a.setCreatedBy(entity.getCreatedBy());
                    a.setType(entity.getProduct().getType());
                    return a;
                }).toList()
        );

        return res;
    }

    @Override
    public void handleDeleteAccessoryProduct(Long id) throws IdInvalidException {
        AccessoryProductEntity entity = accessoryProductRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Accessory product không tồn tại!"));

        if (entity.getStatus() == EnumStatusProduct.DELETED) {
            throw new IdInvalidException("Accessory product đã bị xóa!");
        }

        entity.setStatus(EnumStatusProduct.DELETED);
        accessoryProductRepository.save(entity);
    }

    /* ===================== CONVERTER ===================== */

    private CreateAccessoryProductResponse convertToCreateResponse(AccessoryProductEntity entity) {
        CreateAccessoryProductResponse res = new CreateAccessoryProductResponse();
        res.setId(entity.getId());
        res.setName(entity.getName());
        res.setDescription(entity.getDescription());
        res.setPrice(entity.getPrice());
        res.setQuantity(entity.getQuantity());
        res.setStatus(entity.getStatus());
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        res.setType(entity.getProduct().getType());
        return res;
    }

    private UpdateAccessoryProductResponse convertToUpdateResponse(AccessoryProductEntity entity) {
        UpdateAccessoryProductResponse res = new UpdateAccessoryProductResponse();
        res.setId(entity.getId());
        res.setName(entity.getName());
        res.setDescription(entity.getDescription());
        res.setPrice(entity.getPrice());
        res.setQuantity(entity.getQuantity());
        res.setStatus(entity.getStatus());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        res.setType(entity.getProduct().getType());
        return res;
    }
}
