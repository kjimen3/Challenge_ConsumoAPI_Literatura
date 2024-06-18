package com.alura.challenge.libros.apiLibros.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
        @JsonAlias("title") String titulo,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("authors") List<DatosAutor> autoresLibro,
        @JsonAlias("download_count") Double numeroDeDescargas
) {

    @Override
    public String toString() {
        return "Los datos del libro son: " +
                " Titulo = '" + titulo + '\'' +
                ", Idiomas = " + idiomas +
                ", Autores del libro = " + autoresLibro +
                ", Numero de descargas = " + numeroDeDescargas +
                '}';
    }
}
