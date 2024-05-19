package com.example.shopmebe.service;

import com.example.shopmebe.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.stereotype.Service;

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
}
