package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {
    private int idUsuario;
    
    @NotNull(message = "El campo no debe estar vacio")
    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El UserName solo permite letras y números")
    private String UserName;
    
    @NotNull(message = "El campo no debe estar vacio")
    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "^[A-ZÁÉÍÓÚÑ][a-záéíóúñA-ZÁÉÍÓÚÑ]*( [A-ZÁÉÍÓÚÑ][a-záéíóúñA-ZÁÉÍÓÚÑ]*)*$", message = "El nombre debe iniciar con mayúscula y contener solo letras")
    private String Nombre;
    
    @NotNull(message = "El campo no debe estar vacio")
    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "^[A-ZÁÉÍÓÚÑ][a-záéíóúñA-ZÁÉÍÓÚÑ]*( [A-ZÁÉÍÓÚÑ][a-záéíóúñA-ZÁÉÍÓÚÑ]*)*$", message = "El nombre debe iniciar con mayúscula y contener solo letras")
    private String ApellidoPat;
    
    @NotNull(message = "El campo no debe estar vacio")
    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "^[A-ZÁÉÍÓÚÑ][a-záéíóúñA-ZÁÉÍÓÚÑ]*( [A-ZÁÉÍÓÚÑ][a-záéíóúñA-ZÁÉÍÓÚÑ]*)*$", message = "El nombre debe iniciar con mayúscula y contener solo letras")
    private String ApellidoMat;
    
    @NotBlank(message = "Campo obligatorio")
    @Email(message = "Email inválido")
    private String Email;
    
    @NotNull(message = "El campo no debe estar vacio")
    @NotBlank(message = "Campo obligatorio")
    @Size(min = 8, max = 16, message = "Debe tener de 8 a 16 caracteres")
//    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,16}$", message = "Debe tener mayúscula, minúscula, número y caracter especial")
    private String Password;
    
    @NotNull(message = "El campo no debe estar vacio")
    @Past(message = "Debe ser una fecha anterior al día de hoy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date FechaNacimiento;
    
    @NotNull(message = "El campo no debe estar vacio")
    @NotBlank(message = "Campo obligatorio")
    private String Sexo;
    
    @NotNull(message = "El campo no debe estar vacio")
    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "^(\\+[0-9]{2,3}\\s*)*[0-9]{10}$", message = "El numero no es valido")
    private String Telefono;
    
    @NotNull(message = "El campo no debe estar vacio")
    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "^(\\+[0-9]{2,3}\\s*)*[0-9]{10}$", message = "El numero no es valido")
    private String Celular;
    
    @NotNull(message = "El campo no debe estar vacio")
    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "^[A-Z]{4}\\d{6}[HM][A-Z]{5}[0-9A-Z]\\d$", message = "CURP inválida")
    private String Curp;
    
    private String Imagen;

    private int Status;
    
    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String Imagen) {
        this.Imagen = Imagen;
    }
    
    @NotNull(message = "Debe seleccionar un rol")
    @Valid
    public Rol RolJPA;

    @Valid
    public List<Direccion> DireccionesJPA;
    
    

    public Usuario() { }

    public Usuario(String UserName, String Nombre, String ApellidoPat, String ApellidoMat,
                   String Email, String Password, Date FechaNacimiento, String Sexo,
                   String Telefono, String Celular, String Curp, Rol RolJPA) {
        this.UserName = UserName;
        this.Nombre = Nombre;
        this.ApellidoPat = ApellidoPat;
        this.ApellidoMat = ApellidoMat;
        this.Email = Email;
        this.Password = Password;
        this.FechaNacimiento = FechaNacimiento;
        this.Sexo = Sexo;
        this.Telefono = Telefono;
        this.Celular = Celular;
        this.Curp = Curp;
        this.RolJPA = RolJPA;
    }

    public int getIdUsuario() { 
        return idUsuario; 
    }
    public void setIdUsuario(int idUsuario) { 
        this.idUsuario = idUsuario; 
    }

    public String getUserName() { 
        return UserName; 
    }
    public void setUserName(String UserName) { 
        this.UserName = UserName; 
    }

    public String getNombre() { 
        return Nombre; 
    }
    public void setNombre(String Nombre) { 
        this.Nombre = Nombre; 
    }

    public String getApellidoPat() { 
        return ApellidoPat; 
    }
    public void setApellidoPat(String ApellidoPat) { 
        this.ApellidoPat =ApellidoPat; 
    }

    public String getApellidoMat() { 
        return ApellidoMat; 
    }
    public void setApellidoMat(String ApellidoMat) { 
        this.ApellidoMat = ApellidoMat; 
    }

    public String getEmail() { 
        return Email; 
    }
    public void setEmail(String Email) { 
        this.Email = Email; 
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public Date getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(Date FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public String getSexo() { 
        return Sexo; 
    }
    public void setSexo(String Sexo) { 
        this.Sexo = Sexo; 
    }

    public String getTelefono() { 
        return Telefono; 
    }
    public void setTelefono(String Telefono) { 
        this.Telefono = Telefono; 
    }

    public String getCelular() { 
        return Celular; 
    }
    public void setCelular(String Celular) { 
        this.Celular = Celular; 
    }

    public String getCurp() { 
        return Curp; 
    }
    public void setCurp(String Curp) { 
        this.Curp = Curp; 
    }

    public Rol getRolJPA() {
        return RolJPA;
    }

    public void setRolJPA(Rol RolJPA) {
        this.RolJPA = RolJPA;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    

    public List<Direccion> getDireccionesJPA() {
        return DireccionesJPA;
    }

    public void setDireccionesJPA(List<Direccion> DireccionesJPA) {
        this.DireccionesJPA = DireccionesJPA;
    }

    
    public void addDireccion(Direccion direccion) {
        if (DireccionesJPA == null) {
            DireccionesJPA = new ArrayList<>();
        }
        DireccionesJPA.add(direccion);
    }

    
}
