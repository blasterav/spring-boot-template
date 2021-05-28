package com.volans.template.model.command;

import com.volans.template.model.enums.UserLevel;
import com.volans.template.model.enums.UserStatus;
import com.volans.template.model.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UserCommand {

    private Long id;

    private String cardId;

    private String firstName;

    private String secondName;

    private UserType type;

    private UserStatus status;

    private UserLevel level;

    private String dateOfBirth;

    private Integer age;

    private MobileCommand mobile;

}
