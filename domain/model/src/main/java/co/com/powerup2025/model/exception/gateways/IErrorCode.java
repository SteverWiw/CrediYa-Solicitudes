package co.com.powerup2025.model.exception.gateways;

import co.com.powerup2025.model.exception.enums.ErrorModule;
import co.com.powerup2025.model.exception.enums.Severity;

public interface IErrorCode {
    String code();

    String message();

    Severity severity();

    ErrorModule module();

}
