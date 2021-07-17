package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.dto.ProductRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
    	Product product = productRepository.save(productRequest.toProduct());
		return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
		List<Product> products = productRepository.findAll();

    	return products.stream()
			.map(product -> ProductResponse.of(product))
			.collect(Collectors.toList());
    }
}
