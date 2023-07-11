package com.kkh.springframe.service;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.kkh.springframe.dao.UserDaoJdbc;

@Configuration
public class TestServiceFactory {
	@Bean
	public DataSource dataSource() {
		
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost:3306/testdb?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");

		return dataSource;
	}

	@Bean
	public UserDaoJdbc userDao() {
		UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
		userDaoJdbc.setDataSource(dataSource());
		return userDaoJdbc;
	}
	
	@Bean
	public UserService userService() {
		UserService userService = new UserService();
		userService.setUserDao(userDao());
		userService.setTransactionManager(transactionManager());
		//<property name="mailSender" ref="mailSender" />
		userService.setMailSender(javamailsenderimpl());
		//userService.setDataSource(dataSource());
		return userService;
	}
	
	@Bean
	public DummyMailSender mailSender() {
		DummyMailSender dummyMailSender = new DummyMailSender();
		//dummyMailSender dummyMailsender(DummyMailSender());
		return dummyMailSender;
	}
	
	@Bean
	public JavaMailSenderImpl javamailsenderimpl() {
		
		JavaMailSenderImpl mailsender = new JavaMailSenderImpl();
		
		mailsender.setHost("smtp.gmail.com");
		mailsender.setPort(587); // TLS: 587, SSL: 465
		mailsender.setUsername("kkh30123@gmail.com"); // 발신자 Gmail 계정
		mailsender.setPassword("cbwjhdfjgwovuzvs"); // 발신자 Gmail 계정 비밀번호
		return mailsender;
	}
	
	@Bean
	public Properties properties() {
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

	    return props;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		return dataSourceTransactionManager;
	}
}
