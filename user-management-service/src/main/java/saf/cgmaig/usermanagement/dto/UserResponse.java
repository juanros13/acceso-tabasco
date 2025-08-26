package saf.cgmaig.usermanagement.dto;

import saf.cgmaig.usermanagement.entity.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserResponse {

    private String curp;
    private String nombreCompleto;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String emailPersonal;
    private String telefono;
    private String dependencia;
    private String puesto;
    private Integer nivelOrganizacional;
    private String jefeInmediatoCurp;
    private UserStatus statusUsuario;
    private LocalDate fechaIngreso;
    private LocalDate fechaBaja;
    private Boolean activo;
    private Boolean cuentaBloqueada;
    private LocalDateTime ultimoAcceso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<String> roles;
    private Boolean esJefe;
    private Integer cantidadSubordinados;

    // Constructores
    public UserResponse() {}

    // Getters y Setters
    public String getCurp() { return curp; }
    public void setCurp(String curp) { this.curp = curp; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

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

    public UserStatus getStatusUsuario() { return statusUsuario; }
    public void setStatusUsuario(UserStatus statusUsuario) { this.statusUsuario = statusUsuario; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDate getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(LocalDate fechaBaja) { this.fechaBaja = fechaBaja; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Boolean getCuentaBloqueada() { return cuentaBloqueada; }
    public void setCuentaBloqueada(Boolean cuentaBloqueada) { this.cuentaBloqueada = cuentaBloqueada; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public Boolean getEsJefe() { return esJefe; }
    public void setEsJefe(Boolean esJefe) { this.esJefe = esJefe; }

    public Integer getCantidadSubordinados() { return cantidadSubordinados; }
    public void setCantidadSubordinados(Integer cantidadSubordinados) { this.cantidadSubordinados = cantidadSubordinados; }
}