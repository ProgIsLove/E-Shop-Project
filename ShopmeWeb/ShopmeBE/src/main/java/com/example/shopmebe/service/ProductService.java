package com.example.shopmebe.service;

import com.example.shopmebe.repository.ProductRepository;
import com.shopme.common.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }
}
