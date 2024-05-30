package shopme.brand;

import com.example.shopmebe.repository.BrandRepository;
import com.example.shopmebe.service.BrandService;
import com.shopme.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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
        Brand brand = new Brand(null, name);

        when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.checkUnique(null, name);

        assertThat(result).isEqualTo("Duplicate Name");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOk() {
        Integer id = null;
        String name = "Acer";

        when(brandRepository.findByName(name)).thenReturn(null);

        String result = brandService.checkUnique(null, name);

        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicate() {

        Integer id = 1;
        String name = "Acer";
        Brand brand = new Brand(id, name);

        when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.checkUnique(2, name);

        assertThat(result).isEqualTo("Duplicate Name");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOk() {
        Integer id = 1;
        String name = "Acer";
        Brand brand = new Brand(id, name);

        when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.checkUnique(id, name);

        assertThat(result).isEqualTo("OK");
    }
}
