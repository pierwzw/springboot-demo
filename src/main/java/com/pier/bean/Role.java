package com.pier.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author zhongweiwu
 * @date 2019/9/19 14:30
 */
@Document
@Data
public class Role implements Serializable {

    private static final long serialVersionUID = -4659144512744733508L;

    private String id;
    @Field("role_name")
    private String roleName = null;
    private String note = null;
}
