package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("컨트롤러");
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		BoardDao boardDao = new BoardDao();
		String action = request.getParameter("action");
		
		if("list".equals(action)) {
			
			System.out.println("[리스트]");
			
			String keyword = request.getParameter("keyword");
			
			List<BoardVo> boardList = null;
			
			if(keyword != null) {
				boardList = boardDao.getBoardList(keyword);
			}else {
				boardList = boardDao.getBoardList();
			}
			
			request.setAttribute("bList", boardList);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
		}else if("writeForm".equals(action)) {
			System.out.println("[쓰기폼]");
			
			
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
			
		}else if("write".equals(action)) {
			System.out.println("[쓰기]");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int userNo = ((UserVo)session.getAttribute("authUser")).getNo();
			
			BoardVo boardVo = new BoardVo(title, content, userNo);
			boardDao.boardInsert(boardVo);
			
			WebUtil.redirect(request, response, "/mysite/board?action=list");
			
		}else if("delete".equals(action)) {
			
			System.out.println("[삭제]");
			
			int userNo = ((UserVo)session.getAttribute("authUser")).getNo();
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo boardVo = new BoardVo(no, userNo);
			boardDao.delete(boardVo);
			
			WebUtil.redirect(request, response, "/mysite/board?action=list");
			
			
		}else if("read".equals(action)) {
			System.out.println("[읽기]");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			boardDao.updateHit(no);
			
			BoardVo boardVo = boardDao.getContent(no);
			
			request.setAttribute("boardVo", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
		}else if("modifyForm".equals(action)) {
			
			System.out.println("[수정폼]");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo boardVo = boardDao.getContent(no);
			
			request.setAttribute("boardVo", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");
			
		}else if("modify".equals(action)) {
			
			System.out.println("[수정]");
			
			
			int no = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			
			BoardVo boardVo = new BoardVo(no, title, content);
			
			System.out.println(boardVo);
			
			boardDao.modify(boardVo);
			
			WebUtil.redirect(request, response, "/mysite/board?action=list");

		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
