package org.example.apigateway.service;


import org.example.apigateway.feignClients.UsuarioAuthFeignClient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service

public class UserService implements UserDetailsService {

    private final UsuarioAuthFeignClient usuarioAuthFeignClient;

    public UserService(UsuarioAuthFeignClient usuarioAuthFeignClient) {
        this.usuarioAuthFeignClient = usuarioAuthFeignClient;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        // Llama a MS Usuario
        org.example.apigateway.entity.User user = usuarioAuthFeignClient.getUsuarioAuthData(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario " + username + " no encontrado en MS Usuario.");
        }

        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRol()));

        return new org.springframework.security.core.userdetails.User(
                user.getNombre(),
                user.getContrasenia(), // Contraseña ENCRIPTADA
                grantedAuthorities
        );
    }
}
