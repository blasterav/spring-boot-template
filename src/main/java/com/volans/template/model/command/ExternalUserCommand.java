package com.volans.template.model.command;

import com.volans.template.model.enums.UserLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ExternalUserCommand {

    private String cardId;

    private String firstName;

    private String secondName;

    private UserLevel level;

    private String dateOfBirth;

    private Integer age;

}
