package io.github.mottacaina.libraryapi.controller;

import io.github.mottacaina.libraryapi.dto.CadastroLivroDTO;
import io.github.mottacaina.libraryapi.dto.ErroResposta;
import io.github.mottacaina.libraryapi.dto.ResultadoPesquisaLivroDTO;
import io.github.mottacaina.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.mottacaina.libraryapi.mappers.LivroMapper;
import io.github.mottacaina.libraryapi.model.GeneroLivro;
import io.github.mottacaina.libraryapi.model.Livro;
import io.github.mottacaina.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController implements GenericController {

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO) {

        Livro livro = livroMapper.toEntity(cadastroLivroDTO);
        livroService.salvar(livro);

        URI url = gerarHeaderLocation(livro.getId());

        return ResponseEntity.created(url).build();
    }

    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(
            @PathVariable("id") String id
    ){
             return livroService.obterPorId(UUID.fromString(id))
                    .map(livro -> {
                        ResultadoPesquisaLivroDTO dto = livroMapper.toDto(livro);
                        return ResponseEntity.ok(dto);
                    }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable String id){

        return livroService.obterPorId(UUID.fromString(id)).map(livro -> {
            livroService.deletar(livro);
            return  ResponseEntity.noContent().build();
        }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @GetMapping
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao ", required = false)
            Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10")
            Integer tamanhoPagina

    ){
        Page<Livro> paginaResultado = livroService.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao, pagina, tamanhoPagina);

        Page<ResultadoPesquisaLivroDTO> resultadoDto = paginaResultado.map(livroMapper::toDto);

        return ResponseEntity.ok(resultadoDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody @Valid CadastroLivroDTO cadastroLivroDTO){

        return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro entidadeAux = livroMapper.toEntity(cadastroLivroDTO);
                    livro.setDataPublicacao(entidadeAux.getDataPublicacao());
                    livro.setIsbn(entidadeAux.getIsbn());
                    livro.setTitulo(entidadeAux.getTitulo());
                    livro.setGenero(entidadeAux.getGenero());
                    livro.setPreco(entidadeAux.getPreco());
                    livro.setAutor(entidadeAux.getAutor());

                    livroService.atualizar(livro);

                    return ResponseEntity.noContent().build();

                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }
}
