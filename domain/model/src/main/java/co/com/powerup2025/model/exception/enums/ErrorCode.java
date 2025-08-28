package co.com.powerup2025.model.exception.enums;


import co.com.powerup2025.model.exception.gateways.iErrorCode;

public enum ErrorCode implements iErrorCode {

    USR_001("El usuario no fue encontrado", Severity.HIGH, ErrorModule.USUARIO),
    USR_002("El email ya esta en uso", Severity.HIGH, ErrorModule.USUARIO),

    VAL_001("El nombre es obligatorio", Severity.MEDIUM, ErrorModule.USUARIO),
    VAL_002("El apellido es obligatorio", Severity.MEDIUM, ErrorModule.USUARIO),
    VAL_003("El email es obligatorio", Severity.MEDIUM, ErrorModule.USUARIO),
    VAL_005("Documento inválido", Severity.MEDIUM, ErrorModule.USUARIO),
    VAL_006("El teléfono es obligatorio", Severity.MEDIUM, ErrorModule.USUARIO),
    VAL_007("El rol es obligatorio", Severity.MEDIUM, ErrorModule.USUARIO),
    VAL_008("El salario debe estar entre 0 y 15.000.000", Severity.MEDIUM, ErrorModule.USUARIO),
    VAL_009("El email debe tener formato válido", Severity.MEDIUM, ErrorModule.USUARIO),

    SYS_001("Error inesperado en el sistema", Severity.CRITICAL, ErrorModule.INFRAESTRUCTURA);

    private final String message;
    private final Severity severity;
    private final ErrorModule module;

    ErrorCode(String message, Severity severity, ErrorModule module) {
        this.message = message;
        this.severity = severity;
        this.module = module;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public Severity severity() {
        return severity;
    }

    @Override
    public ErrorModule module() {
        return module;
    }

}