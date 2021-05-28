package com.volans.template.controller;

import com.volans.template.model.Response;
import com.volans.template.model.Status;
import com.volans.template.model.StatusConstants;

public interface BaseController {

    default <T> Response<T> success() {
        return new Response<>(new Status(StatusConstants.HttpConstants.SUCCESS), null);
    }

    default <T> Response<T> success(T data) {
        return new Response<>(new Status(StatusConstants.HttpConstants.SUCCESS), data);
    }

}
