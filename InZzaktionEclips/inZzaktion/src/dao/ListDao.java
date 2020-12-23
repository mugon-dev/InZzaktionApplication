package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vo.Note;
import vo.tagName;

public class ListDao {
	
	private ListDao() {}
	
	private static ListDao instance  = new ListDao();
	
	public static ListDao getInstance() {
		
		return instance;
	}
	
	public List<tagName> list(String tag_nm){
		String str="";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<String> tag = new ArrayList<String>();
		
		String sql = "select * from test";
		
		List<tagName> list = new ArrayList<tagName>();
		
		try {
			conn = DBconn.getConn();
			ps= conn.prepareStatement(sql);
			rs=ps.executeQuery();
			int n =1;
			
			while(rs.next()) {
				tagName tagname = new tagName();
				
				String title= rs.getNString("title");
				String tag1 = rs.getNString("tag_nm");
				System.out.println(n+"타이틀 :" +title);
				System.out.println(n+"tag : " +tag1);
				
				tagname.setTag(tag1);
				tagname.setTitle(title);
				
				list.add(tagname);
				
				//str = title;
				//str= tag1;
				n++;
			}
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			try {
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				if(conn!=null) conn.close();
				
			}catch(Exception e) {
				
			}
		}
			
			return list;

		}
	}


