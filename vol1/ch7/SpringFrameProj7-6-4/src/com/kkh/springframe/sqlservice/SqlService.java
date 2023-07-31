package com.kkh.springframe.sqlservice;

public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}
