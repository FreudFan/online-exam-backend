package model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Topics implements Serializable {
    private Integer topics_id;
    private Integer options_id;
}
