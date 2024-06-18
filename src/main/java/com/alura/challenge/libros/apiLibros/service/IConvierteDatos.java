package com.alura.challenge.libros.apiLibros.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.lang.reflect.Array;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);

    <T, U> U convertir(T origen, Class<U> claseDestino);
}