package nova.hackathon.util.exception;

import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import nova.hackathon.util.jwt.exception.JwtException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessBaseException.class)
    public ResponseEntity<ErrorResponse> handle(BusinessBaseException ex) {
        log.error("Member update error: {}", ex.getMessage());
        return createErrorResponseEntity(ex);
    }

    // Chapter : 내장 예외 처리
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        return createErrorResponseEntity(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "[MJS] 토큰이 유효하지 않거나 사용자에게 해당 접근 권한이 없습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument error: {}", ex.getMessage());
        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT", ex.getMessage());
    }
    // UUID가 유효하지 않을때 발생되는 에러
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == UUID.class) {
            String errorMessage = "잘못된 UUID 형식입니다: " + ex.getValue();
            log.error("잘못된 UUID 형식입니다: {}", errorMessage);
            return createErrorResponseEntity(HttpStatus.BAD_REQUEST, "INVALID_UUID", errorMessage);
        }
        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", ex.getMessage());
    }

    // 형식에 맞지 않는 요청
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());

        // 어떤 필드에서 검증 오류가 발생했는지 수집
        List<String> errorDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .toList();

        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", String.join(", ", errorDetails));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException ex) {
        log.error("MalformedJwtException error: {}", ex.getMessage());
        return createErrorResponseEntity(HttpStatus.UNAUTHORIZED, "WRONG_SIGNATURE_TOKEN", "[MJS] JWT 토큰이 변조되었거나 잘못된 형식입니다");
    }

    // 요청 HTTP 메서드 잘못됨
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException error: {}", ex.getMessage());
        return createErrorResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, "HTTP_METHOD_NOT_ALLOWED", ex.getMessage());
    }

    // 필수 헤더 누락
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException      ex) {
        log.error("MissingRequestHeaderException error: {}", ex.getMessage());
        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("Username not found error: {}", ex.getMessage());
        return createErrorResponseEntity(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ex.getMessage());
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error("Authentication error", e);
        return createErrorResponseEntity(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_ERROR", e.getMessage());
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException e) {
        log.error("No such algorithm exception", e);
        return createErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "NO_SUCH_ALGORITHM_ERROR", e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
        log.error("DataAccessException", e);
        return createErrorResponseEntity(HttpStatus.SERVICE_UNAVAILABLE, "DataAccessException", e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Exception: {}", ex.getMessage());
        return createErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ex.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
        log.warn("[MJS] {}", ex.getMessage());
        return createErrorResponseEntity(ex);
    }


    private ResponseEntity<ErrorResponse> createErrorResponseEntity(BusinessBaseException ex) {
        ErrorResponse errorResponse;

        if (ex.getMessage() != null && !ex.getMessage().equals(ex.getErrorCode().getMessage())) {
            errorResponse = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
        } else {
            errorResponse = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
        }

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status.value())
                .body(ErrorResponse.of(status, error, message));
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode, errorCode.getMessage()));
    }
}
