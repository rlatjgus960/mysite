package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

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

	public List<BoardVo> getBoardList() {
		return  getBoardList("");

	}
	
	// 리스트
	public List<BoardVo> getBoardList(String keyword) {
		List<BoardVo> boardList = new ArrayList<BoardVo>();

		getConnection();

		try {
			
			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " select  b.no, ";
			query += "         b.title, ";
			query += "         b.hit, ";
			query += "         to_char(b.reg_date, 'YY-MM-DD HH24:MI') regDate, ";
			query += "         b.user_no, ";
			query += "         u.name ";
			query += " from board b, users u ";
			query += " where b.user_no = u.no ";
			

			if (keyword != "" || keyword == null) {
				query += " and b.title like ? "; //(b.title || u.name || b.content)
				query += " order by b.no desc ";
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기

				pstmt.setString(1, '%' + keyword + '%'); // ?(물음표) 중 1번째, 순서중요
			} else {
				query += " order by b.no desc ";
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			}

			rs = pstmt.executeQuery();


			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				int user_no = rs.getInt("user_no");
				String name = rs.getString("name");

				BoardVo boardVo = new BoardVo(no, title, hit, regDate, user_no, name);

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
			query += " select  b.no, ";
			query += "         b.title, ";
			query += "         b.hit, ";
			query += "         to_char(b.reg_date, 'YYYY-MM-DD') regDate, ";
			query += "         b.content, ";
			query += "         b.user_no, ";
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
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				String content = rs.getString("content");
				int userNo = rs.getInt("user_no");
				String name = rs.getString("name");

				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, name);

				//boardList.add(boardVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return boardVo;

	}
	
	//쓰기

	public int boardInsert (BoardVo boardVo) {
		
		
		int count = -1;
		
		getConnection();

		try {
			
			String query = "";
			query += " insert into board ";
			query += " values(seq_board_no.nextval, ?, ?, ?, sysdate, ?) ";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getHit());
			pstmt.setInt(4, boardVo.getUserNo());
			
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count+"건 수정");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
		return count;
		
	}


	
	//수정

	public int modify(BoardVo boardVo) {
		
		
		int count = -1;
		
		getConnection();

		try {
			
			String query = "";
			query += " update board ";
			query += " set title = ? , ";
			query += "     content = ? ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, boardVo.getTitle()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, boardVo.getContent()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setInt(3, boardVo.getNo()); // ?(물음표) 중 3번째, 순서중요
			
			System.out.println(query);

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			System.out.println(count+"건 수정");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
		return count;
		
	}
	
	//삭제

	public int delete(BoardVo boardVo) {
		
		
		int count = -1;
		
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " delete from board ";
			query += " where user_no = ? ";
			query += " and no = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, boardVo.getUserNo());
			pstmt.setInt(2, boardVo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
		return count;
		
	}
	
	
	//수정

	public int updateHit(int no) {
		
		
		int count = -1;
		
		getConnection();

		try {
			
			String query = "";
			query += " update board ";
			query += " set hit = hit+1 ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no); 
			
			System.out.println(query);

			count = pstmt.executeUpdate(); 

			// 4.결과처리
			System.out.println(count+"건 수정");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
		return count;
		
	}
	

	
}
