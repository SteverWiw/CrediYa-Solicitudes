package co.com.powerup2025.api.mapper;


import co.com.powerup2025.model.exception.gateways.iErrorCode;
import org.springframework.http.HttpStatus;

public class ErrorCodeMapper {

    public static HttpStatus mapToHttpStatus(iErrorCode errorCode) {
        String prefix = errorCode.code().substring(0, 3);

        return switch (prefix) {
            case "VAL" -> HttpStatus.BAD_REQUEST;
            case "USR" -> HttpStatus.NOT_FOUND;
            case "AUT" -> HttpStatus.UNAUTHORIZED;
            case "FOR" -> HttpStatus.FORBIDDEN;
            case "SYS" -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

}
