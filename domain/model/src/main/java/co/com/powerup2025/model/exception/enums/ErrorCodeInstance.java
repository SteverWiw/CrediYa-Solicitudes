package co.com.powerup2025.model.exception.enums;

import co.com.powerup2025.model.exception.gateways.IErrorCode;

public record ErrorCodeInstance(ErrorCode base, String message) implements IErrorCode {
    @Override
    public String code() {
        return base.name();
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public ErrorModule module() {
        return base.module();
    }

    @Override
    public Severity severity() {
        return base.severity();
    }
}
