package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.ProductEntity;
import vn.motoCare.domain.VehicleProductEntity;
import vn.motoCare.domain.request.vehiclePrd.CreateVehicleProductRequest;
import vn.motoCare.domain.request.vehiclePrd.UpdateVehicleProductRequest;
import vn.motoCare.domain.request.vehiclePrd.VehicleProductSpecificationRequest;
import vn.motoCare.domain.response.vehiclePrd.CreateVehicleProductResponse;
import vn.motoCare.domain.response.vehiclePrd.GetVehicleProductResponse;
import vn.motoCare.domain.response.vehiclePrd.UpdateVehicleProductResponse;
import vn.motoCare.repository.ProductRepository;
import vn.motoCare.repository.VehicleProductRepository;
import vn.motoCare.service.VehicleProductService;
import vn.motoCare.service.specification.VehicleProductSpecification;
import vn.motoCare.util.enumEntity.EnumStatusProduct;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "VEHICLE-PRODUCT-SERVICE")
@RequiredArgsConstructor
public class VehicleProductServiceImpl implements VehicleProductService {
    private final VehicleProductRepository vehicleProductRepository;
    private final ProductRepository productRepository;

    @Override
    public CreateVehicleProductResponse handleCreate(CreateVehicleProductRequest req)
            throws IdInvalidException {

        boolean exists = this.vehicleProductRepository
                .existsByBrandAndModelAndName(req.getBrand(), req.getModel(), req.getName());

        if (exists) {
            throw new IdInvalidException("Vehicle product đã tồn tại!");
        }

        ProductEntity product = this.productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IdInvalidException("Product không tồn tại!"));

        VehicleProductEntity entity = new VehicleProductEntity();
        entity.setBrand(req.getBrand());
        entity.setModel(req.getModel());
        entity.setName(req.getName());
        entity.setColors(req.getColors());
        entity.setStatus(EnumStatusProduct.AVAILABLE);
        entity.setPrice(req.getPrice());
        entity.setQuantity(req.getQuantity());
        entity.setProduct(product);

        this.vehicleProductRepository.save(entity);

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
        res.setPrice(entity.getPrice());
        res.setQuantity(entity.getQuantity());
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        res.setType(entity.getProduct().getType());
        return res;
    }

    @Override
    public UpdateVehicleProductResponse handleUpdate(UpdateVehicleProductRequest req)
            throws IdInvalidException {
        VehicleProductEntity entity = this.vehicleProductRepository
                .findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Vehicle product không tồn tại!"));

        boolean exists = this.vehicleProductRepository
                .existsByBrandAndModelAndNameAndIdNot(
                        req.getBrand(), req.getModel(), req.getName(), req.getId()
                );

        if (exists) {
            throw new IdInvalidException("Vehicle product đã tồn tại!");
        }

        ProductEntity product = this.productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IdInvalidException("Product không tồn tại!"));

        entity.setBrand(req.getBrand());
        entity.setModel(req.getModel());
        entity.setName(req.getName());
        entity.setColors(req.getColors());
        entity.setStatus(req.getStatus());
        entity.setPrice(req.getPrice());
        entity.setQuantity(req.getQuantity());
        entity.setProduct(product);

        this.vehicleProductRepository.save(entity);

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
        res.setPrice(entity.getPrice());
        res.setQuantity(entity.getQuantity());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        res.setType(entity.getProduct().getType());
        return res;
    }

    @Override
    public GetVehicleProductResponse handleGetVehicleProduct(Pageable pageable, VehicleProductSpecificationRequest req) {
        Specification<VehicleProductEntity> spec = VehicleProductSpecification.specVehicleProduct(req);

        Page<VehicleProductEntity> pageProduct = this.vehicleProductRepository.findAll(spec, pageable);

        GetVehicleProductResponse res = new GetVehicleProductResponse();
        GetVehicleProductResponse.DataPage resPage = new GetVehicleProductResponse.DataPage();

        resPage.setTotalPages(pageProduct.getTotalPages());
        resPage.setSize(pageProduct.getSize());
        resPage.setNumber(pageProduct.getNumber() + 1);
        resPage.setNumberOfElements(pageProduct.getNumberOfElements());
        res.setPage(resPage);

        List<GetVehicleProductResponse.VehicleProduct> products =
                pageProduct.getContent().stream()
                        .map(product -> {
                            GetVehicleProductResponse.VehicleProduct p =
                                    new GetVehicleProductResponse.VehicleProduct();
                            p.setId(product.getId());
                            p.setBrand(product.getBrand());
                            p.setModel(product.getModel());
                            p.setName(product.getName());
                            p.setColors(product.getColors());
                            p.setStatus(product.getStatus());
                            p.setPrice(product.getPrice());
                            p.setQuantity(product.getQuantity());
                            p.setCreatedAt(product.getCreatedAt());
                            p.setCreatedBy(product.getCreatedBy());
                            p.setType(product.getProduct().getType());
                            return p;
                        }).toList();

        res.setProducts(products);
        return res;
    }

    @Override
    public void handleDeleteVehicleProduct(Long id) throws IdInvalidException {
        VehicleProductEntity entity = this.vehicleProductRepository
                .findById(id)
                .orElseThrow(() -> new IdInvalidException("Vehicle product không tồn tại!"));
        if (entity.getStatus() == EnumStatusProduct.DELETED) {
            throw new IdInvalidException("Vehicle product đã bị xóa!");
        }
        entity.setStatus(EnumStatusProduct.DELETED);
        this.vehicleProductRepository.save(entity);
    }
}
