package edu.sandau.dao;

import edu.sandau.model.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

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
        file.setUpload_file_id(keyId);
        return file;
    }

}
