package co.com.powerup2025.model.exception.enums;

import co.com.powerup2025.model.exception.gateways.iErrorCode;

public enum ErrorCode implements iErrorCode {

    USR_001("El usuario no fue encontrado", Severity.HIGH, ErrorModule.SOLICITUDES),
    // USR_002("El email ya esta en uso", Severity.HIGH, ErrorModule.SOLICITUDES),

    VAL_001("El nombre es obligatorio", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_002("El apellido es obligatorio", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_003("El email es obligatorio", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_005("Documento inválido", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_006("El teléfono es obligatorio", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_007("El rol es obligatorio", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_008("El salario debe estar entre 0 y 15.000.000", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_009("El email debe tener formato válido", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_010("El monto es obligatorio", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_011("El tipo de prestamo es obligatorio", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_012("El plazo es obligatorio", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    VAL_013("El tipo de prestamo no existe", Severity.MEDIUM, ErrorModule.SOLICITUDES),

    SOL_001("El tipo de prestamo no existe", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    SOL_002("El monto es mernor al posible para el tipo de prestamo", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    SOL_003("El monto es mayor al posible para el tipo de prestamo", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    SOL_004("El plazo es mernor al posible para el tipo de prestamo", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    SOL_005("El plazo es mayor al posible para el tipo de prestamo", Severity.MEDIUM, ErrorModule.SOLICITUDES),

    SYS_001("Error inesperado en el sistema", Severity.CRITICAL, ErrorModule.SOLICITUDES),
    SYS_OO2("Error al consumir servicio externo", Severity.CRITICAL, ErrorModule.SOLICITUDES),

    INT_001("Error inesperado en la integración con el microservicio de {module}", Severity.CRITICAL,
            ErrorModule.USUARIO),

    INT_002("Timeout al comunicarse con el microservicio de {module}", Severity.HIGH, ErrorModule.USUARIO),

    INT_003("No se pudo resolver la dirección del microservicio de {module}", Severity.HIGH, ErrorModule.USUARIO),

    INT_004("Respuesta inválida o malformada recibida desde el microservicio de {module}", Severity.MEDIUM,
            ErrorModule.USUARIO),

    INT_005("Error de autenticación/autorización al consumir el microservicio de {module}", Severity.CRITICAL,
            ErrorModule.USUARIO),

    INT_006("El microservicio de {module} devolvió estado HTTP 5xx (error en servidor)", Severity.HIGH,
            ErrorModule.USUARIO),

    INT_007("El microservicio de {module} devolvió estado HTTP 4xx (error de cliente)", Severity.MEDIUM,
            ErrorModule.USUARIO),

    INT_008("La integración con el microservicio de {module} fue rechazada por política de circuit breaker",
            Severity.MEDIUM, ErrorModule.USUARIO);

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
        return message.replace("{module}", module.name());
    }

    @Override
    public Severity severity() {
        return severity;
    }

    @Override
    public ErrorModule module() {
        return module;
    }

    public ErrorCodeInstance withModule(String moduleName) {
        return new ErrorCodeInstance(this, message.replace("{module}", moduleName));
    }

}