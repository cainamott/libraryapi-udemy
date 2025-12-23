package io.github.mottacaina.libraryapi.controller;

import io.github.mottacaina.libraryapi.dto.UsuarioDTO;
import io.github.mottacaina.libraryapi.mappers.UsuarioMapper;
import io.github.mottacaina.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid UsuarioDTO dto){
        var usuario = usuarioMapper.toEntity(dto);
        usuarioService.salvar(usuario);
    }
}
