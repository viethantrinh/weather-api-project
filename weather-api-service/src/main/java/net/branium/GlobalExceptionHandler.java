package net.branium;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGenericException(HttpServletRequest request, Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ErrorDTO.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errors(List.of(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .path(request.getServletPath())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequestException(HttpServletRequest request, Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ErrorDTO.builder()
                .timeStamp(LocalDateTime.now())
                .path(request.getServletPath())
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(List.of(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ErrorDTO error = ErrorDTO.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(((ServletWebRequest) request).getRequest().getServletPath())
                .errors(ex.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()))
                .build();
        LOGGER.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode status,
                                                                            WebRequest request) {
        ErrorDTO error = ErrorDTO.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(((ServletWebRequest) request).getRequest().getServletPath())
                .errors(Arrays.stream(Objects.requireNonNull(ex.getDetailMessageArguments())).map(Object::toString).toList())
                .build();
        LOGGER.error(ex.getMessage(), ex);

        Object[] detailMessageArguments = ex.getDetailMessageArguments();
        System.out.println(Arrays.toString(detailMessageArguments));
        return new ResponseEntity<>(error, headers, status);
    }
}
