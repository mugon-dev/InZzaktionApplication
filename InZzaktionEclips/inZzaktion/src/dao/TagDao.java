package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vo.Note;
import vo.Tag;

public class TagDao {
	private TagDao() {}
	private static TagDao instance = new TagDao();
	public static TagDao getInstance() {
		return instance;
	}
	public boolean insert(Tag tag) {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		
		String sql = "INSERT INTO TAG (NOTE_NO, TAG_NM, RGB_NO) VALUES (?,?,?)";
		
		try {
			conn= DBconn.getConn();
			ps = conn.prepareStatement(sql);
			ps.setNString(1, tag.getNoteNo());
			ps.setNString(2, tag.getTagNm());
			ps.setNString(3, tag.getRgbNo());
			int n = ps.executeUpdate();
			if(n==1) {
				flag=true;
				System.out.println("데이터 입력 성공");
			}else {
				System.out.println("데이터 입력 실패");
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				DBconn.close(conn, ps, rs);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	return flag;	
	}
	
	//내꺼 리스트
			public List<Tag> selectMyAll(String memberNo){
				List<Tag> list=new ArrayList<Tag>();
				Connection conn=null;
				PreparedStatement ps=null;
				ResultSet rs=null;
				String sql="select * from TAG where no=? order by RGST_DT";
				try {
					conn=DBconn.getConn();
					ps=conn.prepareStatement(sql);
					ps.setNString(1, memberNo);
					rs=ps.executeQuery();
					while(rs.next()) {
						Tag tag = new Tag();
						tag.setNoteNo("NOTE_NO");
						tag.setTagNm("TAG_NM");
						tag.setRgbNo("RGB_NO");
						
						list.add(tag);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					DBconn.close(conn, ps, rs);
				}
				return list;
			}
	

}
