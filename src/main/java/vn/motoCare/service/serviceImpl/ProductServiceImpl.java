package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.AgencyEntity;
import vn.motoCare.domain.ProductEntity;
import vn.motoCare.domain.request.product.CreateProductRequest;
import vn.motoCare.domain.response.product.GetProductResponse;
import vn.motoCare.domain.response.product.ProductResponse;
import vn.motoCare.repository.AgencyRepository;
import vn.motoCare.repository.ProductRepository;
import vn.motoCare.service.ProductService;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "PRODUCT-SERVICE")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final AgencyRepository agencyRepository;

    @Override
    public ProductResponse handleCreate(CreateProductRequest req) {
        AgencyEntity agency = this.agencyRepository.findById(req.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Agency không tồn tại!"));

        ProductEntity product = new ProductEntity();
        product.setType(req.getType());
        product.setAgency(agency);

        ProductEntity saved = this.productRepository.save(product);

        return toResponseCreate(saved);
    }

    public static ProductResponse toResponseCreate(ProductEntity entity) {
        ProductResponse res = new ProductResponse();

        res.setId(entity.getId());
        res.setType(entity.getType());

        if (entity.getAgency() != null) {
            res.setAgencyId(entity.getAgency().getId());
            res.setAgencyName(entity.getAgency().getName());
        }

        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        res.setUpdatedBy(entity.getUpdatedBy());

        return res;
    }

    @Override
    public GetProductResponse handleGetProduct(Pageable pageable) {
        Page<ProductEntity> pageData = productRepository.findAll(pageable);

        GetProductResponse response = new GetProductResponse();
        response.setPage(
                new GetProductResponse.DataPage(
                        pageData.getNumber(),
                        pageData.getSize(),
                        pageData.getNumberOfElements(),
                        pageData.getTotalPages()
                )
        );

        List<GetProductResponse.Product> products = pageData.getContent()
                .stream()
                .map(this::mapToProduct)
                .toList();

        response.setProducts(products);

        return response;
    }

    private GetProductResponse.Product mapToProduct(ProductEntity entity) {
        return new GetProductResponse.Product(
                entity.getId(),
                entity.getType(),
                entity.getCreatedAt(),
                entity.getCreatedBy()
        );
    }

    @Override
    public void handleDeleteProduct(Long id) {
        this.productRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại sản phẩm này!"));
        this.productRepository.deleteById(id);
    }
}
