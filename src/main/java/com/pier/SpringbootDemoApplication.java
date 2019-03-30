package com.pier;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 等价于@Configuration、@EnableAutoConfiguration 和 @ComponentScan
 */
@SpringBootApplication/*(exclude={DataSourceAutoConfiguration.class})*/
/*@MapperScan(basePackages = {"com.pier.dao"})*/
public class SpringbootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoApplication.class, args);
    }

}

