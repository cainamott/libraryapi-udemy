package io.github.mottacaina.libraryapi.dto;

import io.github.mottacaina.libraryapi.model.GeneroLivro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ResultadoPesquisaLivroDTO(
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        GeneroLivro generoLivro,
        BigDecimal preco,
        UUID idAutor,
        AutorDTO autorDTO) {
}
