package com.kkh.springframe.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.kkh.springframe.sqlservice.BaseSqlService;
import com.kkh.springframe.sqlservice.DefaultSqlService;
import com.kkh.springframe.sqlservice.HashMapSqlRegistry;
import com.kkh.springframe.sqlservice.JaxbXmlSqlReader;
import com.kkh.springframe.sqlservice.OxmSqlService;
import com.kkh.springframe.sqlservice.XmlSqlService;

@Configuration
public class TestDaoFactory {
	
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
		userDaoJdbc.setSqlService(sqlService());
		return userDaoJdbc;
	}
	
	@Bean
	public OxmSqlService sqlService() {
		OxmSqlService oxmSqlService = new OxmSqlService();
		oxmSqlService.setUnmarshaller(unmarshaller());
		return oxmSqlService;
	}

	@Bean
	public Jaxb2Marshaller unmarshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setContextPath("com.intheeast.springframe.sqlservice.jaxb");
		return jaxb2Marshaller;
	}

}


