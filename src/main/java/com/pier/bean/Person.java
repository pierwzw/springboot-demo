package com.pier.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhongweiwu
 * @date 2019/9/19 14:23
 */
@Document
@Data
public class Person implements Serializable {

    private static final long serialVersionUID = 2222522492188550889L;

    // mongodb文档编号， 主键
    @Id
    private String id;

    // 在mongodb中使用person_name保存属性
    @Field("person_name")
    private String personName = null;

    private String note = null;

    // 角色列表
    private List<Role> roles = null;
}
