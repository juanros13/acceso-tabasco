package saf.cgmaig.usermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "curp", length = 18)
    @Pattern(regexp = "^[A-Z]{1}[AEIOUX]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[HM]{1}[A-Z]{2}[BCDFGHJKLMNPQRSTVWXYZ]{3}[0-9A-Z]{1}$",
            message = "CURP debe tener formato válido")
    private String curp;

    @Column(name = "nombres", length = 100, nullable = false)
    @NotBlank(message = "Nombres son obligatorios")
    @Size(max = 100, message = "Nombres no pueden exceder 100 caracteres")
    private String nombres;

    @Column(name = "apellido_paterno", length = 50)
    @Size(max = 50, message = "Apellido paterno no puede exceder 50 caracteres")
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 50)
    @Size(max = 50, message = "Apellido materno no puede exceder 50 caracteres")
    private String apellidoMaterno;

    @Column(name = "email", length = 100)
    @Email(message = "Email debe tener formato válido")
    @Size(max = 100, message = "Email no puede exceder 100 caracteres")
    private String email;

    @Column(name = "email_personal", length = 100)
    @Email(message = "Email personal debe tener formato válido")
    @Size(max = 100, message = "Email personal no puede exceder 100 caracteres")
    private String emailPersonal;

    @Column(name = "telefono", length = 15)
    @Pattern(regexp = "^[0-9+()-\\s]*$", message = "Teléfono debe contener solo números y símbolos válidos")
    private String telefono;

    @Column(name = "dependencia", length = 100)
    @Size(max = 100, message = "Dependencia no puede exceder 100 caracteres")
    private String dependencia;

    @Column(name = "puesto", length = 100)
    @Size(max = 100, message = "Puesto no puede exceder 100 caracteres")
    private String puesto;

    @Column(name = "nivel_organizacional")
    private Integer nivelOrganizacional;

    @Column(name = "jefe_inmediato_curp", length = 18)
    private String jefeInmediatoCurp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jefe_inmediato_curp", referencedColumnName = "curp", insertable = false, updatable = false)
    private User jefeInmediato;

    @OneToMany(mappedBy = "jefeInmediato", fetch = FetchType.LAZY)
    private Set<User> subordinados = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status_usuario", length = 20)
    private UserStatus statusUsuario = UserStatus.ACTIVO;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @Column(name = "motivo_baja", length = 200)
    private String motivoBaja;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_curp"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos = 0;

    @Column(name = "cuenta_bloqueada")
    private Boolean cuentaBloqueada = false;

    @Column(name = "fecha_bloqueo")
    private LocalDateTime fechaBloqueo;

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

    public User() {}

    public User(String curp, String nombres, String apellidoPaterno, String apellidoMaterno) {
        this.curp = curp;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombres);
        if (apellidoPaterno != null && !apellidoPaterno.trim().isEmpty()) {
            sb.append(" ").append(apellidoPaterno);
        }
        if (apellidoMaterno != null && !apellidoMaterno.trim().isEmpty()) {
            sb.append(" ").append(apellidoMaterno);
        }
        return sb.toString();
    }

    public boolean isJefe() {
        return subordinados != null && !subordinados.isEmpty();
    }

    public boolean tieneRol(String nombreRol) {
        return roles.stream().anyMatch(role -> role.getNombre().equals(nombreRol));
    }

    // Getters y Setters
    public String getCurp() { return curp; }
    public void setCurp(String curp) { this.curp = curp; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEmailPersonal() { return emailPersonal; }
    public void setEmailPersonal(String emailPersonal) { this.emailPersonal = emailPersonal; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDependencia() { return dependencia; }
    public void setDependencia(String dependencia) { this.dependencia = dependencia; }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }

    public Integer getNivelOrganizacional() { return nivelOrganizacional; }
    public void setNivelOrganizacional(Integer nivelOrganizacional) { this.nivelOrganizacional = nivelOrganizacional; }

    public String getJefeInmediatoCurp() { return jefeInmediatoCurp; }
    public void setJefeInmediatoCurp(String jefeInmediatoCurp) { this.jefeInmediatoCurp = jefeInmediatoCurp; }

    public User getJefeInmediato() { return jefeInmediato; }
    public void setJefeInmediato(User jefeInmediato) { this.jefeInmediato = jefeInmediato; }

    public Set<User> getSubordinados() { return subordinados; }
    public void setSubordinados(Set<User> subordinados) { this.subordinados = subordinados; }

    public UserStatus getStatusUsuario() { return statusUsuario; }
    public void setStatusUsuario(UserStatus statusUsuario) { this.statusUsuario = statusUsuario; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDate getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(LocalDate fechaBaja) { this.fechaBaja = fechaBaja; }

    public String getMotivoBaja() { return motivoBaja; }
    public void setMotivoBaja(String motivoBaja) { this.motivoBaja = motivoBaja; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    public Integer getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public Boolean getCuentaBloqueada() { return cuentaBloqueada; }
    public void setCuentaBloqueada(Boolean cuentaBloqueada) { this.cuentaBloqueada = cuentaBloqueada; }

    public LocalDateTime getFechaBloqueo() { return fechaBloqueo; }
    public void setFechaBloqueo(LocalDateTime fechaBloqueo) { this.fechaBloqueo = fechaBloqueo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }
}