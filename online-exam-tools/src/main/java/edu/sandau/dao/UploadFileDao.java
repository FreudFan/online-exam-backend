package edu.sandau.dao;

import edu.sandau.model.UploadFile;
import edu.sandau.utils.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
        file.setUpload_file_id(keyId);
        return file;
    }

    public UploadFile getFileById(Integer id) throws Exception {
        String SQL = " SELECT * FROM upload_file WHERE upload_file_id = ? ";
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList(SQL, id);
        if (mapList.size() == 0) {
            return null;
        } else {
            return (UploadFile) MapUtil.mapToObject(mapList.get(0), UploadFile.class);
        }
    }
}
