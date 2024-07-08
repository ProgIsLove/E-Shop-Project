package com.example.shopmebe.product;

import com.shopme.common.request.CheckUniqueNameRequest;
import com.example.shopmebe.exception.ConflictException;
import com.shopme.common.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public void checkUnique(CheckUniqueNameRequest checkUniqueNameRequest) throws ConflictException {
        boolean isCreatingNew = (checkUniqueNameRequest.id() == null || checkUniqueNameRequest.id() == 0);

        Optional<Product> productByName = productRepository.findByName(checkUniqueNameRequest.name());

        if (productByName.isPresent() && (isCreatingNew || !Objects.equals(productByName.get().getId(), checkUniqueNameRequest.id()))) {
            throw new ConflictException(String.format("There is another product with the same name %s", checkUniqueNameRequest.name()));
        }
    }

    @Transactional
    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id, enabled);
    }
}
