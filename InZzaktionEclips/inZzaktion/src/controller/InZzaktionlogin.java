package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dao.LoginDao;
import dao.NoteDao;
import dao.TagDao;
import vo.Note;
import vo.Tag;

/**
 * Servlet implementation class inZzaktionlogin
 */
@WebServlet("/login")
public class InZzaktionlogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InZzaktionlogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String str = LoginDao.getInstance().select(id, pw);
		String memberNo = LoginDao.getInstance().selectOne(id);
		System.out.println("memberNO: "+memberNo+str);
		JSONObject sendObject = new JSONObject();
		sendObject.put("memberNo",memberNo);
		sendObject.put("str",str);
		out.print(sendObject.toString());
		

	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		
		doGet(request, response);
	}

}
