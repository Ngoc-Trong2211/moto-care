package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.product.CreateProductRequest;
import vn.motoCare.domain.response.product.GetProductResponse;
import vn.motoCare.domain.response.product.ProductResponse;

public interface ProductService {
    ProductResponse handleCreate(CreateProductRequest req);
    GetProductResponse handleGetProduct(Pageable pageable);
    void handleDeleteProduct(Long id);
}
