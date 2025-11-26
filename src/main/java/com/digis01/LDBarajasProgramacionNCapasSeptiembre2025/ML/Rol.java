package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.ML;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Rol {

    @NotNull(message = "Debe seleccionar un Id de Rol")
    private Integer IdRol;

    private String Nombre;

    public Rol() { }

    public Rol(Integer IdRol, String Nombre) {
        this.IdRol = IdRol;
        this.Nombre = Nombre;
    }

    public Integer getIdRol() {
        return IdRol;
    }

    public void setIdRol(Integer IdRol) {
        this.IdRol = IdRol;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
}