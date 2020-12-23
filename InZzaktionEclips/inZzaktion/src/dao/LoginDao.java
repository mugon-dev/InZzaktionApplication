package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dao.DBconn;

public class LoginDao {
		private LoginDao() {}
		private static LoginDao instance = new LoginDao();
		public static LoginDao getInstance() {
		
			
			return instance;
		}
		
		public String select(String id, String pw) {
			String str="";
			Connection conn= null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			String sql="select pw from member where id=?";
			
			
			try {
				conn = DBconn.getConn();
				ps = conn.prepareStatement(sql);
				ps.setString(1, id);
				rs =ps.executeQuery();
				if(rs.next()) {
					if(pw.equals(rs.getNString(1))) {
						str="로그인 완료";
						
					}else {
						str="비밀번호가 틀림";
						
					}
				}else {
					str="아이디가 틀렸습니다.";
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
		public String selectOne(String id){
			String memberNo = null;
			Connection conn=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			String sql="select NO from member where id=?";
			try {
				conn=DBconn.getConn();
				ps=conn.prepareStatement(sql);
				ps.setString(1, id);
				rs=ps.executeQuery();
				while(rs.next()) {
					memberNo = rs.getString("NO");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				DBconn.close(conn, ps, rs);
			}
			return memberNo;
		}
		
}
