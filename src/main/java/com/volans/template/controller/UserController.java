package com.volans.template.controller;

import com.volans.template.component.UserComponent;
import com.volans.template.model.Response;
import com.volans.template.model.command.UserCommand;
import com.volans.template.model.enums.UserLevel;
import com.volans.template.model.request.CreateUserRequest;
import com.volans.template.model.request.UpdateUserRequest;
import com.volans.template.model.response.CalculateUserLevelResponse;
import com.volans.template.model.response.UserResponse;
import com.volans.template.model.response.UserShortResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController implements BaseController {

    private final UserComponent userComponent;
    private final ConversionService conversionService;

    @PostMapping(path = "/v1/users")
    @ApiOperation(value = "Add new user")
    public Response<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserCommand userCommand = userComponent.createUser(request);
        UserResponse response = conversionService.convert(userCommand, UserResponse.class);
        return success(response);
    }

    @PutMapping(path = "/v1/users/{id}")
    @ApiOperation(value = "Update user")
    public Response<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        UserCommand userCommand = userComponent.updateUser(id, request);
        UserResponse response = conversionService.convert(userCommand, UserResponse.class);
        return success(response);
    }

    @GetMapping(path = "/v1/users/{id}")
    @ApiOperation(value = "Get user")
    public Response<UserResponse> getUser(@PathVariable Long id) {
        UserCommand userCommand = userComponent.getUser(id);
        UserResponse response = conversionService.convert(userCommand, UserResponse.class);
        return success(response);
    }

    @GetMapping(path = "/v1/users")
    @ApiOperation(value = "Get user list")
    public Response<List<UserShortResponse>> getUserList() {
        List<UserShortResponse> userShortResponseList = userComponent.getUserList()
                .stream()
                .map(item -> conversionService.convert(item, UserShortResponse.class))
                .collect(Collectors.toList());
        return success(userShortResponseList);
    }

    @DeleteMapping(path = "/v1/users/{id}")
    @ApiOperation(value = "Delete user")
    public Response<UserResponse> deleteUser(@PathVariable Long id) {
        userComponent.deleteUser(id);
        return success();
    }

    @PostMapping(path = "/v1/users/{id}/calculate-level")
    @ApiOperation(value = "Calculate user level")
    public Response<CalculateUserLevelResponse> calculateUserLevel(@PathVariable Long id) {
        UserLevel userLevel = userComponent.calculateUserLevel(id);
        CalculateUserLevelResponse response = new CalculateUserLevelResponse().setLevel(userLevel.getValue());
        return success(response);
    }

}
