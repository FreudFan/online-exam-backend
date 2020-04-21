package edu.sandau.dao;

import edu.sandau.entity.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class UploadFileDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public UploadFile save(UploadFile file) throws Exception {
        String SQL = " INSERT INTO upload_file " +
                "( fileName, filePath, user_id ) VALUES " +
                "( ?, ?, ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, file.getFileName());
            ps.setString(2, file.getFilePath());
            ps.setInt(3, file.getUser_id());
            return ps;
        }, keyHolder);

        int keyId = keyHolder.getKey().intValue();
        file.setId(keyId);
        return file;
    }

    public UploadFile getFileById(Integer id) throws Exception {
        String SQL = " SELECT * FROM upload_file WHERE id = ? ";
        List<UploadFile> uploadFileList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<UploadFile>(UploadFile.class), id);
        if (uploadFileList.size() == 0) {
            return null;
        } else {
            return uploadFileList.get(0);
        }
    }
}
