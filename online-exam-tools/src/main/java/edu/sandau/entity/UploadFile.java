package edu.sandau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Data
public class UploadFile implements Serializable {
    @JsonIgnore
    private final String TABLE_NAME = "upload_file";

    private Integer id;
    private String fileName;
    private String filePath;
    private Integer user_id;

    @JsonIgnore
    private File file;

    @JsonIgnore
    private Date createtime;
    @JsonIgnore
    private Date updatetime;
}
