package com.example.shopmefe.product;

import com.example.shopmefe.exception.ProductNotFoundException;
import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 10;
    public static final int SEARCH_RESULTS_PER_PAGE = 10;

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> listByCategory(int pageNum, Integer categoryId) {
        String categoryIdMatch = String.format("-%s-", categoryId);
        PageRequest pageRequest = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return productRepository.listByCategory(categoryId, categoryIdMatch, pageRequest);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        return productRepository.findByAlias(alias).orElseThrow(() ->
                new ProductNotFoundException("Could not find any product with alias %s".formatted(alias)));
    }

    public Page<Product> search(String keyword, int pageNum) {
        PageRequest pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
        return productRepository.search(keyword, pageable);
    }
}
