package shopme.user;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.repository.RoleRepository;
import com.example.shopmebe.service.UserService;
import com.shopme.common.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ShopmeBeApplication.class)
class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepositoryMock;

    private static Role role;
    private static Role role2;

    @BeforeEach
    public void setup() {
        role = Role.builder()
                   .id(1)
                   .name("Admin")
                   .description("manage everything").build();

        role2 = Role.builder()
                    .id(2)
                    .name("Salesperson")
                    .description("manage product price, customers, shipping, orders and sales report").build();
    }

    @Test
    void findAllRoles() {

        when(roleRepositoryMock.findAll()).thenReturn(Arrays.asList(role, role2));

        Iterable<Role> roles = roleRepositoryMock.findAll();

        Integer size = null;
        if (roles instanceof Collection<Role>) {
            size = ((Collection<Role>) roles).size();
        }

        verify(roleRepositoryMock).findAll();

        assertNotNull(roles);
        assertEquals(2, size);
    }

    @Test
    void getRoleById() {

        final int ID = 1;

        when(roleRepositoryMock.findById(ID)).thenReturn(Optional.of(role));

        Optional<Role> roleById = roleRepositoryMock.findById(ID);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(roleRepositoryMock).findById(idCaptor.capture());

        Integer idValue = idCaptor.getValue();

        assertTrue(roleById.isPresent());
        assertEquals(1, idValue);
        assertEquals("Admin", roleById.get().getName());
        assertEquals("manage everything", roleById.get().getDescription());
    }

    @Test
    void saveRole() {

        when(roleRepositoryMock.save(role)).thenReturn(role);

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);

        roleRepositoryMock.save(role);

        verify(roleRepositoryMock).save(roleCaptor.capture());

        Role role = roleCaptor.getValue();

        assertEquals(1, role.getId());
        assertEquals("Admin", role.getName());
        assertEquals("manage everything", role.getDescription());
    }
}
