package io.github.mottacaina.libraryapi.security;

import io.github.mottacaina.libraryapi.model.Usuario;
import io.github.mottacaina.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario user = userService.obterPorLogin(login);

        if(user == null){
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return User
                .builder()
                .username(user.getLogin())
                .password(user.getSenha())
                .roles(user.getRoles().toArray(new String[user.getRoles().size()]))
                .build();
    }
}
