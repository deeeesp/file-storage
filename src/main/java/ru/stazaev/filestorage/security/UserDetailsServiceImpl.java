package ru.stazaev.filestorage.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.stazaev.filestorage.entity.User;
import ru.stazaev.filestorage.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("Имя пользователя не найдено"));
//        if (!user.getStatus().equals(Status.ACTIVE)) {
//            throw new UsernameNotFoundException("Пользователь заблокирован");
//        }
        String authority = user.getRole().getAuthority();
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), simpleGrantedAuthorities);
    }
}
