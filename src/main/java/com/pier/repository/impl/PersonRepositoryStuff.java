package com.pier.repository.impl;

import com.pier.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

/****imports ****/
//定义为数据访问层
@Repository
//注意这里类名称，默认要求是接口名称（PersonRepository） + "impl"
//这里Spring JPA会自动找到这个类作为接口方法实现
@EnableMongoRepositories(
	//扫描包
	basePackages = "com.pier.repository",
	//使用自定义后缀，其默认值为工mpl
	//此时需要修改类名2 UserRepositoryimpl 一＞UserRepositoryStuff
	repositoryImplementationPostfix ="Stuff"
)
public class PersonRepositoryStuff {
	
	@Autowired// 注入MongoTemplate
	private MongoTemplate mongoTmpl = null;

	// 注意方法名称和接口定义也需要保持一致
	public Person findPersonByIdOrPersonName(Long id, String personName) {
		// 构造id查询准则
		Criteria criteriaId = Criteria.where("id").is(id);
		// 构造用户名查询准则
		Criteria criteriaPersonName = Criteria.where("person_name").is(personName);
		Criteria criteria = new Criteria();
		// 使用$or操作符关联两个条件，形成或关系
		criteria.orOperator(criteriaId, criteriaPersonName);
		Query query = Query.query(criteria);
		// 执行查询返回结果
		return mongoTmpl.findOne(query, Person.class);
	}
}
