package com.volans.template.controller;

import com.volans.template.component.PassedVerificationComponent;
import com.volans.template.model.Response;
import com.volans.template.model.request.AddPassedVerificationRequest;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class PassedVerificationController implements BaseController {

    private final PassedVerificationComponent passedVerificationComponent;

    @PostMapping(path = "/v1/passed-verifications")
    @ApiOperation(value = "Add passed verification", notes = "Possible response codes: 0")
    public Response<Void> addPassedVerification(@Valid @RequestBody AddPassedVerificationRequest request) {
        passedVerificationComponent.addPassedVerification(request);
        return success();
    }

}
