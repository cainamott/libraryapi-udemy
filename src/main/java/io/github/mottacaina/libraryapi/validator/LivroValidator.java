package io.github.mottacaina.libraryapi.validator;

import io.github.mottacaina.libraryapi.exceptions.CampoInvalidoException;
import io.github.mottacaina.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.mottacaina.libraryapi.model.Livro;
import io.github.mottacaina.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {

    private final LivroRepository livroRepository;
    private static final int AND_EXIGENCIA_PRECO = 2020;

    public void validar(Livro livro){
        if(existeLivroComIsbn(livro)){
            throw new RegistroDuplicadoException("Livro não pode ter ISBN duplicado");
        }
        if(isPrecoObrigatorioNulo(livro)){
            throw new CampoInvalidoException("preço", "Preço obrigatório para livros com publicação >= 2020");
        }
    }

    private boolean isPrecoObrigatorioNulo(Livro livro) {

        return livro.getPreco() == null && livro.getDataPublicacao().getYear() >= AND_EXIGENCIA_PRECO;
    }


    public boolean existeLivroComIsbn(Livro livro){

        Optional<Livro> livroExiste = livroRepository.findByIsbn(livro.getIsbn());

        if(livro.getId() == null){
            return livroExiste.isPresent();
        }

        return livroExiste
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livro.getIsbn()));
    }



}
