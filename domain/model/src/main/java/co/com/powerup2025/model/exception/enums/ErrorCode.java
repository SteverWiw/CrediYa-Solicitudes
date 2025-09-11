package co.com.powerup2025.model.exception.enums;

import co.com.powerup2025.model.exception.gateways.IErrorCode;

public enum ErrorCode implements IErrorCode {

    // USUARIO
    USR_001("El usuario no fue encontrado", Severity.HIGH, ErrorModule.USUARIO),
    USR_002("El email ya está en uso", Severity.HIGH, ErrorModule.USUARIO),
    USR_003("El usuario no fue encontrado", Severity.HIGH, ErrorModule.USUARIO),
    USR_004("El usuario no tiene roles asociados", Severity.HIGH, ErrorModule.USUARIO),

    // VALIDACIONES
    VAL_001("El nombre es obligatorio", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_002("El apellido es obligatorio", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_003("El email es obligatorio", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_005("Documento inválido", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_006("El teléfono es obligatorio", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_007("El rol es obligatorio", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_008("El salario debe estar entre 0 y 15.000.000", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_009("El email debe tener formato válido", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_010("La contraseña es obligatoria", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_011("El monto es obligatorio", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_012("El plazo es obligatorio", Severity.MEDIUM, ErrorModule.VALIDACION),
    VAL_013("El tipo de préstamo no existe", Severity.MEDIUM, ErrorModule.VALIDACION),

    // SOLICITUDES
    SOL_001("El tipo de préstamo no existe", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    SOL_002("El monto es menor al posible para el tipo de préstamo", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    SOL_003("El monto es mayor al posible para el tipo de préstamo", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    SOL_004("El plazo es menor al posible para el tipo de préstamo", Severity.MEDIUM, ErrorModule.SOLICITUDES),
    SOL_005("El plazo es mayor al posible para el tipo de préstamo", Severity.MEDIUM, ErrorModule.SOLICITUDES),

    // AUTENTICACIÓN
    AUT_001("Credenciales inválidas", Severity.MEDIUM, ErrorModule.AUTH),
    AUT_002("Acceso denegado", Severity.MEDIUM, ErrorModule.AUTH),
    AUT_003("No autorizado", Severity.MEDIUM, ErrorModule.AUTH),

    // SISTEMA
    SYS_001("Error inesperado en el sistema", Severity.CRITICAL, ErrorModule.SISTEMA),
    SYS_002("Error al consumir servicio externo", Severity.CRITICAL, ErrorModule.SISTEMA),

    // INTEGRACIONES
    INT_001("Error inesperado en la integración con el microservicio de {module}", Severity.CRITICAL, ErrorModule.INTEGRACION),
    INT_002("Timeout al comunicarse con el microservicio de {module}", Severity.HIGH, ErrorModule.INTEGRACION),
    INT_003("No se pudo resolver la dirección del microservicio de {module}", Severity.HIGH, ErrorModule.INTEGRACION),
    INT_004("Respuesta inválida o malformada recibida desde el microservicio de {module}", Severity.MEDIUM, ErrorModule.INTEGRACION),
    INT_005("Error de autenticación/autorización al consumir el microservicio de {module}", Severity.CRITICAL, ErrorModule.INTEGRACION),
    INT_006("El microservicio de {module} devolvió estado HTTP 5xx (error en servidor)", Severity.HIGH, ErrorModule.INTEGRACION),
    INT_007("El microservicio de {module} devolvió estado HTTP 4xx (error de cliente)", Severity.MEDIUM, ErrorModule.INTEGRACION),
    INT_008("La integración con el microservicio de {module} fue rechazada por política de circuit breaker", Severity.MEDIUM, ErrorModule.INTEGRACION);

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
