package com.pier.repository;

import java.util.List;

import com.pier.bean.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// 标识为DAO层
@Repository
// 扩展MongoRepository接口
public interface PersonRepository extends MongoRepository<Person, Long> {
	/**
	 * 符合JPA规范命名方法，则不需要再实现该方法也可用，
	 * 意在对满足条件的文档按照用户名称进行模糊查询
	 * @param personName -- 用户名称
	 * @return 满足条件的用户信息
	 */
	List<Person> findByPersonNameLike(String personName);
	
	
	/**
	 * 根据编号或者用户名查找用户
	 * @param id -- 编号
	 * @param personName -- 用户名
	 * @return 用户信息
	 */
	Person findPersonByIdOrPersonName(String id, String personName);
}