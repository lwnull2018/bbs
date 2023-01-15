package com.bbs.remark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.bbs.remark.dao")
public class BBSRemarkApplication {

	public static void main(String[] args) {
		// 初始化Api上下文
//		ApiContextInitializer.init();

		SpringApplication.run(BBSRemarkApplication.class, args);
	}
}
