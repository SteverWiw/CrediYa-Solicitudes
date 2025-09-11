package co.com.powerup2025.model.loans.enums;

public enum LoanState {

    PENDIENTE(1L, "Pendiente","La solicitud sera revisada por un analista en unos momentos"),
    APROBADA(2L, "Aprobada","La solicitud ha sido aprobada y está en proceso de desembolso"),
    RECHAZADA(3L, "Rechazada","La solicitud fue evaluada y no cumple con los requisitos"),
    REVISION(4L,"En revisión","La solicitud está siendo revisada por un analista");

    private final Long id;
    private final String descripcion;
    private final String message;

    LoanState(Long id, String descripcion, String message) {
        this.id = id;
        this.descripcion = descripcion;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getMessage() {
        return message;
    }

    public static String getEstado(Long id) {
        for (LoanState estado : values()) {
            if (estado.getId().equals(id)) {
                return estado.getDescripcion();
            }
        }
        return "Desconocido";
    }

    public static String getMessage(Long id) {
        for (LoanState estado : values()) {
            if (estado.getId().equals(id)) {
                return estado.getMessage();
            }
        }
        return "No message";
    }
}
