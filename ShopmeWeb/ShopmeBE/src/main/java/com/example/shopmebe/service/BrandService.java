package com.example.shopmebe.service;

import com.example.shopmebe.exception.BrandNotFoundException;
import com.example.shopmebe.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> listBrands() {
        return (List<Brand>) brandRepository.findAll();
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
}
