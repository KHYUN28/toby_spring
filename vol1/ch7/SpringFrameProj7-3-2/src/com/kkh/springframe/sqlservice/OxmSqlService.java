package com.kkh.springframe.sqlservice;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Unmarshaller;

import com.kkh.springframe.dao.UserDao;
import com.kkh.springframe.sqlservice.jaxb.SqlType;
import com.kkh.springframe.sqlservice.jaxb.Sqlmap;

public class OxmSqlService implements SqlService {

    // BaseSqlService 객체를 생성하여 사용할 준비
    private final BaseSqlService baseSqlService = new BaseSqlService();
    // OxmSqlReader 객체를 생성하여 사용할 준비
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    // SqlRegistry를 구현한 구현체를 저장하는 변수
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    // SqlRegistry를 설정하는 메서드
    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    // Unmarshaller를 설정하는 메서드
    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    // SQL 매핑 정보를 가진 XML 파일의 경로를 설정하는 메서드
    public void setSqlmapFile(String sqlmapFile) {
        this.oxmSqlReader.setSqlmapFile(sqlmapFile);
    }

    // PostConstruct 어노테이션이 붙은 메서드는 객체 생성 후 자동으로 호출됩니다.
    // 이 메서드는 XML 파일로부터 SQL 정보를 읽어와 BaseSqlService에 등록합니다.
    @PostConstruct
    public void loadSql() {
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);
        //하나씩 가져와서 저장.
        
        this.baseSqlService.loadSql();
    }

    // 주어진 key에 해당하는 SQL을 반환하는 메서드
    public String getSql(String key) throws SqlRetrievalFailureException {
        return this.baseSqlService.getSql(key);
    }

    // 내부 클래스로 정의된 OxmSqlReader 클래스는 SqlReader 인터페이스를 구현하고 있습니다.
    private class OxmSqlReader implements SqlReader {
    	
        private Unmarshaller unmarshaller;
        // SQL 매핑 정보를 가진 XML 파일의 기본 경로
        private final static String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
        private String sqlmapFile = DEFAULT_SQLMAP_FILE;

        // Unmarshaller를 설정하는 메서드
        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        // SQL 매핑 정보를 가진 XML 파일의 경로를 설정하는 메서드
        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }

        // XML 파일로부터 SQL 매핑 정보를 읽어와 SqlRegistry에 등록하는 메서드
        public void read(SqlRegistry sqlRegistry) {
            try {
                // UserDao 클래스의 리소스로부터 XML 파일을 읽어옵니다.
                Source source = new StreamSource(UserDao.class.getResourceAsStream(this.sqlmapFile));
                Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);
                // SqlType 객체들을 순회하며 key와 value를 SqlRegistry에 등록합니다.
                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                // XML 파일을 읽어오는 중 예외가 발생하면 IllegalArgumentException을 던집니다.
                throw new IllegalArgumentException(this.sqlmapFile + " 파일을 읽어오지 못했습니다.", e);
            }
        }
    }
}

