package io.github.mottacaina.libraryapi.service;

import io.github.mottacaina.libraryapi.dto.AutorDTO;
import io.github.mottacaina.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.mottacaina.libraryapi.model.Autor;
import io.github.mottacaina.libraryapi.repository.AutorRepository;
import io.github.mottacaina.libraryapi.repository.LivroRepository;
import io.github.mottacaina.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorValidator autorValidator;
    private final LivroRepository livroRepository;

    public Autor salvar(Autor autor){

        autorValidator.validar(autor);
        return autorRepository.save(autor);
    }

    public void atualizar(Autor autor){

        if(autor.getId() == null){
            throw new IllegalArgumentException("Autor não encontrado");
        }
        autorValidator.validar(autor);
        autorRepository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID id){
        return autorRepository.findById(id);
    }

    public void deletar(Autor autor){
        if(possuiLivro(autor)){
            throw new OperacaoNaoPermitidaException("Autor possui livros cadastrados");
        }
        autorRepository.delete(autor);
    }

    public List<Autor> pesquisa(String nome, String nacionalidade){
        if(nome != null && nacionalidade != null){

            return autorRepository.findByNomeAndNacionalidade(nome, nacionalidade);
        }
        if(nome != null){

            return autorRepository.findByNome(nome);
        }
        if(nacionalidade != null){

            return autorRepository.findByNacionalidade(nacionalidade);
        }

        return autorRepository.findAll();
    }

    public boolean possuiLivro(Autor autor){

       return livroRepository.existsByAutor(autor);
    }
}
