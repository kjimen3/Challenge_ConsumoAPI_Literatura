package com.alura.challenge.libros.apiLibros.principal;

import com.alura.challenge.libros.apiLibros.model.*;
import com.alura.challenge.libros.apiLibros.repository.LibroRepository;
import com.alura.challenge.libros.apiLibros.service.ConsumoAPI;
import com.alura.challenge.libros.apiLibros.service.ConvierteDatos;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books";
    private ConvierteDatos conversor = new ConvierteDatos();

    private List<DatosLibros> datosLibros = new ArrayList<>();

    private LibroRepository repositorio;

    private List<Libro> libros;
    public Principal(LibroRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void muestraElMenu() throws IOException {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    ---- BIENVENIDO A TU APLICACIÓN DE LIBROS ----
                    
                    Elije la opción a través de su número:
                    
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnDeterminadoAno();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void buscarLibroPorTitulo() throws IOException {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        System.out.println();
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("Tu libro fue encontrado: ");
            System.out.printf("""
                
                ------ LIBRO -------
                Titulo =  %s
                Idioma =  %s
                Autores =  %s
                Numero de descargas =  %s
                --------- // -------
                
                """,
                    libroBuscado.get().titulo(), libroBuscado.get().idiomas(), libroBuscado.get().autoresLibro(), libroBuscado.get().numeroDeDescargas());

            Optional<Libro> libroExistente = repositorio.findByTitulo(libroBuscado.get().titulo());

            if (libroExistente.isPresent()) {
                System.out.printf("""
                    -----------------------------------------
                    El libro existe en la base de datos.
                    -----------------------------------------
                    """);
            } else {
                Libro libro = new Libro(libroBuscado.get());
                var datosBuscados = libroBuscado.get();
                datosLibros.add(libroBuscado.get());

                List<Autor> autores = datosBuscados.autoresLibro().stream()
                        .map(a -> new Autor(new DatosAutor(a.nombre(), a.fechaDeNacimiento(), a.fechaDeFallecimiento())))
                        .collect(Collectors.toList());

                libro.setAutores(autores);
                repositorio.save(libro);
                System.out.printf("""
                    -----------------------------------------
                    El libro fue guardado en la base de datos.
                    -----------------------------------------
                    """);
            }
        } else {
            System.out.printf("""
                    -----------------------------------------
                    Lo siento, libro no encontrado,
                    intenta nuevamente con otro libro. :)
                    -----------------------------------------
                    """);
        }
    }


    private void listarLibrosRegistrados() {
        libros = repositorio.findAll();
        libros.forEach(libro -> System.out.printf("""
                    
                    ------ LIBRO -------
                    Titulo: %s
                    Idioma: %s
                    Autores: %s
                    Numero de descargas: %s
                    ---------//------------
                    
                    """, libro.getTitulo(), libro.getIdiomas(), libro.getAutores(), libro.getNumeroDeDescargas()));
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = repositorio.listarTodosLosAutores();
        autores.forEach(autor -> System.out.printf("""
                    
                    ------ AUTOR -------
                    Autor: %s
                    Fecha de nacimiento: %s
                    Fecha de fallecimiento: %s
                    ---------//------------
                    
                    """, autor.getNombre(), autor.getFechaDeNacimiento(), autor.getFechaDeFallecimiento()));
    }

    private void listarAutoresVivosEnDeterminadoAno() {
        System.out.println("Escribe el año del cual quieres ver los autores vivos:");
        var ano = teclado.nextLine();

        List<Autor> autorNacido = repositorio.listarLosAutoresVivosEnDeterminadoAno(ano);

        if (autorNacido.isEmpty()) {
            System.out.printf("""
                    ----------------------------------------------------------------------------------------------
                    Lo siento, no se encontraron autores en el año indicado o los datos ingresados son incorrectos.
                    Intenta nuevamente con otro año. :)
                    -----------------------------------------------------------------------------------------------
                    """);
        } else {
            autorNacido.forEach(a ->
                    System.out.printf("""
                
                ------ AUTOR -------
                Nombre =  %s
                Fecha de nacimiento =  %s
                --------- // -------
                
                """, a.getNombre(), a.getFechaDeNacimiento()));
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                    ¿En que idioma quieres encontrar tus libros?
                    Escribe tu opción por medio de su abreviatura, ejm. 'fr' si quieres buscar libros en francés.
                    
                    es - español
                    en - inglés
                    fr - francés
                    pt - portugués
                    
                    """);
        String idioma = teclado.nextLine();
        List<Libro> libros = repositorio.findAll();
        List<Libro> librosFiltrados = libros.stream()
                .filter(libro -> libro.getIdiomas().contains(idioma))
                .collect(Collectors.toList());
        if (librosFiltrados.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
        } else {
            librosFiltrados.forEach(libro -> System.out.printf("""
                    
                    ------ LIBRO -------
                    Titulo: %s
                    Idioma: %s
                    Autores: %s
                    Numero de descargas: %s
                    ---------//------------
                    
                    """, libro.getTitulo(), libro.getIdiomas(), libro.getAutores(), libro.getNumeroDeDescargas()));
        }
    }
}