package com.cr.toutiao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * @author cr
 */
@SpringBootApplication
@MapperScan("com.cr.toutiao.mapper")
public class ToutiaoApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SpringApplication.run(ToutiaoApplication.class, args);
	}

}
