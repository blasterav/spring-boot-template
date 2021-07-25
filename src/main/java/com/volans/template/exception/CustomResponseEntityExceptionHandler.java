package com.volans.template.exception;

import com.volans.template.model.Response;
import com.volans.template.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import static com.volans.template.model.StatusConstants.HttpConstants;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Response<?>> handleInvalidRequestException(InvalidRequestException e, HttpServletRequest request) {
        Object servletPath = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern");
        LOG.error("Failed {} {}: {}", request.getMethod(), servletPath, e.getStatus().getDesc());
        return new ResponseEntity<>(new Response<>(new Status(e.getStatus()), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<?>> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        Object servletPath = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern");
        LOG.error("Failed {} {}: {}", request.getMethod(), servletPath, e.getStatus().getDesc());
        return new ResponseEntity<>(new Response<>(new Status(e.getStatus()), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Response<?>> handleServiceException(ServiceException e, HttpServletRequest request) {
        Object servletPath = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern");
        LOG.error("Failed {} {}: {}", request.getMethod(), servletPath, e.getStatus().getDesc());
        return new ResponseEntity<>(new Response<>(new Status(e.getStatus()), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Response<?>> handleConversionFailedException(ConversionFailedException e, HttpServletRequest request) {
        if (e.getCause() instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) e.getCause();
            return this.handleServiceException(serviceException, request);
        }
        Object servletPath = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern");
        LOG.error("Failed {} {}: {}", request.getMethod(), servletPath, e.getMessage());
        return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Response<?>> handleValidationException(ValidationException e, HttpServletRequest request) {
        if (e.getCause() instanceof InvalidRequestException) {
            InvalidRequestException invalidRequestException = (InvalidRequestException) e.getCause();
            return this.handleInvalidRequestException(invalidRequestException, request);
        }
        Object servletPath = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern");
        LOG.error("Failed {} {}: {}", request.getMethod(), servletPath, e.getMessage());
        return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleException(Exception e, HttpServletRequest request) {
        Object servletPath = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern");
        LOG.error("Failed {} {}: {}", request.getMethod(), servletPath, e.getMessage());
        return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
