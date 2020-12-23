package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vo.Note;

public class WriteDao {
	private WriteDao() {
	}

	private static WriteDao instance = new WriteDao();

	public static WriteDao getInstance() {
		return instance;
	}
	//멤버 no 넣기
	public boolean insert(Note note) {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		
		String sql = "INSERT INTO NOTE (`NO`,`TITLE`, `CONTENT`, `PHOTO`, `SHARE`) VALUES (?,?,?,?,?)";
		
		try {
			conn= DBconn.getConn();
			ps = conn.prepareStatement(sql);
			ps.setNString(1, note.getMemberNo());
			ps.setNString(2, note.getTitle());
			ps.setNString(3, note.getContent());
			ps.setNString(4, note.getPhoto());
			ps.setNString(5, note.getShare());
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
	
	//가장 최근 insert note 번호 리턴
	public String selectOne(){
		String noteNo = null;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql="select NOTE_NO from NOTE order by RGST_DT desc LIMIT 1";
		try {
			conn=DBconn.getConn();
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()) {
				noteNo = rs.getString("NOTE_NO");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBconn.close(conn, ps, rs);
		}
		return noteNo;
	}

}
