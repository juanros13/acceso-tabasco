package saf.cgmaig.usermanagement.entity;

public enum UserStatus {
    ACTIVO("Activo"),
    INACTIVO("Inactivo"),
    SUSPENDIDO("Suspendido"),
    JUBILADO("Jubilado"),
    RENUNCIADO("Renunciado"),
    DESPEDIDO("Despedido");

    private final String descripcion;

    UserStatus(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}