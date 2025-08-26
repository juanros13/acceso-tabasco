package saf.cgmaig.usermanagement.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class UserCreateRequest {

    @NotBlank(message = "CURP es obligatorio")
    @Pattern(regexp = "^[A-Z]{1}[AEIOUX]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[HM]{1}[A-Z]{2}[BCDFGHJKLMNPQRSTVWXYZ]{3}[0-9A-Z]{1}$",
            message = "CURP debe tener formato válido")
    private String curp;

    @NotBlank(message = "Nombres son obligatorios")
    @Size(max = 100, message = "Nombres no pueden exceder 100 caracteres")
    private String nombres;

    @Size(max = 50, message = "Apellido paterno no puede exceder 50 caracteres")
    private String apellidoPaterno;

    @Size(max = 50, message = "Apellido materno no puede exceder 50 caracteres")
    private String apellidoMaterno;

    @Email(message = "Email debe tener formato válido")
    @Size(max = 100, message = "Email no puede exceder 100 caracteres")
    private String email;

    @Email(message = "Email personal debe tener formato válido")
    @Size(max = 100, message = "Email personal no puede exceder 100 caracteres")
    private String emailPersonal;

    @Pattern(regexp = "^[0-9+()-\\s]*$", message = "Teléfono debe contener solo números y símbolos válidos")
    private String telefono;

    @NotBlank(message = "Dependencia es obligatoria")
    @Size(max = 100, message = "Dependencia no puede exceder 100 caracteres")
    private String dependencia;

    @Size(max = 100, message = "Puesto no puede exceder 100 caracteres")
    private String puesto;

    @Min(value = 1, message = "Nivel organizacional debe ser mayor a 0")
    @Max(value = 10, message = "Nivel organizacional no puede ser mayor a 10")
    private Integer nivelOrganizacional;

    @Pattern(regexp = "^[A-Z]{1}[AEIOUX]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[HM]{1}[A-Z]{2}[BCDFGHJKLMNPQRSTVWXYZ]{3}[0-9A-Z]{1}$",
            message = "CURP del jefe debe tener formato válido")
    private String jefeInmediatoCurp;

    private LocalDate fechaIngreso;

    // Constructores
    public UserCreateRequest() {}

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

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
}