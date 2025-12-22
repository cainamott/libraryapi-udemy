package io.github.mottacaina.libraryapi.mappers;

import io.github.mottacaina.libraryapi.dto.CadastroLivroDTO;
import io.github.mottacaina.libraryapi.dto.ResultadoPesquisaLivroDTO;
import io.github.mottacaina.libraryapi.model.Autor;
import io.github.mottacaina.libraryapi.model.Livro;
import io.github.mottacaina.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    private AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java( autorRepository.findById(dto.idAutor()) )")
    public abstract Livro toEntity(CadastroLivroDTO livroDTO);


    public abstract ResultadoPesquisaLivroDTO toDto(Livro livro);
}
