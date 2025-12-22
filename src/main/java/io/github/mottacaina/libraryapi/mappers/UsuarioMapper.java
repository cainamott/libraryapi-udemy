package io.github.mottacaina.libraryapi.mappers;

import io.github.mottacaina.libraryapi.dto.UsuarioDTO;
import io.github.mottacaina.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);

    UsuarioDTO toDto(Usuario usuario);
}
