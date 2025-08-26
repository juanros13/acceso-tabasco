package saf.cgmaig.usermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre", "recurso"})
})
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 50, nullable = false)
    @NotBlank(message = "Nombre del permiso es obligatorio")
    @Size(max = 50, message = "Nombre del permiso no puede exceder 50 caracteres")
    private String nombre;

    @Column(name = "descripcion", length = 200)
    @Size(max = 200, message = "Descripción no puede exceder 200 caracteres")
    private String descripcion;

    @Column(name = "recurso", length = 100, nullable = false)
    @NotBlank(message = "Recurso es obligatorio")
    @Size(max = 100, message = "Recurso no puede exceder 100 caracteres")
    private String recurso;

    @Enumerated(EnumType.STRING)
    @Column(name = "accion", length = 20, nullable = false)
    private PermissionAction accion;

    @Column(name = "sistema", length = 50)
    @Size(max = 50, message = "Sistema no puede exceder 50 caracteres")
    private String sistema;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "creado_por", length = 18)
    private String creadoPor;

    @Column(name = "actualizado_por", length = 18)
    private String actualizadoPor;

    public Permission() {}

    public Permission(String nombre, String descripcion, String recurso, PermissionAction accion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.recurso = recurso;
        this.accion = accion;
    }

    public Permission(String nombre, String descripcion, String recurso, PermissionAction accion, String sistema) {
        this(nombre, descripcion, recurso, accion);
        this.sistema = sistema;
    }

    public String getPermisoCompleto() {
        return String.format("%s:%s:%s", sistema != null ? sistema : "SISTEMA", recurso, accion.name());
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRecurso() { return recurso; }
    public void setRecurso(String recurso) { this.recurso = recurso; }

    public PermissionAction getAccion() { return accion; }
    public void setAccion(PermissionAction accion) { this.accion = accion; }

    public String getSistema() { return sistema; }
    public void setSistema(String sistema) { this.sistema = sistema; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }
}