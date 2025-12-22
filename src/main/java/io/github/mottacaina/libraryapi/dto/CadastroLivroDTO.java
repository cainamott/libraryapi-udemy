package io.github.mottacaina.libraryapi.dto;

import io.github.mottacaina.libraryapi.model.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(
        @NotBlank(message = "Campo obrigatório")
        @ISBN
        String isbn,
        @NotBlank(message = "Campo obrigatório")
        String titulo,
        @NotNull(message = "Campo obrigatório")
        @Past(message = "Não pode ser data futura")
        LocalDate dataPublicacao,
        GeneroLivro generoLivro,
        BigDecimal preco,
        @NotNull(message = "Campo obrigatório")
        UUID idAutor
    ) {
}
