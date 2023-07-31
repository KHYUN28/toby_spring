package com.kkh;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import com.kkh.springframe.service.DummyMailSender;
import com.kkh.springframe.service.UserService;
import com.kkh.springframe.service.UserServiceTest.TestUserService;

@Configuration
public class TestAppContext {
	@Bean
	public UserService testUserService() {
		return new TestUserService();
	}
	
	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
}
