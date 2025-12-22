package io.github.mottacaina.libraryapi.service;

import io.github.mottacaina.libraryapi.model.GeneroLivro;
import io.github.mottacaina.libraryapi.model.Livro;
import io.github.mottacaina.libraryapi.model.Usuario;
import io.github.mottacaina.libraryapi.repository.LivroRepository;
import io.github.mottacaina.libraryapi.repository.specs.LivroSpecs;
import io.github.mottacaina.libraryapi.security.SecurityService;
import io.github.mottacaina.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.github.mottacaina.libraryapi.repository.specs.LivroSpecs.*;

import static io.github.mottacaina.libraryapi.repository.specs.LivroSpecs.*;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroValidator livroValidator;
    private final SecurityService securityService;

    public void salvar(Livro livro){
        livroValidator.validar(livro);
        Usuario usuario = securityService.obterUsuarioLogado();
        livro.setUsuario(usuario);
        livroRepository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id){

        return livroRepository.findById(id);
    }

    public void deletar(Livro livro){

        livroRepository.delete(livro);
    }

    public Page<Livro> pesquisa (String isbn, String titulo, String nomeAutor, GeneroLivro generoLivro, Integer anoPublicacao, Integer pagina, Integer tamanhoPagina){

//        Specification<Livro> specs = Specification
//                .where(LivroSpecs.isbnEqual(isbn))
//                .and(LivroSpecs.tituloLike(titulo))
//                .and(LivroSpecs.generoEqual(generoLivro));

        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if(isbn != null){
            specs = specs.and(isbnEqual(isbn));
        }
        if(titulo != null){
            specs = specs.and(tituloLike(titulo));
        }
        if(generoLivro != null){
            specs = specs.and(generoEquals(generoLivro));
        }
        if(anoPublicacao != null){
            specs = specs.and(anoPublicacaoEqual(anoPublicacao));
        }
        if(nomeAutor != null){
            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        Pageable pageRequest =  PageRequest.of(pagina, tamanhoPagina);
        return livroRepository.findAll(specs, pageRequest);

    }

    public void atualizar(Livro livro){
        if(livro.getId() == null){
            throw new IllegalArgumentException("Livro não encontrado");
        }
        livroValidator.validar(livro);
        livroRepository.save(livro);
    }

}
