package com.kkh.springframe.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.kkh.springframe.dao.UserDao;
import com.kkh.springframe.sqlservice.jaxb.SqlType;
import com.kkh.springframe.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService {

	private Map<String, String> sqlMap = new HashMap<String, String>();

	private String sqlmapFile;

	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}
	// Spring IOC 컨테이너 작업이 끝나면 @PostConstruct 호출
	
	@PostConstruct
	public void loadSql() {
		String contextPath = Sqlmap.class.getPackage().getName(); 
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
			// InputStream == ByteStream 0과 1로만, BitStream은 
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);

			for(SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		} 
	}

	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);
		if (sql == null)  
			throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다");
		else
			return sql;
	}

}