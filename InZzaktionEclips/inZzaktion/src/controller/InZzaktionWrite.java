package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dao.TagDao;
import dao.TagSearchDao;
import dao.WriteDao;
import vo.Note;
import vo.Tag;
import vo.TagSearch;

/**
 * Servlet implementation class InZzaktionWrite
 */
@WebServlet("/write")
public class InZzaktionWrite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InZzaktionWrite() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");
    	System.out.println("servlet:");
    	
    	PrintWriter out = response.getWriter();
    	
		String noteStr = request.getParameter("Write");
		String tagStr = request.getParameter("Tag");
		System.out.println(noteStr);
		System.out.println(tagStr);
		//노트저장
		String noteNo = null;;
		JSONParser jsonParser = new JSONParser();
		
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(noteStr);
			Note note = new Note();
			note.setTitle(jsonObject.get("title").toString());
			note.setShare(jsonObject.get("shared").toString());
			note.setContent(jsonObject.get("content").toString());
			note.setMemberNo(jsonObject.get("memberNo").toString());
			boolean flag = WriteDao.getInstance().insert(note);
			if(flag) {
				System.out.println("write 저장 성공: "+note.toString());
				noteNo = WriteDao.getInstance().selectOne();
				System.out.println("현재저장하는 노트 번호: "+noteNo);
				out.print(noteNo);
			}else {
				System.out.println("write 실패");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//태그저장
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(tagStr);
			Tag tag = new Tag();
			tag.setNoteNo(noteNo);
			tag.setTagNm(jsonObject.get("tagName").toString());
			tag.setRgbNo(jsonObject.get("rgbNo").toString());
			boolean flag = TagDao.getInstance().insert(tag);
			if(flag) {
				System.out.println("tag 저장 성공: "+tag.toString());
			}else {
				System.out.println("tag 실패");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
 // 파일 업로드
  	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  		Map<String, String> recipeMap = new HashMap<String, String>();
  		String encoding = "UTF-8";
  		
  		File currentDirPath = new File("D:\\src\\inzzaktion\\inzzaktionJsp\\inzzaktion\\WebContent\\images");
  		
  		DiskFileItemFactory factory = new DiskFileItemFactory();
  		factory.setRepository(currentDirPath);
  		factory.setSizeThreshold(1024 * 1024 * 5); // 5GB
  		factory.setDefaultCharset(encoding); // 파일올라올때 인코딩
  		ServletFileUpload upload = new ServletFileUpload(factory);
  		try {
  			List<FileItem> items = upload.parseRequest((RequestContext) request); //캐스트 없애야함
  			for (int i = 0; i < items.size(); i++) {
  				FileItem item = (FileItem) items.get(i);
  				if(item.isFormField()) {
  					System.out.println(item.getFieldName() + ":" + item.getString());
  					recipeMap.put(item.getFieldName(), item.getString());
  				} else {
  					System.out.println("파라미터명: " + item.getFieldName());
  					System.out.println("파일명: " + item.getName());
  					System.out.println("파일의 크기: " + item.getSize());

  					if(item.getSize() > 0) {
  						int idx = item.getName().lastIndexOf("\\"); // 윈도우시스템
  						if (idx == -1) {
  							idx = item.getName().lastIndexOf("/"); // 리눅스시스템 파일 마지막 부분
  						}
  						String fileName = item.getName().substring(idx + 1);
  						File uploadFile = new File(currentDirPath + "\\" + fileName);
  						recipeMap.put(item.getFieldName(), fileName);
  						item.write(uploadFile);
  					}
  				}
  			}
  		} catch(Exception e) {
  			e.printStackTrace();
  		}
  		return recipeMap;
  	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		doHandle(request, response);
	}

}
