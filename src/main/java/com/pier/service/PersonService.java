package com.pier.service;

import java.util.List;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.pier.bean.Person;

public interface PersonService {
	public void savePerson(Person person);

	public DeleteResult deletePerson(String id);

	public List<Person> findPerson(String personName, String note, int skip, int limit);

	public UpdateResult updatePerson(String id, String personName, String note);

	public Person getPerson(String id);
}
