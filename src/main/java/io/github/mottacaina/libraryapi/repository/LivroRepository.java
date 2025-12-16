package io.github.mottacaina.libraryapi.repository;

import io.github.mottacaina.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID>{
}
