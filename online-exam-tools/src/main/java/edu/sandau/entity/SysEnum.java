package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class SysEnum {
    private Integer id;
    /***
     * 模块名
     */
    private String catalog;
    /***
     * 枚举类型
     */
    private String type;
    /***
     * 枚举名称
     */
    private String name;
    /***
     * 整型值
     */
    private Integer value;
    /***
     * 说明
     */
    private String description;

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;
}
