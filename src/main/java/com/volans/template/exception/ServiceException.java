package com.volans.template.exception;

import lombok.Getter;

import static com.volans.template.model.StatusConstants.HttpConstants;


@Getter
public class ServiceException extends RuntimeException {

    private final HttpConstants status;
    private String text;

    public ServiceException(HttpConstants status) {
        super(status.getDesc(), null);
        this.status = status;
    }

    public ServiceException(String text, HttpConstants status) {
        super(status.getDesc(), null);
        this.status = status;
        this.text = text;
    }

}
