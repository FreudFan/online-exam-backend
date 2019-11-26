package com.sandau.onlineexam.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class QuestionUpload {


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam MultipartFile file) {

        return "上传成功";
    }

    /**
     * 添加商品
     * @param request
     * @return
     */
    @RequestMapping(value = "/addcommodity", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addCommodity(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartHttpServletRequest;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        // 图片处理
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                // 取出略缩图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("imageFile");
                if (thumbnailFile != null) {
//                    thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "上传的图片不能为空");
                    return modelMap;
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传的图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // entity处理
        String name = HttpServletRequestUtil.getString(request, "name");
        Integer originalPrice = HttpServletRequestUtil.getInt(request, "originalPrice");
        Integer currentPrice = HttpServletRequestUtil.getInt(request, "currentPrice");
        Boolean enable = HttpServletRequestUtil.getBoolean(request, "enable");
        String describe = HttpServletRequestUtil.getString(request, "describe");
        Integer sum = HttpServletRequestUtil.getInt(request, "sum");
        Boolean isUnderRevision = HttpServletRequestUtil.getBoolean(request, "isUnderRevision");
        Boolean isSeeMore = HttpServletRequestUtil.getBoolean(request, "isSeeMore");

            return modelMap;
        }


}
