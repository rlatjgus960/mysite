package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {


	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

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

	
	// 리스트
	public List<BoardVo> getBoardList() {
		List<BoardVo> boardList = new ArrayList<BoardVo>();

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " select  b.no, ";
			query += "         b.title, ";
			query += "         b.hit, ";
			query += "         to_char(b.reg_date, 'YY-MM-DD HH24:MI') regDate, ";
			query += "         u.name ";
			query += " from board b, users u ";
			query += " where b.user_no = u.no ";
			query += " order by b.no desc ";

			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				String name = rs.getString("name");

				BoardVo boardVo = new BoardVo(no, title, hit, regDate, name);

				boardList.add(boardVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return boardList;

	}

	
	//읽기
	public BoardVo getContent(int uNo) {
		
		BoardVo boardVo = null;

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " select  b.title, ";
			query += "         b.hit, ";
			query += "         to_char(b.reg_date, 'YYYY-MM-DD') regDate, ";
			query += "         b.content, ";
			query += "         u.name ";
			query += " from board b, users u ";
			query += " where b.user_no = u.no ";
			query += " and b.no = ? ";
			query += " order by b.no desc ";

			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, uNo);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				String content = rs.getString("content");
				String name = rs.getString("name");

				boardVo = new BoardVo(title, content, hit, regDate, name);

				//boardList.add(boardVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return boardVo;

	}
	
}
