package com.volans.template.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum PassedVerificationType implements BaseEnum {

    TYPE_1(1),

    TYPE_2(2);

    private final Integer value;

    public static Optional<PassedVerificationType> find(int value) {
        return Arrays.stream(values())
                .filter(item -> item.getValue() == value)
                .findFirst();
    }

}
