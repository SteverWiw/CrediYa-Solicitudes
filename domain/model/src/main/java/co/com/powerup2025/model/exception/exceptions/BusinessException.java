package co.com.powerup2025.model.exception.exceptions;

import java.util.Collections;
import java.util.List;

import co.com.powerup2025.model.exception.gateways.iErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final List<iErrorCode> errorCodes;

    public BusinessException(iErrorCode errorCode) {
        super("Business error");
        this.errorCodes = Collections.singletonList(errorCode);
    }

    public BusinessException(List<? extends iErrorCode> errorCodes) {
        super("Se presentaron " + errorCodes.size() + " Errores de negocio");
        this.errorCodes = List.copyOf(errorCodes);
    }

    @Override
    public String toString() {
        if (errorCodes == null || errorCodes.isEmpty()) {
            return super.toString();
        }

        return errorCodes.stream()
                .map(code -> String.format("[%s] %s (%s - %s)",
                        code.code(), code.message(), code.module(), code.severity()))
                .reduce((a, b) -> a + "; " + b)
                .orElse(super.toString());
    }
}
