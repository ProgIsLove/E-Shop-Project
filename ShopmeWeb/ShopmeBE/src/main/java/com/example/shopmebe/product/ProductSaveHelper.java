package com.example.shopmebe.product;

import com.example.shopmebe.utils.FileUploadUtil;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class ProductSaveHelper {

    static void deleteExtraImagesWereRemoveOnForm(Product product) {
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

    static void setMainImageName(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            product.setMainImage(fileName);
        }
    }

    static void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        for (MultipartFile extraImage : extraImageMultiparts) {
            if (!extraImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(extraImage.getOriginalFilename()));
                if (!product.isImageNamePresent(fileName)) {
                    product.addExtraImage(fileName);
                }
            }
        }
    }

    static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs == null || imageIDs.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int count = 0; count < imageIDs.length; count++) {
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];

            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            int id = Integer.parseInt(detailIDs[count]);

            if (id != 0) {
                product.addDetail(id, name, value);
            } else if (StringUtils.hasText(name) && StringUtils.hasText(value)) {
                product.addDetail(name, value);
            }
        }
    }

    static void savedUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
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
}
