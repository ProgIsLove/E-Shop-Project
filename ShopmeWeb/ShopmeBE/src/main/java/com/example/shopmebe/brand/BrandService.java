package com.example.shopmebe.brand;

import com.example.shopmebe.exception.BrandNotFoundException;
import com.example.shopmebe.exception.ConflictException;
import com.shopme.common.entity.Brand;
import com.shopme.common.request.CheckUniqueNameRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public void checkUnique(CheckUniqueNameRequest checkUniqueNameRequest) throws ConflictException {
        boolean isCreatingNew = (checkUniqueNameRequest.id() == null || checkUniqueNameRequest.id() == 0);

        Optional<Brand> brandByName = brandRepository.findByName(checkUniqueNameRequest.name());

        if (brandByName.isPresent() && (isCreatingNew || !Objects.equals(brandByName.get().getId(), checkUniqueNameRequest.id()))) {
            throw new ConflictException(
                    String.format("There is another brand with the same name %s",
                            checkUniqueNameRequest.name()));
        }
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
