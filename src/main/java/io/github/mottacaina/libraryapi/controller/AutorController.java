package io.github.mottacaina.libraryapi.controller;

import io.github.mottacaina.libraryapi.dto.AutorDTO;
import io.github.mottacaina.libraryapi.dto.ErroResposta;
import io.github.mottacaina.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.mottacaina.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.mottacaina.libraryapi.model.Autor;
import io.github.mottacaina.libraryapi.repository.AutorRepository;
import io.github.mottacaina.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping

    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autorDTO){
        try {
            var autorEntity = autorDTO.mapearParaAutor();
            autorService.salvar(autorEntity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/{id}")
                    .buildAndExpand(autorEntity.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        }
        catch(RegistroDuplicadoException e){
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if(autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(
                    autor.getId(),
                    autor.getNome(),
                    autor.getDataNascimento(),
                    autor.getNacionalidade()
            );
            return ResponseEntity.ok(dto);
        }
        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id){
        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            autorService.deletar(autorOptional.get());
            return ResponseEntity.noContent().build();
        } catch(OperacaoNaoPermitidaException e){

            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisar(@RequestParam(value ="nome", required = false) String nome ,
                                                    @RequestParam(value = "nacionalidade", required = false) String nacionalidade){

        List<Autor> lista = autorService.pesquisa(nome, nacionalidade);
        List<AutorDTO> listaDTO = lista
                .stream()
                .map(x -> new AutorDTO(
                        x.getId(), x.getNome(),
                        x.getDataNascimento(),
                        x.getNacionalidade()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(listaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id,
            @RequestBody AutorDTO dto)
    {
        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autor = autorOptional.get();
            autor.setNome(dto.nome());
            autor.setNacionalidade(dto.nacionalidade());
            autor.setDataNascimento(dto.dataNascimento());

            autorService.atualizar(autor);

            return ResponseEntity.noContent().build();
        }catch (RegistroDuplicadoException e){
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }
}
