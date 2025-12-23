package io.github.mottacaina.libraryapi.security;

import io.github.mottacaina.libraryapi.model.Usuario;
import io.github.mottacaina.libraryapi.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String SENHA_PADRAO = "senhaP123";

    private final UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);

        OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = auth2AuthenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        Usuario usuario = usuarioService.obterPorEmail(email);

        if(usuario == null){
            usuario = cadastrarUsuario(email);
        }

        CustomAuthentication customAuthentication = new CustomAuthentication(usuario);

        SecurityContextHolder.getContext().setAuthentication(customAuthentication);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private Usuario cadastrarUsuario(String email) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setLogin(obterLoginAtravesDoEmail(email));
        usuario.setSenha(SENHA_PADRAO);
        usuario.setRoles(List.of("OPERADOR"));
        usuarioService.salvar(usuario);
        return usuario;
    }

    private String obterLoginAtravesDoEmail(String email) {

        return email.substring(0, email.indexOf("@"));
    }

}
