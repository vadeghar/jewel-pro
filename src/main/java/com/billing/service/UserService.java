package com.billing.service;


import com.billing.dto.UserDto;
import com.billing.entity.Role;
import com.billing.entity.User;
import com.billing.repository.RoleRepository;
import com.billing.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
//        User user = new User();
//        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
//        user.setEmail(userDto.getEmail());
//        user.setUsername(userDto.getUsername());
        // encrypt the password using spring security
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder.encode("password"));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

//        Optional<Role> roleOpt = roleRepository.findByName("ROLE_ADMIN");
//        Role role = null;
//        if (!roleOpt.isPresent()) {
//            role = checkRoleExist();
//        } else {
//            role = roleOpt.get();
//        }
//        user.setRoles(Set.of(role));
        User user1 = userRepository.save(user);
        return user1;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            log.info("Current logged in user: {}", currentUserName);
            return currentUserName != null;
        }
        return false;
    }

    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

//    public User saveUser(User user) {
//        return userRepository.save(user);
//    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        //user.setPassword(userDetails.getPassword());
        if (StringUtils.isNotBlank(userDetails.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setRoles(userDetails.getRoles());
        return userRepository.save(user);
    }










    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    public boolean isUserIsAdmin() {
        Authentication authentication = SecurityContextHolder.getContext() != null ? SecurityContextHolder.getContext().getAuthentication() : null;
        if (null != authentication && authentication.isAuthenticated()) {
            List<? extends GrantedAuthority> admin = authentication.getAuthorities().stream()
                    .filter(grantedAuthority -> grantedAuthority.getAuthority().contains("ADMIN"))
                    .collect(Collectors.toList());
            return admin.size() > 0;
        }
        return false;
    }

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext() != null ? SecurityContextHolder.getContext().getAuthentication() : null;
        if (null != authentication && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "System";
    }

}
