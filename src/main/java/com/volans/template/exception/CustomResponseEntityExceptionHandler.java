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

import static com.volans.template.model.StatusConstants.HttpConstants;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = {InvalidRequestException.class})
    protected ResponseEntity<Object> handleInvalidRequestException(RuntimeException e, HttpServletRequest request) {
        if (e instanceof InvalidRequestException) {
            InvalidRequestException invalidRequestException = (InvalidRequestException) e;
            LOG.error("Failed {} {}: {}", request.getMethod(), request.getServletPath(), invalidRequestException.getStatus().getDesc());
            return new ResponseEntity<>(new Response<>(new Status(invalidRequestException.getStatus()), null), HttpStatus.BAD_REQUEST);
        } else if (e.getCause() instanceof InvalidRequestException) {
            InvalidRequestException invalidRequestException = (InvalidRequestException) e.getCause();
            LOG.error("Failed {} {}: {}", request.getMethod(), request.getServletPath(), invalidRequestException.getStatus().getDesc());
            return new ResponseEntity<>(new Response<>(new Status(invalidRequestException.getStatus()), null), HttpStatus.BAD_REQUEST);
        }
        LOG.error("Failed {} {}: {}", request.getMethod(), request.getServletPath(), e.getMessage());
        return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException e, HttpServletRequest request) {
        if (e instanceof NotFoundException) {
            NotFoundException notFoundException = (NotFoundException) e;
            LOG.error("Failed {} {}: {}", request.getMethod(), request.getServletPath(), notFoundException.getStatus().getDesc());
            return new ResponseEntity<>(new Response<>(new Status(notFoundException.getStatus()), null), HttpStatus.NOT_FOUND);
        } else if (e.getCause() instanceof NotFoundException) {
            NotFoundException notFoundException = (NotFoundException) e.getCause();
            LOG.error("Failed {} {}: {}", request.getMethod(), request.getServletPath(), notFoundException.getStatus().getDesc());
            return new ResponseEntity<>(new Response<>(new Status(notFoundException.getStatus()), null), HttpStatus.NOT_FOUND);
        }
        LOG.error("Failed {} {}: {}", request.getMethod(), request.getServletPath(), e.getMessage());
        return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<Object> handleServiceException(RuntimeException e, HttpServletRequest request) {
        if (e instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) e;
            printServiceExceptionLog(request, serviceException);
            return new ResponseEntity<>(new Response<>(new Status(serviceException.getStatus()), null), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (e.getCause() instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) e.getCause();
            printServiceExceptionLog(request, serviceException);
            return new ResponseEntity<>(new Response<>(new Status(serviceException.getStatus()), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (request == null || request.getMethod() == null && request.getServletPath() == null) {
            LOG.error("Failed: {}", e.getMessage());
            return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOG.error("Failed {} {}: {}", request.getMethod(), request.getServletPath(), e.getMessage());
        return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void printServiceExceptionLog(HttpServletRequest request, ServiceException serviceException) {
        if (request == null || request.getMethod() == null && request.getServletPath() == null) {
            if (serviceException.getText() != null) {
                LOG.error("{}: {}", serviceException.getText(), serviceException.getStatus().getDesc());
            } else {
                LOG.error("ServiceException: {}", serviceException.getStatus().getDesc());
            }
        } else {
            if (serviceException.getText() != null) {
                LOG.error("Failed {} {}, {}: {}", request.getMethod(), request.getServletPath(), serviceException.getText(), serviceException.getStatus().getDesc());
            } else {
                LOG.error("Failed {} {}: {}", request.getMethod(), request.getServletPath(), serviceException.getStatus().getDesc());
            }
        }
    }

    @ExceptionHandler(value = {ConversionFailedException.class})
    protected ResponseEntity<Object> handleConversionFailedException(RuntimeException e, HttpServletRequest request) {
        if (e.getCause() instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) e.getCause();
            return this.handleServiceException(serviceException, request);
        }
        LOG.error("Failed: {}", e.getMessage());
        return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleException(RuntimeException e, HttpServletRequest request) {
        if (e.getCause() instanceof InvalidRequestException) {
            return handleInvalidRequestException(e, request);
        }
        if (e.getCause() instanceof NotFoundException) {
            return handleNotFoundException(e, request);
        }
        if (e.getCause() instanceof ServiceException) {
            return handleServiceException(e, request);
        }
        LOG.error("Failed: {}", e.getMessage());
        return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
