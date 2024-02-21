package shopme.category;

import com.example.shopmebe.repository.CategoryRepository;
import com.example.shopmebe.service.CategoryService;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepositoryMock;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testCheckUniqueInReturnDuplicateName() {
        String name = "Computers";

        Category category = new Category(null, name, null);

        when(categoryRepositoryMock.findByName(name)).thenReturn(category);

        String isUnique = categoryService.checkUnique(null, name, null);

        assertThat(isUnique).isEqualTo("Duplicate Name");
    }

    @Test
    public void testCheckUniqueInReturnDuplicateAlias() {
        String alias = "Computers";

        Category category = new Category(null, null, alias);

        when(categoryRepositoryMock.findByAlias(alias)).thenReturn(category);

        String isUnique = categoryService.checkUnique(null, null, alias);

        assertThat(isUnique).isEqualTo("Duplicate Alias");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName() {
        Integer id = 1;
        String name = "Computers";

        Category category = new Category(2, name, null);

        when(categoryRepositoryMock.findByName(name)).thenReturn(category);

        String isUnique = categoryService.checkUnique(id, name, null);

        assertThat(isUnique).isEqualTo("Duplicate Name");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateAlias() {
        Integer id = 1;
        String alias = "Computers";

        Category category = new Category(2, null, alias);

        when(categoryRepositoryMock.findByAlias(alias)).thenReturn(category);

        String isUnique = categoryService.checkUnique(id, null, alias);

        assertThat(isUnique).isEqualTo("Duplicate Alias");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOK() {
        Integer id = 1;
        String alias = "test";

        when(categoryRepositoryMock.findByAlias(alias)).thenReturn(null);

        String isUnique = categoryService.checkUnique(id, null, alias);

        assertThat(isUnique).isEqualTo("OK");
    }
}
