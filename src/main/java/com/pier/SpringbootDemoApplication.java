package com.pier;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 等价于@Configuration、@EnableAutoConfiguration 和 @ComponentScan
 */
@EnableCaching
@SpringBootApplication/*(exclude={DataSourceAutoConfiguration.class})*/
/*@MapperScan(basePackages = {"com.pier.dao"})*/
public class SpringbootDemoApplication {

    //解决netty冲突
    //System.setProperty("es.set.netty.runtime.available.processors", "false");
    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoApplication.class, args);
    }

}

