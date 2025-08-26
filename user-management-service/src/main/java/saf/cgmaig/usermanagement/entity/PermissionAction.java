package saf.cgmaig.usermanagement.entity;

public enum PermissionAction {
    CREATE("Crear"),
    READ("Leer"),
    UPDATE("Actualizar"),
    DELETE("Eliminar"),
    EXECUTE("Ejecutar"),
    ADMIN("Administrar"),
    VIEW("Ver"),
    EXPORT("Exportar"),
    IMPORT("Importar"),
    APPROVE("Aprobar"),
    REJECT("Rechazar"),
    ASSIGN("Asignar");

    private final String descripcion;

    PermissionAction(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}