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

import dao.TagSearchDao;
import vo.TagSearch;

/**
 * Servlet implementation class TagSearchServlet
 */
@WebServlet("*.do")
public class TagSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TagSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");
    	System.out.println("servlet:");
    	
    	PrintWriter out = response.getWriter();
    	
    	String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String action = requestURI.substring(contextPath.length());

		
		String tagNm = request.getParameter("tagNm");
		System.out.println(tagNm);
		//int no = Integer.parseInt(request.getParameter("no"));
		int no = 1;
		
		if(action.equals("/TagSearch.do")) {
			List<TagSearch> list = new ArrayList<TagSearch>();
			list = TagSearchDao.getInstance().selectSearchList(tagNm, no);
			
			JSONObject totalObject = new JSONObject();
			JSONArray tagList = new JSONArray();
			
			for(int i = 0 ; i < list.size() ; i++) {
				JSONObject tag = new JSONObject();
				tag.put("title", list.get(i).getTitle());
				tag.put("photo", list.get(i).getPhoto());
				tag.put("tagNm", list.get(i).getTagNm());
				tag.put("rgbCode", list.get(i).getRgbCode());
				tag.put("likedCount", list.get(i).getLikedCount());
				tag.put("liked", list.get(i).getLiked());
				tag.put("content", list.get(i).getContent());
				tag.put("rgstDt", list.get(i).getRgstDt());
				tag.put("no", list.get(i).getNo());
				tag.put("noteNo", list.get(i).getNoteNo());
				
				tagList.add(tag);
			}
			totalObject.put("contents", tagList);
			String jsonInfo = totalObject.toJSONString();
			System.out.println(jsonInfo);
			out.print(jsonInfo);

		} else if(action.equals("/selectReple.do")) {
			int noteNo = Integer.parseInt(request.getParameter("noteNo"));
			
			List<TagSearch> replyList = new ArrayList<TagSearch>();
			replyList = TagSearchDao.getInstance().selectReply(noteNo);
			JSONArray tagList = new JSONArray();
			
			for(int i = 0 ; i < replyList.size() ; i++) {
				JSONObject tag = new JSONObject();
				tag.put("no", replyList.get(i).getNo());
				tag.put("id", replyList.get(i).getId());
				tag.put("replys", replyList.get(i).getReplys());
				tag.put("reNo", replyList.get(i).getReNo());
				
				tagList.add(tag);
			}
			System.out.println(tagList);
			out.print(tagList);
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
