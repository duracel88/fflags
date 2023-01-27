package com.luk.fflags.infrastructure.configuration.security;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties
class ImportedUsers {
    List<ImportedUser> users;

    @Data
    static class ImportedUser {
        String username;
        String password;
        List<String> roles;

        UserDetails toUserDetails(PasswordEncoder passwordEncoder) {
            return User.withUsername(username)
                    .passwordEncoder(passwordEncoder::encode)
                    .password(password)
                    .roles(roles.toArray(new String[0]))
                    .build();
        }
    }

}
