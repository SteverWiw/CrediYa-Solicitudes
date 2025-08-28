package co.com.powerup2025.errorhelper.Exception;

import co.com.powerup2025.errorhelper.dto.ErrorResponse;

import java.util.List;

public class IntegrationException extends RuntimeException{
    private final List<ErrorResponse> errorResponses;

    public IntegrationException(List<ErrorResponse> errorResponses) {
        super("Error de integración con servicio externo");
        this.errorResponses = errorResponses;
    }

    @Override
    public String getMessage() {
        if (errorResponses != null && !errorResponses.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ErrorResponse er : errorResponses) {
                sb.append(String.format("[%s] %s (module: %s, severity: %s, traceId: %s)%n",
                        er.code(),
                        er.message(),
                        er.module(),
                        er.severity(),
                        er.traceId()));
            }
            return sb.toString();
        }
        return super.getMessage();
    }

}
