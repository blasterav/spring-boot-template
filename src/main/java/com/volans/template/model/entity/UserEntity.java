package com.volans.template.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UserEntity {

    private Long id;

    private String cardId;

    private String firstName;

    private String secondName;

    private String type;

    private Integer status;

    private Integer level;

    private String dateOfBirth;

    private Integer age;

    private MobileEntity mobile;

}
