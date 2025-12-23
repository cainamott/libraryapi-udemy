package io.github.mottacaina.libraryapi.security;

import io.github.mottacaina.libraryapi.model.Usuario;
import io.github.mottacaina.libraryapi.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private UsuarioService usuarioService;

    public Usuario obterUsuarioLogado(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof CustomAuthentication customAuth){
            return ((CustomAuthentication) authentication).getUsuario();
        }

        return  null;
    }
}
