package co.com.powerup2025.model.exception.exceptions;


import co.com.powerup2025.model.exception.gateways.iErrorCode;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class BusinessException extends RuntimeException {

    private final List<iErrorCode> errorCodes;

    public BusinessException(iErrorCode errorCode) {
        super(errorCode.message());
        this.errorCodes = Collections.singletonList(errorCode);
    }

    public BusinessException(List<? extends iErrorCode> errorCodes) {
        super("Se encontraron múltiples errores de negocio");
        this.errorCodes = List.copyOf(errorCodes);

    }

    @Override
    public String getMessage() {
        if (errorCodes.size() == 1) {
            iErrorCode code = errorCodes.get(0);
            return String.format("[%s] %s (%s - %s)",
                    code.code(), code.message(), code.module(), code.severity());
        }

        return errorCodes.stream()
                .map(code -> String.format("[%s] %s (%s - %s)",
                        code.code(), code.message(), code.module(), code.severity()))
                .reduce((a, b) -> a + "; " + b)
                .orElse(super.getMessage());
    }

}
