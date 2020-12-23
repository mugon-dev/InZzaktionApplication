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

import dao.NoteDao;
import dao.TagDao;
import dao.WriteDao;
import vo.Note;
import vo.Tag;

/**
 * Servlet implementation class inZzaktionList
 */
@WebServlet("/List")
public class InZzaktionList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InZzaktionList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		System.out.println("list servlet 호출");
		String memberNo = request.getParameter("memberNo");
		String noteNo = WriteDao.getInstance().selectOne();
		int no = Integer.parseInt(noteNo)+1;
		JSONObject sendObject = new JSONObject();
		sendObject.put("memberNo",memberNo);
		sendObject.put("noteNo",no);
		System.out.println(sendObject.toString());
		out.print(sendObject.toString());
		
		/*
		 * List<Note> noteList = new ArrayList<Note>(); noteList =
		 * NoteDao.getInstance().selectMyAll(memberNo);
		 * 
		 * JSONObject totalObject = new JSONObject();
		 * 
		 * JSONArray noteJsonList = new JSONArray();
		 * 
		 * for (int i = 0; i < noteList.size(); i++) { JSONObject note = new
		 * JSONObject(); note.put("noteNo", noteList.get(i).getNoteNo());
		 * note.put("memberNo", noteList.get(i).getTitle()); note.put("content",
		 * noteList.get(i).getContent()); note.put("photo", noteList.get(i).getPhoto());
		 * note.put("shared", noteList.get(i).getShare()); noteJsonList.add(note); }
		 * totalObject.put("note", noteJsonList);
		 * 
		 * List<Tag> tagList = new ArrayList<Tag>(); tagList =
		 * TagDao.getInstance().selectMyAll(memberNo); JSONArray tagJsonList = new
		 * JSONArray();
		 * 
		 * for (int i = 0; i < tagList.size(); i++) { JSONObject tag = new JSONObject();
		 * tag.put("noteNo", tagList.get(i).getNoteNo()); tag.put("tagNm",
		 * tagList.get(i).getTagNm()); tag.put("rgbNo", tagList.get(i).getRgbNo());
		 * noteJsonList.add(tag); } totalObject.put("tag", tagJsonList);
		 * 
		 * String jsonInfo = totalObject.toJSONString(); System.out.println(jsonInfo);
		 * out.print(jsonInfo);
		 */

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");

		doGet(request, response);
	}

}
