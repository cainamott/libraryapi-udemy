package io.github.mottacaina.libraryapi.dto;

import io.github.mottacaina.libraryapi.model.Autor;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;


public record AutorDTO(
        UUID id,
        @NotBlank(message = "campo obrigatório")
        @Size(min = 2, max = 100, message = "Campo fora do tamanho padrão")
        String nome,
        @NotNull(message = "campo obrigatório")
        @Past(message = "Não pode ser uma data futura")
        LocalDate dataNascimento,
        @NotBlank(message = "campo obrigatório")
        @Size(min = 2, max = 100, message = "Campo fora do tamanho padrão")
        String nacionalidade) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);

        return autor;

    }

}
