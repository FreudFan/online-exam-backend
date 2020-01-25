package edu.sandau.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  试题难度枚举
 */

@Getter
@AllArgsConstructor
public enum DifficultTypeEnum {
    /***
     * 容易
     */
    SIMPLE(0,"易"),
    /***
     * 中等 难度
     */
    NORMAL(1,"中"),
    /***
     * 困难 难度
     */
    HARD(2,"难");

    private final Integer key;
    private final String value;

    public  static Integer findKey(String value) {
        DifficultTypeEnum[] difficultTypeEnum = values();
        for (DifficultTypeEnum difficultEnum : difficultTypeEnum) {
            if (difficultEnum.getValue().equals(value)) {
                return difficultEnum.getKey();
            }
        }
        return null;
    }

}


