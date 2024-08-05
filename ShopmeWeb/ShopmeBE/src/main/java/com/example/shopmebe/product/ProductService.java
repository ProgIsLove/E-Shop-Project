package com.example.shopmebe.product;

import com.example.shopmebe.exception.CategoryNotFoundException;
import com.example.shopmebe.exception.ProductNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.request.CheckUniqueNameRequest;
import com.example.shopmebe.exception.ConflictException;
import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 5;

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + categoryId + "-";
                return productRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            }
            return productRepository.findAll(keyword, pageable);
        }

        if (categoryId != null && categoryId > 0) {
            String categoryIdMatch = "-" + categoryId + "-";
            return productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
        }

        return productRepository.findAll(pageable);
    }

    public Product save(Product product) {
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

        return productRepository.save(product);
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

    @Transactional
    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);
        if (countById == null ||  countById == 0) {
            throw new ProductNotFoundException(String.format("Could not find any product with ID %d", id));
        }

        productRepository.deleteById(id);
    }

    public Product productById(Integer id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        String.format("Could not find any product with ID %d", id)));
    }
}
