package com.javaex.dao;

import com.javaex.vo.UserVo;

public class DaoTest {

	public static void main(String[] args) {

		UserVo userVo = new UserVo("aaa", "1234", "이효리", "female");
		
		UserDao userDao = new UserDao();
		userDao.userInsert(userVo);
	}

}
