package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Estado {
    @NotNull(message = "el campo no debe estar vacio")
    private int IdEstado;

    
    private String Nombre;

    @NotNull(message = "Debe seleccionar un estado")
    @Valid
    public Pais PaisJPA = new Pais();
    
    public Estado(String Nombre){
        this.Nombre = Nombre;
        this.IdEstado = IdEstado;
    }

    public Estado() {
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(int IdEstado) {
        this.IdEstado = IdEstado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Pais getPaisJPA() {
        return PaisJPA;
    }

    public void setPaisJPA(Pais PaisJPA) {
        this.PaisJPA = PaisJPA;
    }


    
    
}
