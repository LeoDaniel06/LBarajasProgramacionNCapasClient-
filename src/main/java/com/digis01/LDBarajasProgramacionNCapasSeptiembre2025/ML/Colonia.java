package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Colonia {
    
    private int IdColonia;
    
    private String Nombre;
    
    private String CodigoPostal;

    @NotNull(message = "Debe seleccionar un municipio")
    @Valid
    public Municipio MunicipioJPA = new Municipio();
    
    
    public Colonia(String Nombre, String CodigoPostal){
        this.IdColonia = IdColonia;
        this.Nombre = Nombre;
        this.CodigoPostal = CodigoPostal;
    }

    public Colonia() {
    }

    public int getIdColonia() {
        return IdColonia;
    }

    public void setIdColonia(int IdColonia) {
        this.IdColonia = IdColonia;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String CodigoPostal) {
        this.CodigoPostal = CodigoPostal;
    }    

    public void setMunicipioJPA(Municipio MunicipioJPA) {
        this.MunicipioJPA = MunicipioJPA;
    }

    public Municipio getMunicipioJPA() {
        return MunicipioJPA;
    }


    
}
