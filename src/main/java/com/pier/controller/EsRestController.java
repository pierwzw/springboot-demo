package com.pier.controller;

import com.alibaba.fastjson.JSON;
import com.pier.bean.User;
import com.pier.es.EsRestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhongweiwu
 * @date 2019/4/9 11:05
 */
@Slf4j
@RestController
@RequestMapping("/esr")
public class EsRestController {

    /**
     * 创建索引
     * @param index
     */
    @RequestMapping("/createIndex")
    public String createIndex(@RequestParam String index) throws IOException {
        if (!EsRestUtil.existsIndex(index)) {
            EsRestUtil.createIndex(index);
        } else {
            return "索引已经存在";
        }
        return "索引创建成功";
    }

    /**
     * 判断索引是否存在
     * @param index
     */
    @RequestMapping("/isIndexExist")
    public boolean isIndexExist(@RequestParam String index) throws IOException {
        return EsRestUtil.existsIndex(index);
    }


    /**
     * 增加记录
     * @param index
     * @param type
     * @param id
     */
    @RequestMapping("/addRecord")
    public String addRecord(@RequestParam String index, @RequestParam String type, @RequestParam String id) throws IOException {
        User user = new User();
        user.setUsername("es-user");
        user.setPassword("es-pwd");
        user.setAccount(100);
        EsRestUtil.add(index, type, id, user);
        return "success";
    }


    /**
     * 判断记录是否存在
     * @param index
     * @param type
     * @param id
     */
    @RequestMapping("/isExist")
    public boolean isExist(@RequestParam String index, @RequestParam String type, @RequestParam String id) throws IOException {
        return EsRestUtil.exists(index, type, id);
    }


    /**
     * 获取记录信息
     * @param index
     * @param type
     * @param id
     */
    @RequestMapping("/get")
    public Map<String, Object> getRecord(@RequestParam String index, @RequestParam String type, @RequestParam String id) throws IOException {
        return EsRestUtil.get(index, type, id);
    }


    /**
     * 更新记录信息
     * @param index
     * @param type
     * @param id
     */
    @RequestMapping("/update")
    public String updateRecord(@RequestParam String index, @RequestParam String type, @RequestParam String id) throws IOException {
        User user = new User();
        user.setUsername("es-user-pier");
        user.setPassword("es-pwd-new");
        user.setAccount(10000);
        EsRestUtil.update(index, type, id, user);
        return "update succeed";
    }


    /**
     * 删除记录
     * @param index
     * @param type
     * @param id
     */
    @RequestMapping("/delete")
    public String deleteRecord(@RequestParam String index, @RequestParam String type, @RequestParam String id) throws IOException {
        EsRestUtil.delete(index, type, id);
        return "delete succeed";
    }


    /**
     * 模糊查询
     * @param index
     * @param type
     * @param word
     */
    @RequestMapping("/fuzzy")
    public String fyzzySearch(@RequestParam String index, @RequestParam String type, @RequestParam String field, @RequestParam String word) throws IOException {
        List<Map<String, Object>> list = EsRestUtil.search(index, type, field, word);
        return JSON.toJSONString(list);
    }
}
