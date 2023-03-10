package shopme.user;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.repository.RoleRepository;
import com.example.shopmebe.repository.UserRepository;
import com.example.shopmebe.service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

//Predelat na service nazev tridy i testovat methody userService.methoda()....


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ShopmeBeApplication.class)
@Rollback(value = false)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private RoleRepository roleRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @InjectMocks
    private UserService userService;
    @Autowired
    private TestEntityManager entityManager;

    @Captor
    ArgumentCaptor<User> captorUser;

    private final static int ID = 1;
    private static User userAnna;
    private static User userJohn;


    @BeforeEach
    void setup() {
        captorUser = ArgumentCaptor.forClass(User.class);

        Role roleAdmin = Role.builder()
                             .id(1)
                             .name("Admin")
                             .description("manage everything").build();

        Role roleSalesperson = Role.builder()
                             .id(2)
                             .name("Salesperson")
                             .description("manage product price, customers, shipping, orders and sales report").build();

        Set<Role> annaRoles = new HashSet<>();
        annaRoles.add(roleAdmin);

        Set<Role> johnRoles = new HashSet<>();
        johnRoles.add(roleAdmin);
        johnRoles.add(roleSalesperson);

        userAnna = User.builder()
                       .id(1)
                       .email("anna@company.com")
                       .password("anna123")
                       .firstName("Anna")
                       .roles(annaRoles)
                       .enabled(false)
                       .lastName("Croft").build();

        userJohn = User.builder()
                       .id(2)
                       .email("john@company.com")
                       .password("john123")
                       .firstName("John")
                       .roles(johnRoles)
                       .enabled(true)
                       .lastName("Croft").build();
    }

    @Test
    void testCreateNewUserWithOneRole() {
        when(userRepositoryMock.save(userAnna)).thenReturn(userAnna);

        userService.save(userAnna);

        verify(userRepositoryMock).save(captorUser.capture());

        assertThat(captorUser.getValue().getId()).isPositive();
        assertEquals(userAnna, captorUser.getValue());
    }

    @Test
    void testCreateNewUserWithTwoRoles() {
        when(userRepositoryMock.save(userJohn)).thenReturn(userJohn);

        userService.save(userJohn);

        verify(userRepositoryMock).save(captorUser.capture());

        User user = captorUser.getValue();

        List<String> rolesName = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        assertThat(user.getId()).isPositive();
        assertEquals("John", user.getFirstName());
        assertEquals("Croft", user.getLastName());
        assertEquals(2, user.getRoles().size());

        List<String> expected = new ArrayList<>();
        expected.add("Admin");
        expected.add("Salesperson");
        assertEquals(expected, rolesName);
    }

    @Test
    void testListAllUsers() {
        when(userRepositoryMock.findAll(Sort.by("firstName").ascending())).thenReturn(List.of(userAnna, userJohn));

        List<User> users = userService.listAll();

        verify(userRepositoryMock).findAll(Sort.by("firstName").ascending());

        List<String> usersName = users.stream().map(User::getFirstName).collect(Collectors.toList());

        assertNotNull(users);
        assertEquals(2, users.size());

        List<String> expected = new ArrayList<>();
        expected.add("Anna");
        expected.add("John");
        assertEquals(expected, usersName);
    }

    @Test
    void testGetUserById() {
        when(userRepositoryMock.findById(ID)).thenReturn(Optional.of(userAnna));
        Optional<User> userById = userRepositoryMock.findById(ID);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(userRepositoryMock).findById(idCaptor.capture());

        Integer idValue = idCaptor.getValue();

        assertTrue(userById.isPresent());

        User user = userById.get();

        assertEquals(ID, idValue);
        assertEquals("Anna", user.getFirstName());
        assertEquals("Croft", user.getLastName());
        assertEquals(1, user.getRoles().size());
    }

    @Test
    void testUpdateUserDetails() {
        when(userRepositoryMock.findById(ID)).thenReturn(Optional.of(userAnna));

        User user = userRepositoryMock.findById(ID).get();
        user.setEnabled(true);
        user.setLastName("Smart");

        when(userRepositoryMock.save(user)).thenReturn(user);
        userRepositoryMock.save(user);
        verify(userRepositoryMock).save(captorUser.capture());

        User captorAnnaUser = captorUser.getValue();

        assertEquals(ID, user.getId());
        assertEquals("Smart", captorAnnaUser.getLastName());
        assertTrue(captorAnnaUser.isEnabled());
    }

    @Test
    void testUpdateUserRole() {
        when(userRepositoryMock.findById(ID)).thenReturn(Optional.of(userAnna));

        User newUserRole = userRepositoryMock.findById(ID).get();

        Role roleAdmin = entityManager.find(Role.class, 1);
        Role roleSalesperson = entityManager.find(Role.class, 2);

        newUserRole.getRoles().remove(roleAdmin);
        newUserRole.addRole(roleSalesperson);

        when(userRepositoryMock.save(newUserRole)).thenReturn(newUserRole);
        userRepositoryMock.save(newUserRole);
        verify(userRepositoryMock).save(captorUser.capture());

        User user = captorUser.getValue();

        assertEquals("Salesperson", getRoleName(user.getRoles()));
        assertEquals("Anna", user.getFirstName());
        assertEquals("Croft", user.getLastName());
        assertEquals(1, user.getRoles().size());
    }

    @Test
    void testDeleteUser() {
        userRepositoryMock.deleteById(ID);

        verify(userRepositoryMock).deleteById(eq(userAnna.getId()));
        Optional<User> userById = userRepositoryMock.findById(ID);

        assertThat(userById).isEmpty();
    }

    @Test
    void testGetUserByEmailNotFound() {
        String email = "test@company.com";

        when(userRepositoryMock.getUserByEmail(anyString())).thenThrow(new NullPointerException("Email not found!"));

        Assertions.assertThrows(NullPointerException.class, () -> userRepositoryMock.getUserByEmail(email));
    }

    @Test
    void testGetUserByEmail() {

        when(userRepositoryMock.getUserByEmail(anyString())).thenReturn(userAnna);
        userRepositoryMock.getUserByEmail(userAnna.getEmail());
        ArgumentCaptor<String> mail = ArgumentCaptor.forClass(String.class);
        verify(userRepositoryMock).getUserByEmail(mail.capture());

        assertThat(mail.getValue()).isNotNull();
        assertEquals(mail.getValue(), userAnna.getEmail());
    }

    @Test
    void testCountById() {

        when(userRepositoryMock.countById(anyInt())).thenReturn(anyLong());
        Long countById = userRepositoryMock.countById(ID);
        verify(userRepositoryMock, atLeastOnce()).countById(anyInt());

        assertThat(countById).isNotNull();
    }

    @Test
    @Transactional
    void testDisabledUser() {

//        when(userRepositoryMock.updateEnabledStatus(anyInt(), anyBoolean())).thenCallRealMethod();
//        doNothing().when(userRepositoryMock).updateEnabledStatus(anyInt(), anyBoolean());
//        doCallRealMethod().when(userRepositoryMock).updateEnabledStatus(anyInt(), anyBoolean());

        userRepositoryMock.updateEnabledStatus(2, false);
        verify(userRepositoryMock, times(1)).updateEnabledStatus(2, false);
//
        assertEquals(2, userJohn.getId());
//        assertFalse(userJohn.isEnabled());
    }

    @Test
    void testEnabledUser() {
//        userAnna.setEnabled(true);

        userRepositoryMock.updateEnabledStatus(ID, true);
        verify(userRepositoryMock).updateEnabledStatus(anyInt(), anyBoolean());
//
//        assertEquals(ID, userAnna.getId());
//        assertTrue(userAnna.isEnabled());
    }

    @Test
    void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        
        Page<User> page = new PageImpl<>(List.of(userAnna, userJohn));
        when(userRepositoryMock.findAll(pageable)).thenReturn(page);
        List<User> listUsers = userRepositoryMock.findAll(pageable).getContent();

        assertThat(listUsers).hasSize(pageSize);
    }

    @Test
    void testSearchUsers() {
        String keyword = "anna";

        int pageNumber = 0;
        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> page = new PageImpl<>(List.of(userAnna));
        when(userRepositoryMock.findByFullnameOrEmail(keyword, pageable)).thenReturn(page);
        List<User> listUsers = userRepositoryMock.findByFullnameOrEmail(keyword, pageable).getContent();

        assertThat(listUsers).hasSize(pageSize).isNotEmpty();
    }

    private String getRoleName(Set<Role> roles) {
        for (Role name : roles) {
            return name.getName();
        }

        return null;
    }
}
