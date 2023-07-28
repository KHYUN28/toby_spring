package com.kkh.springframe.sqlservice.updatable;

import com.kkh.springframe.sqlservice.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		return new ConcurrentHashMapSqlRegistry();
	}
}