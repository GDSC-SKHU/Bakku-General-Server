package com.gdsc.bakku.auth.service;

import com.gdsc.bakku.auth.domain.entity.Role;
import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.auth.domain.repo.UserRepository;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(FirebaseToken firebaseToken, Role... roles) {
        User user = User.builder()
                .username(firebaseToken.getUid())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .email(firebaseToken.getEmail())
                .name(firebaseToken.getName())
                .picture(firebaseToken.getPicture())
                .build();

        user.addRoles(roles);

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("해당 유저(%s)를 찾을 수 없습니다.", username)));
    }
}
