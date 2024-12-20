package com.example.shopmebe.user;

import com.example.shopmebe.exception.ConflictException;
import com.example.shopmebe.exception.InvalidOldPasswordException;
import com.example.shopmebe.exception.UserNotFoundException;
import com.example.shopmebe.security.ShopmeUserDetails;
import com.shopme.common.entity.User;
import com.shopme.common.request.CheckUniqueEmailRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    public static final int USER_PER_PAGE = 4;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listAll() {
        return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE, sort);

        if (keyword != null) {
            return  userRepository.findByFullnameOrEmail(keyword, pageable);
        }

        return userRepository.findAll(pageable);
    }

    @Transactional
    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).orElse(null);

            if (user.getPassword().isEmpty() && existingUser != null) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodedPassword(user);
            }
        } else {
            encodedPassword(user);
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateAccount(User loggedUser) throws UserNotFoundException {
        User existingUser = this.getUserById(loggedUser.getId());

        if (loggedUser.getPhotos() != null) {
            existingUser.setPhotos(loggedUser.getPhotos());
        }

        existingUser.setFirstName(loggedUser.getFirstName());
        existingUser.setLastName(loggedUser.getLastName());

        return userRepository.save(existingUser);
    }

    @Transactional
    public boolean updatePassword(User user, ShopmeUserDetails loggedUser) throws UserNotFoundException, InvalidOldPasswordException {
        User existingUser = this.getUserById(user.getId());

        if (existingUser != null && (checkIfValidOldPassword(user, loggedUser))) {
            existingUser.setPassword(user.getNewPassword());
            encodedPassword(existingUser);
            userRepository.save(existingUser);
            return true;
        }

        return false;
    }

    public boolean checkIfValidOldPassword(User user, ShopmeUserDetails loggedUser) throws InvalidOldPasswordException {
        if (!user.getPassword().equals(loggedUser.getPassword())) {
            throw new InvalidOldPasswordException("Invalid Old Password");
        }
        return user.getPassword().equals(loggedUser.getPassword());
    }

    private void encodedPassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public void isEmailUnique(CheckUniqueEmailRequest emailRequest) throws ConflictException {
        boolean isCreatingNew = (emailRequest.id() == null);

        Optional<User> userByEmail = userRepository.getUserByEmail(emailRequest.email());

        if (userByEmail.isPresent() && (isCreatingNew || !Objects.equals(userByEmail.get().getId(), emailRequest.id()))) {
            throw new ConflictException(String.format("There is another user having the email %s", emailRequest.email()));
        }
    }

    public User getUserById(Integer id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("Could not find any user with ID %d", id)));
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(IllegalArgumentException::new);
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == 0) {
            throw new UserNotFoundException(String.format("Could not find any user with ID %d", id));
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }
}
