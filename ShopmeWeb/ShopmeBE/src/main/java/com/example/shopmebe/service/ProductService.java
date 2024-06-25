package com.example.shopmebe.service;

import com.example.shopmebe.repository.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }

    public void save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setModifiedTime(new Date());

        productRepository.save(product);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        Product productByName = productRepository.findByName(name);

        if (isCreatingNew) {
            if (productByName != null) {
                return "Duplicate";
            }
        } else {
            if (productByName != null && !Objects.equals(productByName.getId(), id)) {
                return "Duplicate";
            }
        }

        return "OK";
    }
}
