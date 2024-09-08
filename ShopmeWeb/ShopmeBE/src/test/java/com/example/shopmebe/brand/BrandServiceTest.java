package com.example.shopmebe.brand;

import com.example.shopmebe.exception.ConflictException;
import com.shopme.common.entity.Brand;
import com.shopme.common.request.CheckUniqueNameRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;


    @Test
    public void testCheckUniqueInNewModeReturnDuplicate() {
        String name = "Acer";
        CheckUniqueNameRequest request = new CheckUniqueNameRequest(null, name);

        Brand brand = new Brand(null, name);

        when(brandRepository.findByName(name)).thenReturn(Optional.of(brand));

        Exception exception = assertThrows(ConflictException.class, () -> brandService.checkUnique(request));

        String expectedMessage = "There is another brand with the same name " + name;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCheckUniqueInNewModeReturnOk() {
        String name = "Acer";
        CheckUniqueNameRequest request = new CheckUniqueNameRequest(null, name);

        when(brandRepository.findByName(name)).thenReturn(Optional.ofNullable(any()));

        assertDoesNotThrow(() -> brandService.checkUnique(request));
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicate() {
        Integer id = 1;
        Integer editId = 2;
        String name = "Acer";
        CheckUniqueNameRequest request = new CheckUniqueNameRequest(editId, name);

        Brand brand = new Brand(id, name);

        when(brandRepository.findByName(name)).thenReturn(Optional.of(brand));

        Exception exception = assertThrows(ConflictException.class, () -> brandService.checkUnique(request));

        String expectedMessage = "There is another brand with the same name " + name;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCheckUniqueInEditModeReturnOk() {
        Integer id = 1;
        String name = "Acer";
        CheckUniqueNameRequest request = new CheckUniqueNameRequest(id, name);
        Brand brand = new Brand(id, name);

        when(brandRepository.findByName(name)).thenReturn(Optional.of(brand));

        assertDoesNotThrow(() -> brandService.checkUnique(request));
    }
}
