package com.volans.template.exception;

import lombok.Getter;

import static com.volans.template.model.StatusConstants.HttpConstants;


@Getter
public class InvalidRequestException extends RuntimeException {

    private final HttpConstants status;

    public InvalidRequestException(HttpConstants status) {
        super(status.getDesc(), null);
        this.status = status;
    }

}
