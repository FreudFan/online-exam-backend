package edu.sandau.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * 院校专业枚举
 */
@Getter
@AllArgsConstructor
public enum OrganizationEnum {
    /***
     * 学校
     */
    SCHOOL(0, "school"),
    /***
     * 学院
     */
    COLLEGE(1, "college"),
    /***
     * 专业
     */
    MAJOR(2,"username");

    private final Integer value;
    private final String name;
}
