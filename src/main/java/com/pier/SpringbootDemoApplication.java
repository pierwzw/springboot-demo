package com.pier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 等价于@Configuration、@EnableAutoConfiguration 和 @ComponentScan
 * 而@EnableWebMvc 则需要以编程的方式指定视图文件相关配置 @see:https://blog.csdn.net/zxc123e/article/details/84636521
 */
@SpringBootApplication/*(exclude={DataSourceAutoConfiguration.class})*/
@EnableCaching
/*@MapperScan(basePackages = {"com.pier.dao"})*/
//@ServletComponentScan 用于@WebFilter和@WebListener 启动类下不用加package
public class SpringbootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoApplication.class, args);
    }

}

