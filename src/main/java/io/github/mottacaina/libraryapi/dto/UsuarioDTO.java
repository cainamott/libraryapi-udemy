package io.github.mottacaina.libraryapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UsuarioDTO(
                         @NotBlank(message = "Login obrigatório")
                         String login,
                         @NotBlank(message = "Senha obrigatória")
                         String senha,
                         @Email(message = "Email inválido") String email,
                         List<String> roles) {
}
