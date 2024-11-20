package mx.edu.utez.integradora.login.service.impl;

import lombok.AllArgsConstructor;
import mx.edu.utez.integradora.login.entity.SecurityUser;
import mx.edu.utez.integradora.login.entity.User;
import mx.edu.utez.integradora.login.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException(" Usuario no Encontrado");
        }
        return new SecurityUser(user);
    }
}
