package com.example.shopmebe.product;

import com.example.shopmebe.brand.BrandService;
import com.example.shopmebe.category.CategoryService;
import com.example.shopmebe.exception.ProductNotFoundException;
import com.example.shopmebe.utils.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Controller
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, BrandService brandService, CategoryService categoryService) {
        this.productService = productService;
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public String productsFirstPage(Model model) {
        return listByPage(1, model, "name", "asc", null, null);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum,
                             Model model,
                             @RequestParam(name = "sortField", required = false) String sortField,
                             @RequestParam("sortDir") String sortDir,
                             @RequestParam(name = "keyword", required = false) String keyword,
                             @RequestParam(name = "categoryId", required = false) Integer categoryId
    ) {

        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
        List<Product> products = page.getContent();

        List<Category> categories = categoryService.listCategoriesUsedInForm();

        long startCount = (long) (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        if (categoryId != null) model.addAttribute("categoryId", categoryId);

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("products", products);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("categories", categories);

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
        model.addAttribute("numberOfExistingExtraImages", 0);

        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam("fileImage") MultipartFile mainImageMultipart,
                              @RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames) throws IOException {

        setMainImageName(mainImageMultipart, product);
        setExistingExtraImageNames(imageIDs, imageNames, product);
        setNewExtraImageNames(extraImageMultiparts, product);
        setProductDetails(detailIDs, detailNames, detailValues, product);

        Product savedProduct = productService.save(product);

        savedUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

        deleteExtraImagesWereRemoveOnForm(product);

        redirectAttributes.addFlashAttribute("message", "The product has been saved successfully");

        return "redirect:/products";
    }

    private void deleteExtraImagesWereRemoveOnForm(Product product) {
        String extraImageDir = "../product-images/" + product.getId() + "/extras";
        Path dirpath = Paths.get(extraImageDir);

        try(Stream<Path> pathList = Files.list(dirpath)) {
            pathList.forEach(file -> {
                String fileName = file.toFile().getName();

                if (!product.isImageNamePresent(fileName)) {
                    try {
                        Files.delete(file);
                        log.info("Deleted extra image {}", fileName);
                    } catch (IOException e) {
                        log.error("Could not delete extra image: {}", fileName);
                    }
                }
            });
        } catch (IOException ex) {
            log.error("Could not list directory: {}", dirpath);
        }
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

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.productById(id);
            List<Brand> brands = brandService.findAllBrandsWithIdAndName();
            Integer numberOfExistingExtraImages = product.getImages().size();

            model.addAttribute("product", product);
            model.addAttribute("brands", brands);
            model.addAttribute("pageTitle", String.format("Edit Product (ID: %d)", id));
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);


            return "products/product_form";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.productById(id);
            model.addAttribute("product", product);

            return "products/product_detail_modal";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            product.setMainImage(fileName);
        }
    }

    private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        for (MultipartFile extraImage : extraImageMultiparts) {
            if (!extraImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(extraImage.getOriginalFilename()));
                if (!product.isImageNamePresent(fileName)) {
                    product.addExtraImage(fileName);
                }
            }
        }
    }

    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs == null || imageIDs.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int count = 0; count < imageIDs.length; count++) {
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];

            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    private void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            Integer id = Integer.parseInt(detailIDs[count]);

            if (id != 0) {
                product.addDetail(id, name, value);
            } else if (StringUtils.hasText(name) && StringUtils.hasText(value)) {
                product.addDetail(name, value);
            }
        }
    }
}
