package com.alura.challenge.libros.apiLibros.repository;

import com.alura.challenge.libros.apiLibros.model.Autor;
import com.alura.challenge.libros.apiLibros.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTitulo(String titulo);

    @Query("SELECT a FROM Libro l JOIN l.autores a WHERE a.nombre ILIKE %:nombreAutor%")
    List<Autor> autoresPorLibro(String nombreAutor);

    @Query("SELECT a FROM Autor a")
    List<Autor> listarTodosLosAutores();

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :ano AND (a.fechaDeFallecimiento IS NULL OR a.fechaDeFallecimiento >= :ano)")
    List<Autor> listarLosAutoresVivosEnDeterminadoAno(String ano);



}
