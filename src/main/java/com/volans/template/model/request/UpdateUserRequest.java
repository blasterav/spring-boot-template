package com.volans.template.model.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.volans.template.annotations.IsDate;
import com.volans.template.annotations.IsEnum;
import com.volans.template.annotations.Max;
import com.volans.template.annotations.Min;
import com.volans.template.model.StatusConstants;
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
public class UpdateUserRequest {

    private String cardId;

    private String firstName;

    private String secondName;

    @IsEnum(enumClass = UserType.class, exception = StatusConstants.HttpConstants.TYPE_IS_INVALID)
    private String type;

    @IsEnum(enumClass = UserStatus.class, exception = StatusConstants.HttpConstants.STATUS_IS_INVALID)
    private Integer status;

    @IsDate(format = DateTimeUtils.DATE_FORMAT_YYYY_MM_DD, exception = StatusConstants.HttpConstants.DATE_OF_BIRTH_IS_INVALID)
    private String dateOfBirth;

    @Min(value = 18, exception = StatusConstants.HttpConstants.AGE_IS_INVALID)
    @Max(value = 54, exception = StatusConstants.HttpConstants.AGE_IS_INVALID)
    private Integer age;

    private String mobileNumber;

    private String mobileBrand;

}
