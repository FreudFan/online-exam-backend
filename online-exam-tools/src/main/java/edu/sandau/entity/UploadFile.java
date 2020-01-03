package edu.sandau.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class UploadFile implements Serializable {
    @JSONField(serialize = false)
    private final String TABLE_NAME = "upload_file";

    @Getter @Setter
    private Integer upload_file_id;
    @Getter @Setter
    private String fileName;
    @Getter @Setter
    private String filePath;
    @Getter @Setter
    private Integer user_id;

    @JSONField(serialize = false)
    @Getter @Setter
    private File file;

    @Getter @JSONField(serialize = false)
    private Date createtime;
    @Getter @JSONField(serialize = false)
    private Date updatetime;
}
