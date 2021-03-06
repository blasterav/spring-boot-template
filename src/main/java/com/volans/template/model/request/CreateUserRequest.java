package com.volans.template.model.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.volans.template.annotations.*;
import com.volans.template.model.StatusConstants.HttpConstants;
import com.volans.template.model.enums.UserStatus;
import com.volans.template.model.enums.UserType;
import com.volans.template.util.DateTimeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateUserRequest {

    @Required(exception = HttpConstants.CARD_ID_IS_REQUIRED)
    private String cardId;

    @Required(exception = HttpConstants.FIRST_NAME_IS_REQUIRED)
    private String firstName;

    @Required(exception = HttpConstants.SECOND_NAME_IS_REQUIRED)
    private String secondName;

    @Required(exception = HttpConstants.TYPE_IS_REQUIRED)
    @IsEnum(enumClass = UserType.class, exception = HttpConstants.TYPE_IS_INVALID)
    private String type;

    @Required(exception = HttpConstants.STATUS_IS_REQUIRED)
    @IsEnum(enumClass = UserStatus.class, exception = HttpConstants.STATUS_IS_INVALID)
    private Integer status;

    @Required(exception = HttpConstants.DATE_OF_BIRTH_IS_REQUIRED)
    @IsDate(format = DateTimeUtils.DATE_FORMAT_YYYY_MM_DD, exception = HttpConstants.DATE_OF_BIRTH_IS_INVALID)
    private String dateOfBirth;

    @Required(exception = HttpConstants.AGE_IS_REQUIRED)
    @Min(value = 18, exception = HttpConstants.AGE_IS_INVALID)
    @Max(value = 54, exception = HttpConstants.AGE_IS_INVALID)
    private Integer age;

    @Required(exception = HttpConstants.MOBILE_NUMBER_IS_REQUIRED)
    private String mobileNumber;

    @Required(exception = HttpConstants.MOBILE_BRAND_IS_REQUIRED)
    private String mobileBrand;

}
