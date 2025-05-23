package com.niam.authserver.web.exception;

import com.niam.commonservice.utils.MessageUtil;
import com.niam.authserver.web.dto.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;

@ControllerAdvice
@AllArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageUtil messageUtil;

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        logger.info("handle Application exception");
        e.getLocalizedMessage();
        return ResponseEntity.internalServerError().body(
                ErrorResponse.builder()
                        .code(e.getCode())
                        .message(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({LoginAuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleLoginAuthenticationException(LoginAuthenticationException e) {
        logger.info("handle Authentication exception");
        e.getLocalizedMessage();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ErrorResponse.builder()
                        .code(e.getCode())
                        .message(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({UserMustChangePasswordException.class})
    public ResponseEntity<ErrorResponse> handleUserMustChangePasswordException(UserMustChangePasswordException e) {
        logger.error("User Must Change Password Exception with error message {}", e);
        return ResponseEntity.status(EXPECTATION_FAILED).body(
                ErrorResponse.builder()
                        .code(e.getCode())
                        .message(messageUtil.getMessage(e.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException e) {
        logger.error("handle InvalidToken exception with error message {}", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .code(e.getCode())
                        .message(messageUtil.getMessage(e.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Object> handleTokenException(TokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .code(String.valueOf(ex.getCode()))
                        .message(messageUtil.getMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.info("handle exception");
        e.getLocalizedMessage();
        return ResponseEntity.internalServerError().body(
                ErrorResponse.builder()
                        .code("500")
                        .message(e.getMessage())
                        .build()
        );
    }
}