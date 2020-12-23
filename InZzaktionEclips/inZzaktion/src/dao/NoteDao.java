package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vo.Note;

public class NoteDao {
	private NoteDao() {}
	private static NoteDao instance = new NoteDao();
	public static NoteDao getInstance()	{
		return instance;
	}
	
	//내꺼 리스트
		public List<Note> selectMyAll(String memberNo){
			List<Note> list=new ArrayList<Note>();
			Connection conn=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			String sql="select * from NOTE where no=? order by RGST_DT";
			try {
				conn=DBconn.getConn();
				ps=conn.prepareStatement(sql);
				ps.setNString(1, memberNo);
				rs=ps.executeQuery();
				while(rs.next()) {
					Note note=new Note();
					note.setNoteNo("NOTE_NO");
					note.setMemberNo("NO");
					note.setTitle("TITLE");
					note.setContent("CONTENT");
					note.setPhoto("PHOTO");
					note.setShare("SHARE");
					System.out.println(note.toString());
					list.add(note);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				DBconn.close(conn, ps, rs);
			}
			return list;
		}

}
