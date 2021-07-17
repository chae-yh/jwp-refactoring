package kitchenpos.dao;

import kitchenpos.domain.ProductRequest;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    ProductRequest save(ProductRequest entity);

    Optional<ProductRequest> findById(Long id);

    List<ProductRequest> findAll();
}
