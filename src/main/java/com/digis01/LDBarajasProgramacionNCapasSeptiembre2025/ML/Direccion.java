package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Direccion {
    private Integer IdDireccion;
    @NotBlank(message = "La calle es obligatoria")
    private String Calle;

    @NotBlank(message = "El número interior es obligatorio")
    private String NumeroInterior;

    @NotBlank(message = "El número exterior es obligatorio")
    private String NumeroExterior;

    @NotNull(message = "Debe seleccionar una colonia")
    @Valid
    public Colonia ColoniaJPA;
    
    public Direccion(String Calle, String NumeroInterior, String NumeroExterior){
//        this.IdDireccion = IdDireccion;
        this.Calle = Calle;
        this.NumeroInterior = NumeroInterior;
        this.NumeroExterior = NumeroExterior;
    }

    public Direccion() {
    }

    public Integer getIdDireccion() {
        return IdDireccion;
    }

    public void setIdDireccion(Integer IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }



    public Colonia getColoniaJPA() {
        return ColoniaJPA;
    }

    public void setColoniaJPA(Colonia ColoniaJPA) {
        this.ColoniaJPA = ColoniaJPA;
    }

    
    
    
    
}
