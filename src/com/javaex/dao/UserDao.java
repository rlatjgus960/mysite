package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {
	
	//필드
	//0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	//생성자
	
	//메소드 gs
	
	//메소드 일반
	
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public int userInsert(UserVo userVo) {

		int count = -1;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " insert into users ";
			query += " values(seq_user_no.nextval, ?, ?, ?, ?) ";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPw());
			pstmt.setString(3, userVo.getName());
			pstmt.setString(4, userVo.getGender());
			
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 저장되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
		
	}
	
	//유저 1명 정보 가져오기
	public UserVo getUser(String id, String pass) {
		
		UserVo userVo = null;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select no, id, password, name, gender";
			query += " from users ";
			query += " where id = ? ";
			query += " and password = ? ";
			
			System.out.println(query);

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
	        pstmt.setString(2, pass);

			
			rs = pstmt.executeQuery();

			// 4.결과처리
			while(rs.next()) {
				int no = rs.getInt("no");
				String uid = rs.getString("id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				
				
				//생성자가 없는 경우 setter 이용
				userVo = new UserVo(no, uid, password, name, gender);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
		return userVo;
		
	}
	
	
	
	
	

	//유저 1명 정보 수정하기
	public int modifyUser(UserVo userVo) {
		
		
		int count = -1;
		
		getConnection();

		try {
			
			String query = "";
			query += " update users ";
			query += " set password = ? , ";
			query += "     name = ? , ";
			query += "     gender = ? ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, userVo.getPw()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, userVo.getName()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, userVo.getGender()); // ?(물음표) 중 3번째, 순서중요
			pstmt.setInt(4, userVo.getNo()); // ?(물음표) 중 4번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			System.out.println(count+"건 수정");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
		return count;
		
	}


}
