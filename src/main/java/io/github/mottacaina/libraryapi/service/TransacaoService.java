package io.github.mottacaina.libraryapi.service;

import io.github.mottacaina.libraryapi.model.Autor;
import io.github.mottacaina.libraryapi.model.Livro;
import io.github.mottacaina.libraryapi.repository.AutorRepository;
import io.github.mottacaina.libraryapi.repository.LivroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public void atualizacaoSemAtualizar(){

        var livro = livroRepository.findById(UUID.fromString("")).orElse(null);

        livro.setDataPublicacao(LocalDate.of(3034, 6, 1));

        livroRepository.save(livro);
    }

    @Transactional
    public void executar(){

        Autor autor = new Autor();
        autor.setNome("Cainã");
        autor.setDataNascimento(LocalDate.of(2005, 8, 31));
        autor.setNacionalidade("Brasileira");

        autorRepository.save(autor);

        Livro livro = new Livro();
        livro.setTitulo("Livro 1");
        livro.setIsbn("32742");
        livro.setPreco(BigDecimal.valueOf(260));
        livro.setTitulo("Titulo 1");
        livro.setDataPublicacao(LocalDate.of(2000, 6, 6));

        livro.setAutor(autor);

        livroRepository.save(livro);

        if(autor.getNome().equals("Cainã")){
            throw new RuntimeException("Rollback");
        }

    }
}
