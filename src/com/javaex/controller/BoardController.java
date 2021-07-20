package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("컨트롤러");
		request.setCharacterEncoding("UTF-8");
		
		String action = request.getParameter("action");
		
		if("list".equals(action)) {
			
			System.out.println("[리스트]");
			
			BoardDao boardDao = new BoardDao();
			List<BoardVo> boardList = boardDao.getBoardList();
			
			request.setAttribute("bList", boardList);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
		}else if("writeForm".equals(action)) {
			
		}else if("write".equals(action)) {
			
		}else if("read".equals(action)) {
			
			System.out.println("[읽기]");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.getContent(no);
			
			request.setAttribute("boardVo", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
		}else if("modifyForm".equals(action)) {
			
		}else if("modify".equals(action)) {
			
		}else if("delete".equals(action)) {
			
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
