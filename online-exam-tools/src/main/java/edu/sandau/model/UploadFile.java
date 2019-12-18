package edu.sandau.model;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

@Data
public class UploadFile implements Serializable {
    String filePath;
    File file;
}
