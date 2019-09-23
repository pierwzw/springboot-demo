package com.pier.controller;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.pier.bean.Person;
import com.pier.repository.PersonRepository;
import com.pier.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhongweiwu
 * @date 2019/9/19 14:34
 */
@Controller
@RequestMapping("/person")
@Slf4j
public class PersonController {

    // 后面会给出其操作的方法
    @Autowired
    private PersonService personService = null;

    // 跳转到测试页面
    @RequestMapping("/page")
    public String page() {
        return "person";
    }

    /**
     * 保存（新增或者更新）用户
     * @param person -- 用户
     * @return 用户信息
     */
    @PostMapping("/save")
    @ResponseBody
    public Person savePerson(@RequestBody Person person) {
        personService.savePerson(person);
        return person;
    }

    /***
     * 获取用户
     * @param id -- 用户主键
     * @return 用户信息
     */
    @RequestMapping("/get")
    @ResponseBody
    public Person getPerson(String id) {
        Person person = personService.getPerson(id);
        return person;
    }


    /**
     * 查询用户
     * @param personName --用户名称
     * @param note -- 备注
     * @param skip -- 跳过用户个数
     * @param limit -- 限制返回用户个数
     * @return
     */
    @RequestMapping("/find")
    @ResponseBody
    public List<Person> findPerson(String personName, String note, Integer skip, Integer limit) {
        List<Person> personList = personService.findPerson(personName, note, skip, limit);
        return personList;
    }

    /**
     * 更新用户部分属性
     * @param id —— 用户编号
     * @param personName —— 用户名称
     * @param note —— 备注
     * @return 更新结果
     */
    @RequestMapping("/update")
    @ResponseBody
    public UpdateResult updatePerson(String id, String personName, String note) {
        return personService.updatePerson(id, personName, note);
    }

    /**
     * 删除用户
     * @param id -- 用户主键
     * @return 删除结果
     */
    @RequestMapping("/delete")
    @ResponseBody
    public DeleteResult deletePerson(String id) {
        return personService.deletePerson(id);
    }


    // 注入接口
    @Autowired
    private PersonRepository personRepository;

    // 执行查询
    @RequestMapping("/byName")
    @ResponseBody
    public List<Person> findByPersonName(String personName) {
        return personRepository.findByPersonNameLike(personName);
    }

    // 执行查询
    @RequestMapping("/findOr")
    @ResponseBody
    public Person findPersonByIdOrPersonName(String id, String personName) {
        return personRepository.findPersonByIdOrPersonName(id, personName);
    }
}
