package saf.cgmaig.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import saf.cgmaig.usermanagement.entity.RoleLevel;

public class RoleUpdateRequest {

    @NotBlank(message = "Nombre del rol es obligatorio")
    @Size(max = 50, message = "Nombre del rol no puede exceder 50 caracteres")
    private String nombre;

    @Size(max = 200, message = "Descripción no puede exceder 200 caracteres")
    private String descripcion;

    @NotBlank(message = "Dependencia es obligatoria")
    @Size(max = 100, message = "Dependencia no puede exceder 100 caracteres")
    private String dependencia;

    private RoleLevel nivel;

    // Constructores
    public RoleUpdateRequest() {}

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDependencia() { return dependencia; }
    public void setDependencia(String dependencia) { this.dependencia = dependencia; }

    public RoleLevel getNivel() { return nivel; }
    public void setNivel(RoleLevel nivel) { this.nivel = nivel; }
}