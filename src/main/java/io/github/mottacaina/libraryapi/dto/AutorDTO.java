package io.github.mottacaina.libraryapi.dto;

import io.github.mottacaina.libraryapi.model.Autor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Autor")
public record AutorDTO(
        UUID id,
        @NotBlank(message = "campo obrigatório")
        @Size(min = 2, max = 100, message = "Campo fora do tamanho padrão")
        @Schema(name = "nome")
        String nome,
        @NotNull(message = "campo obrigatório")
        @Past(message = "Não pode ser uma data futura")
        @Schema(name = "data de nascimento")
        LocalDate dataNascimento,
        @NotBlank(message = "campo obrigatório")
        @Size(min = 2, max = 100, message = "Campo fora do tamanho padrão")
        @Schema(name = "nacionalidade")
        String nacionalidade) {

}
