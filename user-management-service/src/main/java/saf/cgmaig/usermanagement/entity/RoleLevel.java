package saf.cgmaig.usermanagement.entity;

public enum RoleLevel {
    SUPER_ADMIN("Super Administrador"),
    ADMIN_GLOBAL("Administrador Global"),
    ADMIN_DEPENDENCIA("Administrador de Dependencia"),
    SUPERVISOR("Supervisor"),
    OPERATIVO("Operativo"),
    CONSULTA("Solo Consulta");

    private final String descripcion;

    RoleLevel(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean puedeAdministrar(RoleLevel otroNivel) {
        return this.ordinal() < otroNivel.ordinal();
    }
}