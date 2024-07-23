package com.billing.security;

import com.billing.entity.Role;
import com.billing.entity.User;
import com.billing.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("User logging in using: {} as username", username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            log.debug("User found with username {}", username);
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRoles()));
        } else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if (CollectionUtils.isEmpty(roles)) {
            return grantedAuthorityList;
        }
        Collection<? extends GrantedAuthority> allPermissions = roles.stream()
                .flatMap(role -> role.getPermissions().stream())  // Use flatMap
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());
        log.debug("User permissions are {}", allPermissions.stream().map(p -> p.getAuthority()).collect(Collectors.toList()));
        return allPermissions;
    }
}
