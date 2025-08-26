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
@Table(name = "roles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre", "dependencia"})
})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 50, nullable = false)
    @NotBlank(message = "Nombre del rol es obligatorio")
    @Size(max = 50, message = "Nombre del rol no puede exceder 50 caracteres")
    private String nombre;

    @Column(name = "descripcion", length = 200)
    @Size(max = 200, message = "Descripción no puede exceder 200 caracteres")
    private String descripcion;

    @Column(name = "dependencia", length = 100)
    @Size(max = 100, message = "Dependencia no puede exceder 100 caracteres")
    private String dependencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", length = 20)
    private RoleLevel nivel = RoleLevel.OPERATIVO;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

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

    public Role() {}

    public Role(String nombre, String descripcion, String dependencia) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dependencia = dependencia;
    }

    public void agregarPermiso(Permission permission) {
        this.permissions.add(permission);
        permission.getRoles().add(this);
    }

    public void removerPermiso(Permission permission) {
        this.permissions.remove(permission);
        permission.getRoles().remove(this);
    }

    public boolean tienePermiso(String nombrePermiso) {
        return permissions.stream()
                .anyMatch(permission -> permission.getNombre().equals(nombrePermiso));
    }

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

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }

    public Set<Permission> getPermissions() { return permissions; }
    public void setPermissions(Set<Permission> permissions) { this.permissions = permissions; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }
}