package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.ML;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Pais {
    @NotNull(message = "el campo no debe estar vacio")
    private int IdPais;
    
    private String Nombre;
    
  
    public Pais(String Nombre){
        this.Nombre = Nombre;
        this.IdPais = IdPais;
    }

    public Pais() {
    }

    public int getIdPais() {
        return IdPais;
    }

    public void setIdPais(int IdPais) {
        this.IdPais = IdPais;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
    
}
