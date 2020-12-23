package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class JoinDao {
	
		private JoinDao() {}
		private static JoinDao instance = new JoinDao();
		public static JoinDao getInstance() {
			
			return instance;
			
		}
		public String insert(String id, String pw) {
			String str="";
			Connection conn= null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			String sql="select * from member where id=?";
			String sql2= "insert into member(id,pw) values(?,?)";
			
			try {
				conn = DBconn.getConn();
				ps = conn.prepareStatement(sql);
				ps.setNString(1, id);
				rs =ps.executeQuery();
				
				if(rs.next()) {
					str ="이미 사용 중인 아이디 입니다.";
				
				}else {
					ps= conn.prepareStatement(sql2);
					ps.setNString(1, id);
					ps.setNString(2, pw);
					
					int n = ps.executeUpdate();
					
					if(n==1) {
						str ="회원 가입 성공";
					}else {
						//str="회원 가입 실패";
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				
				try {
					if(rs!=null)rs.close();
					if(ps!=null)ps.close();
					if(conn!=null)conn.close();
					
				}catch(Exception e) {
					
				}
			}
			return str;
			
		}
		
		

}
