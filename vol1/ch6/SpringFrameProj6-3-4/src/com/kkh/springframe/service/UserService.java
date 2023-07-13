package com.kkh.springframe.service;

import com.kkh.springframe.domain.User;

public interface UserService {
	void add(User user); // 단위 테스트 add()
	void upgradeLevels();
}

