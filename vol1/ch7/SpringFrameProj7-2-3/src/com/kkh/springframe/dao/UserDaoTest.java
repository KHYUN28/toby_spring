package com.kkh.springframe.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kkh.springframe.domain.Level;
import com.kkh.springframe.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoFactory.class})
public class UserDaoTest {	
	// UserDao와 DataSource 객체를 자동 주입받음
	@Autowired UserDao dao; 
	@Autowired DataSource dataSource;
	
	// 테스트에 사용될 User 객체들
	private User user1;
	private User user2;
	private User user3;
	
	@BeforeEach
	public void setUp() {		
		// 각 테스트 메서드 실행 전에 호출되는 메서드로, 테스트에 사용될 User 객체들을 초기화합니다.
		this.user1 = new User("gyumee", "박성철", "springno1", "user1@ksug.org", Level.BASIC, 1, 0);
		this.user2 = new User("leegw700", "이길원", "springno2", "user2@ksug.org", Level.SILVER, 55, 10);
		this.user3 = new User("bumjin", "박범진", "springno3", "user3@ksug.org", Level.GOLD, 100, 40);
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {				
		// 테스트용 데이터베이스 테이블을 모두 삭제하고, getCount()로 결과가 0인지 확인
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);
		
		// User 객체 2개를 추가하고, getCount()로 결과가 2인지 확인
		dao.add(user1);
		dao.add(user2);
		assertEquals(dao.getCount(), 2);
		
		// 각 User 객체의 id를 이용하여 get() 메서드로 User 객체를 조회하고, 일치하는지 확인
		Optional<User> Optuserget1 = dao.get(user1.getId());
		if (!Optuserget1.isEmpty()) {
			User userget = Optuserget1.get();
			assertEquals(user1.getName(), userget.getName());
			assertEquals(user1.getPassword(), userget.getPassword());
		}
		
		Optional<User> Optuserget2 = dao.get(user2.getId());
		if (!Optuserget2.isEmpty()) {
			User userget = Optuserget2.get();
			assertEquals(user2.getName(), userget.getName());
			assertEquals(user2.getPassword(), userget.getPassword());
		}
	}
	
	@Test
	public void getUserFailure() throws SQLException, ClassNotFoundException {		
		// 테스트용 데이터베이스 테이블을 모두 삭제하고, getCount()로 결과가 0인지 확인
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);		
		
		// 존재하지 않는 id로 get() 메서드 호출 시 Optional이 비어있는지 확인
		Optional<User> Optuserget = dao.get("unknown_id");
		assertTrue(Optuserget.isEmpty());	
	}
	
	@Test
	public void count() throws SQLException, ClassNotFoundException {		
		// 테스트용 데이터베이스 테이블을 모두 삭제하고, getCount()로 결과가 0인지 확인
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		// User 객체 3개를 추가하고, getCount()로 결과가 3인지 확인
		dao.add(user1);
		assertEquals(dao.getCount(), 1);
		
		dao.add(user2);
		assertEquals(dao.getCount(), 2);
		
		dao.add(user3);
		assertEquals(dao.getCount(), 3);		
	}	
	
	@Test
	public void getAll() throws SQLException  {
		// 테스트용 데이터베이스 테이블을 모두 삭제하고, getAll()로 결과가 빈 리스트인지 확인
		dao.deleteAll();
		List<User> users0 = dao.getAll();
		assertEquals(users0.size(), 0);
		
		// User 객체를 추가하고 getAll()로 결과 리스트를 가져와 확인
		dao.add(user1); 
		List<User> users1 = dao.getAll();
		assertEquals(users1.size(), 1);
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2); 
		List<User> users2 = dao.getAll();
		assertEquals(users2.size(), 2);
		checkSameUser(user1, users2.get(0));  
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3); 
		List<User> users3 = dao.getAll();
		assertEquals(users3.size(), 3);	
		checkSameUser(user3, users3.get(0));  
		checkSameUser(user1, users3.get(1));  
		checkSameUser(user2, users3.get(2));
	}
	
	private void checkSameUser(User user1, User user2) {
		// 두 User 객체의 필드가 모두 일치하는지 확인하는 메서드
		assertEquals(user1.getId(), user2.getId());
		assertEquals(user1.getName(), user2.getName());
		assertEquals(user1.getPassword(), user2.getPassword());
		assertEquals(user1.getEmail(), user2.getEmail());
		assertEquals(user1.getLevel(), user2.getLevel());
		assertEquals(user1.getLogin(), user2.getLogin());
		assertEquals(user1.getRecommend(), user2.getRecommend());
	}
	
	@Test
	public void duplciateKey() throws SQLException {
		// 테스트용 데이터베이스 테이블을 모두 삭제하고, User 객체를 추가
		dao.deleteAll();
		dao.add(user1);
		// 이미 존재하는 id로 User 객체를 추가하면 DuplicateKeyException이 발생하는지 확인
		assertThrows(DuplicateKeyException.class, () -> dao.add(user1));
	}
	
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		try {
			 // 테스트용 데이터베이스 테이블을 모두 삭제하고, User 객체를 중복 추가
			dao.add(user1);
			dao.add(user1);
		} catch(DuplicateKeyException ex) {
			// DuplicateKeyException 발생 시 SQLException으로 변환하고, SQLExceptionTranslator를 통해 DataAccessException으로 변환하여 예외 발생 확인
			SQLException sqlEx = (SQLException)ex.getCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);			
			DataAccessException transEx = set.translate(null, null, sqlEx);
			assertEquals(DuplicateKeyException.class, transEx.getClass());
		}
	}
	
	@Test
	public void update() {
		// 테스트용 데이터베이스 테이블을 모두 삭제하고, User 객체 2개 추가
		dao.deleteAll();
		dao.add(user1);
		dao.add(user2);
		
		// user1 정보를 업데이트하고 update() 메서드를 호출하여 정보가 일치하는지 확인
		user1.setName("갱신된이름");
		user1.setPassword("springo6");
		user1.setEmail("user6@ksug.org");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		dao.update(user1);
		
		Optional<User> Optuser1update = dao.get(user1.getId());
		if (!Optuser1update.isEmpty()) {
			User user1update = Optuser1update.get();
			checkSameUser(user1, user1update);
		}
		
		Optional<User> Optuser2update = dao.get(user2.getId());
		if (!Optuser2update.isEmpty()) {
			User user2update = Optuser2update.get();
			checkSameUser(user2, user2update);
		}
	}	
}
