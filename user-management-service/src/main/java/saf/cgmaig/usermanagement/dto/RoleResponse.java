package saf.cgmaig.usermanagement.dto;

import saf.cgmaig.usermanagement.entity.RoleLevel;

import java.time.LocalDateTime;

public class RoleResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private String dependencia;
    private RoleLevel nivel;
    private Boolean activo;
    private Integer cantidadUsuarios;
    private Integer cantidadPermisos;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // Constructores
    public RoleResponse() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDependencia() { return dependencia; }
    public void setDependencia(String dependencia) { this.dependencia = dependencia; }

    public RoleLevel getNivel() { return nivel; }
    public void setNivel(RoleLevel nivel) { this.nivel = nivel; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Integer getCantidadUsuarios() { return cantidadUsuarios; }
    public void setCantidadUsuarios(Integer cantidadUsuarios) { this.cantidadUsuarios = cantidadUsuarios; }

    public Integer getCantidadPermisos() { return cantidadPermisos; }
    public void setCantidadPermisos(Integer cantidadPermisos) { this.cantidadPermisos = cantidadPermisos; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}