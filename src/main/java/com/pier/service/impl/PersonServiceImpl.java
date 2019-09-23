package com.pier.service.impl;

import java.util.List;

import com.pier.bean.Person;
import com.pier.service.PersonService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DbCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@Service
public class PersonServiceImpl implements PersonService {

	// 注入MongoTemplate对象
	@Autowired
	private MongoTemplate mongoTmpl = null;

	@Override
	public Person getPerson(String id) {
		return mongoTmpl.findById(id, Person.class);
		// 如果只需要获取第一个也可以采用如下查询方法
		// Criteria criteriaId = Criteria.where("id").is(id);
		// Query queryId = Query.query(criteriaId);
		// return mongoTmpl.findOne(queryId, Person.class);
	}

	@Override
	public List<Person> findPerson(String personName, String note, int skip, int limit) {
		// 将用户名称和备注设置为模糊查询准则
		Criteria criteria = Criteria.where("person_name").regex(personName).and("note").regex(note);
		// 构建查询条件,并设置分页跳过前skip个，至多返回limit个
		Query query = Query.query(criteria).limit(limit).skip(skip);
		// 执行
		List<Person> personList = mongoTmpl.find(query, Person.class);
		return personList;
	}

	@Override
	public void savePerson(Person person) {
		// 使用名称为person文档保存用户信息
		mongoTmpl.save(person, "person");
		// 如果文档采用类名首字符小写，则可以这样保存
		// mongoTmpl.save(person);
	}

	@Override
	public DeleteResult deletePerson(String id) {
		// 构建id相等的条件
		Criteria criteriaId = Criteria.where("id").is(id);
		// 查询对象
		Query queryId = Query.query(criteriaId);
		// 删除用户
		DeleteResult result = mongoTmpl.remove(queryId, Person.class);
		return result;
	}

	@Override
	public UpdateResult updatePerson(String id, String personName, String note) {
		// 确定要更新的对象
		Criteria criteriaId = Criteria.where("id").is(id);
		Query query = Query.query(criteriaId);
		// 定义更新对象，后续可变化的字符串代表排除在外的属性
		Update update = Update.update("person_name", personName);
		update.set("note", note);
		// 更新单个对象
		UpdateResult result = mongoTmpl.updateFirst(query, update, Person.class);
		// 更新多个对象
		// UpdateResult result2 = mongoTmpl.updateMulti(query, update, Person.class);
		return result;
	}

}
