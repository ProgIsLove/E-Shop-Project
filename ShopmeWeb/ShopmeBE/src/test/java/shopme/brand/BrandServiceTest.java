package shopme.brand;

import com.example.shopmebe.brand.BrandRepository;
import com.example.shopmebe.brand.BrandService;
import com.example.shopmebe.brand.BrandStatus;
import com.example.shopmebe.brand.CheckUniqueRequest;
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
        CheckUniqueRequest request = new CheckUniqueRequest(null, name);

        Brand brand = new Brand(null, name);

        when(brandRepository.findByName(name)).thenReturn(brand);

        BrandStatus result = brandService.checkUnique(request);

        assertThat(result).isEqualTo(BrandStatus.DUPLICATE_NAME);
    }

    @Test
    public void testCheckUniqueInNewModeReturnOk() {
        String name = "Acer";
        CheckUniqueRequest request = new CheckUniqueRequest(null, name);

        when(brandRepository.findByName(name)).thenReturn(null);

        BrandStatus result = brandService.checkUnique(request);

        assertThat(result).isEqualTo(BrandStatus.OK);
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicate() {
        Integer id = 1;
        Integer editId = 2;
        String name = "Acer";
        CheckUniqueRequest request = new CheckUniqueRequest(editId, name);

        Brand brand = new Brand(id, name);

        when(brandRepository.findByName(name)).thenReturn(brand);

        BrandStatus result = brandService.checkUnique(request);

        assertThat(result).isEqualTo(BrandStatus.DUPLICATE_NAME);
    }

    @Test
    public void testCheckUniqueInEditModeReturnOk() {
        Integer id = 1;
        String name = "Acer";
        CheckUniqueRequest request = new CheckUniqueRequest(id, name);
        Brand brand = new Brand(id, name);

        when(brandRepository.findByName(name)).thenReturn(brand);

        BrandStatus result = brandService.checkUnique(request);

        assertThat(result).isEqualTo(BrandStatus.OK);
    }
}
