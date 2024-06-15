package com.example.shopmebe.service;

import com.example.shopmebe.exception.BrandNotFoundException;
import com.example.shopmebe.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class BrandService {

    public static final int BRAND_PER_PAGE = 10;

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> findAllBrandsWithIdAndName() {
        return brandRepository.findAllBrandsWithIdAndName();
    }

    public Brand getBrandById(Integer id) throws BrandNotFoundException {
        return brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(
                        String.format("Could not find any brand with ID: %d", id)));
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Transactional
    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = brandRepository.countById(id);
        if (countById == null ||  countById == 0) {
            throw new BrandNotFoundException(String.format("Could not find any category with ID %d", id));
        }

        brandRepository.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        Brand brandByName = brandRepository.findByName(name);

        if (isCreatingNew) {
            if (brandByName != null) {
                return "Duplicate Name";
            }
        } else {
            if (brandByName != null && !Objects.equals(brandByName.getId(), id)) {
                return "Duplicate Name";
            }
        }

        return "OK";
    }

    public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sort);

        if (keyword != null) {
            return brandRepository.findAll(keyword, pageable);
        }

        return brandRepository.findAll(pageable);
    }
}
