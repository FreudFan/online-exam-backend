package rest;

import com.alibaba.fastjson.JSONObject;
import model.Topics;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/***
 * 文件上传
 */
@Path("topic")
public class TopicsImportController {

    @POST
    @Path("import")
    @Consumes({ MediaType.MULTIPART_FORM_DATA + ";charset=UTF-8" })
    @Produces({ MediaType.APPLICATION_JSON })
    public Map topicImport(@FormDataParam("file") InputStream file,
                           @FormDataParam("file") FormDataContentDisposition disposition) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", "测试中文123abc");
        return map;
    }

}
