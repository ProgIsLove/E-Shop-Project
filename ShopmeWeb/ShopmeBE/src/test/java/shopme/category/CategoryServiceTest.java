package shopme.category;

import com.example.shopmebe.category.CategoryRepository;
import com.example.shopmebe.category.CategoryService;
import com.example.shopmebe.exception.ConflictException;
import com.shopme.common.entity.Category;
import com.shopme.common.request.CheckUniqueNameWithAliasRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepositoryMock;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateName() {
        String name = "Computers";
        CheckUniqueNameWithAliasRequest request = new CheckUniqueNameWithAliasRequest(null, name, null);

        Category category = new Category(null, name, null);

        when(categoryRepositoryMock.findByName(name)).thenReturn(Optional.of(category));

        Exception exception = assertThrows(ConflictException.class, () -> categoryService.checkUnique(request));

        String expectedMessage = "There is another category having same name " + name;
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateAlias() {
        String alias = "Computers";
        CheckUniqueNameWithAliasRequest request = new CheckUniqueNameWithAliasRequest(null, null, alias);

        Category category = new Category(null, null, alias);

        when(categoryRepositoryMock.findByAlias(alias)).thenReturn(Optional.of(category));

        ConflictException exception = assertThrows(ConflictException.class, () -> categoryService.checkUnique(request));

        String expectedMessage = "There is another category having same alias " + alias;
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName() {
        Integer id = 1;
        String name = "Computers";
        CheckUniqueNameWithAliasRequest request = new CheckUniqueNameWithAliasRequest(id, name, null);

        Category category = new Category(2, name, null);

        when(categoryRepositoryMock.findByName(name)).thenReturn(Optional.of(category));

        ConflictException exception = assertThrows(ConflictException.class, () -> categoryService.checkUnique(request));

        String expectedMessage = "There is another category having same name " + name;
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateAlias() {
        Integer id = 1;
        String alias = "Computers";
        CheckUniqueNameWithAliasRequest request = new CheckUniqueNameWithAliasRequest(id, null, alias);


        Category category = new Category(2, null, alias);

        when(categoryRepositoryMock.findByAlias(alias)).thenReturn(Optional.of(category));

        ConflictException exception = assertThrows(ConflictException.class, () -> categoryService.checkUnique(request));

        String expectedMessage = "There is another category having same alias " + alias;
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCheckUniqueAliasInEditModeReturnOK() {
        Integer id = 1;
        String alias = "test";
        CheckUniqueNameWithAliasRequest request = new CheckUniqueNameWithAliasRequest(id, null, alias);


        when(categoryRepositoryMock.findByAlias(alias)).thenReturn(Optional.ofNullable(any()));

        assertDoesNotThrow(() -> categoryService.checkUnique(request));
    }

    @Test
    public void testCheckUniqueAliasInNewModeReturnOK() {
        String alias = "test";
        CheckUniqueNameWithAliasRequest request = new CheckUniqueNameWithAliasRequest(null, null, alias);

        when(categoryRepositoryMock.findByAlias(alias)).thenReturn(Optional.ofNullable(any()));

        assertDoesNotThrow(() -> categoryService.checkUnique(request));
    }

    @Test
    public void testCheckUniqueNameInNewModeReturnOK() {
        Integer id = 1;
        String name = "test";
        CheckUniqueNameWithAliasRequest request = new CheckUniqueNameWithAliasRequest(id, name, null);

        when(categoryRepositoryMock.findByName(name)).thenReturn(Optional.ofNullable(any()));

        assertDoesNotThrow(() -> categoryService.checkUnique(request));
    }

    @Test
    public void testCheckUniqueNameInEditModeReturnOK() {
        String name = "test";
        CheckUniqueNameWithAliasRequest request = new CheckUniqueNameWithAliasRequest(null, name, null);

        when(categoryRepositoryMock.findByName(name)).thenReturn(Optional.ofNullable(any()));

        assertDoesNotThrow(() -> categoryService.checkUnique(request));
    }
}
