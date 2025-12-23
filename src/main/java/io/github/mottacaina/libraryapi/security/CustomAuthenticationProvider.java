package io.github.mottacaina.libraryapi.security;

import io.github.mottacaina.libraryapi.model.Usuario;
import io.github.mottacaina.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String senhaDigitada = authentication.getCredentials().toString();

        Usuario usuarioEncontrado = usuarioService.obterPorLogin(login);

        if(usuarioEncontrado == null){

            throw getUsuarioNaoEncontrado();
        }

        String senhaEncontrada = usuarioEncontrado.getSenha();

        boolean senhasBatem = passwordEncoder.matches(senhaDigitada, senhaEncontrada);

        if(senhasBatem){
            return new CustomAuthentication(usuarioEncontrado);
        }

        throw getUsuarioNaoEncontrado();
    }

    private UsernameNotFoundException getUsuarioNaoEncontrado() {
        return new UsernameNotFoundException("Usuario não encontrado");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }


}
