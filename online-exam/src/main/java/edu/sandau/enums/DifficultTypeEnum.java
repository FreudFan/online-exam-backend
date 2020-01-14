package edu.sandau.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  试题难度枚举
 */

@Getter
@AllArgsConstructor
public enum DifficultTypeEnum {
    SIMPLE(0,"易"),
    NORMAL(1,"中"),
    HARD(2,"难");
    private final Integer value;
    private final String name;
}
