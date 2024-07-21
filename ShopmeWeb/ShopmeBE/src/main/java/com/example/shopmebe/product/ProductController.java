package com.example.shopmebe.product;

import com.example.shopmebe.brand.BrandService;
import com.example.shopmebe.exception.ProductNotFoundException;
import com.example.shopmebe.utils.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final BrandService brandService;

    public ProductController(ProductService productService, BrandService brandService) {
        this.productService = productService;
        this.brandService = brandService;
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.listAll();

        model.addAttribute("products", products);

        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> brands = brandService.findAllBrandsWithIdAndName();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("brands", brands);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Crete New Product");

        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam("fileImage") MultipartFile mainImageMultipart,
                              @RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues) throws IOException {

        setMainImageName(mainImageMultipart, product);
        setExtraImageNames(extraImageMultiparts, product);
        setProductDetails(detailNames, detailValues, product);

        Product savedProduct = productService.save(product);

        savedUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

        redirectAttributes.addFlashAttribute("message", "The product has been saved successfully");

        return "redirect:/products";
    }

    private void savedUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            String uploadDir = "../product-images/" + savedProduct.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }
        for (MultipartFile extraImage : extraImageMultiparts) {
            String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
            if (!extraImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(extraImage.getOriginalFilename()));
                FileUploadUtil.saveFile(uploadDir, fileName, extraImage);

            }
        }
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id,
                                             @PathVariable("status") boolean enabled,
                                             RedirectAttributes redirectAttributes) {
        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = String.format("The product ID %d has been %s", id, status);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id,
                                      RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            String productExtraImageDir = "../product-images/" + id;
            String productImagesDir = "../product-images/" + id + "/extras";

            FileUploadUtil.cleanDirectory(productExtraImageDir);
            FileUploadUtil.cleanDirectory(productImagesDir);

            String message = String.format("The product ID %d has been deleted successfully", id);
            redirectAttributes.addFlashAttribute("message", message);
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/products";
    }

    private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            product.setMainImage(fileName);
        }
    }

    private void setExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        for (MultipartFile extraImage : extraImageMultiparts) {
            if (!extraImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(extraImage.getOriginalFilename()));
                product.addExtraImage(fileName);
            }
        }
    }

    private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];

            if (StringUtils.hasText(name) && StringUtils.hasText(value)) {
                product.addDetail(name, value);
            }
        }

    }
}
