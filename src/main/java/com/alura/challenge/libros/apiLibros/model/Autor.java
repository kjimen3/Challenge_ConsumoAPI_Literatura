package com.alura.challenge.libros.apiLibros.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
   private String nombre;
   private String fechaDeNacimiento;
    private String fechaDeFallecimiento;
    @ManyToOne
    private Libro libro;

   public Autor(){}

   public Autor (DatosAutor datosAutor){
       this.nombre = datosAutor.nombre();
       this.fechaDeNacimiento =  String.valueOf(datosAutor.fechaDeNacimiento());
       this.fechaDeFallecimiento = String.valueOf(datosAutor.fechaDeFallecimiento());

   }

    @Override
    public String toString() {
        return  "Nombre = '" + nombre + '\'' +
                ", Fecha de nacimiento = '" + fechaDeNacimiento + '\'' +
                ", Fecha de fallecimiento = '" + fechaDeFallecimiento + '\'' +
                '}';
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public String getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(String fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }
}
