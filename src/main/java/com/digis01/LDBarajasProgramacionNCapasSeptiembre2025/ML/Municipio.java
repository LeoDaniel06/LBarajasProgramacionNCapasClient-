package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Municipio {
    private int IdMunicipio;
    
    private String Nombre;

    @NotNull(message = "Debe seleccionar un estado")
    @Valid
    public Estado EstadoJPA = new Estado();

    public int getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(int IdMunicipio) {
        this.IdMunicipio = IdMunicipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Estado getEstadoJPA() {
        return EstadoJPA;
    }

    public void setEstado(Estado EstadoJPA) {
        this.EstadoJPA = EstadoJPA;
    }
    
    public Municipio(){
    }


    
}
