package com.example.shopmebe.controller;


import com.example.shopmebe.exception.InvalidOldPasswordException;
import com.example.shopmebe.exception.UserNotFoundException;
import com.example.shopmebe.security.ShopmeUserDetails;
import com.example.shopmebe.service.RoleService;
import com.example.shopmebe.service.UserService;
import com.example.shopmebe.utils.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class AccountController {

    private final UserService userService;
    private final RoleService roleService;

    public AccountController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/profile-settings")
    @PreAuthorize("#loggedUser.username == authentication.principal.username")
    public String viewProfile(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model) {

        User user = userService.getUserByEmail(loggedUser.getUsername());
        List<Role> roles = roleService.listRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return "users/profile_account_form";
    }

    @PostMapping("/account/profile-info-update")
    @PreAuthorize("#loggedUser.username == authentication.principal.username")
    public String saveDetails(User user,
                              @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                              RedirectAttributes redirectAttributes,
                              @RequestParam("image") MultipartFile multipartFile) throws IOException, UserNotFoundException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            userService.updateAccount(user);
        }

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message", "Your account details have been updated");

        return "redirect:/profile-settings";
    }

    @GetMapping("/security-settings")
    @PreAuthorize("#loggedUser.username == authentication.principal.username")
    public String viewSecurity(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model) {
        User user = userService.getUserByEmail(loggedUser.getUsername());
        model.addAttribute("user", user);

        return "users/security_account_form";
    }

    @PostMapping("account/password-update")
    @PreAuthorize("#loggedUser.username == authentication.principal.username")
    public String changePassword(@Valid User user,
                                 @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                                 RedirectAttributes redirectAttributes,
                                 BindingResult bindingResult) throws UserNotFoundException, InvalidOldPasswordException {

        if (!bindingResult.hasErrors()) {
            if (userService.updatePassword(user, loggedUser)) {
                redirectAttributes.addFlashAttribute("message", "New password has been saved successfully");
                return "users/security_account_form";
            }
            redirectAttributes.addFlashAttribute("message", "test");
        }

        return "users/security_account_form";
    }
}
