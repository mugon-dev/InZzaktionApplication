package dao;
/*
 * 2020.09.03 이현지 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vo.TagSearch;

public class TagSearchDao {
	private TagSearchDao() {}
	private static TagSearchDao instance = new TagSearchDao();
	
	public static TagSearchDao getInstance() {
		return instance;
	}
	
	// 태그 검색 리스트 뽑기 
	public List<TagSearch> selectSearchList(String tagNm, int no) {
		List<TagSearch> list = new ArrayList<TagSearch>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = " SELECT N.TITLE, N.PHOTO, "  
					+ " 	  GROUP_CONCAT(T.TAG_NM SEPARATOR ' ' ) AS TAG_NM, "
					+ " 	  GROUP_CONCAT(R.RGB_CODE SEPARATOR ' ') AS RGB_CODE, "
					+ " 	  (SELECT COUNT(L.NOTE_NO) FROM LIKED L WHERE L.NOTE_NO = N.NOTE_NO) AS LIKED_COUNT, "
					+ " 	  IF(ISNULL((SELECT NO FROM LIKED WHERE NOTE_NO = N.NOTE_NO AND NO = " + no + ")), 'N', 'Y') AS LIKED, "
					+ " 	  N.CONTENT, N.RGST_DT, N.NO, N.NOTE_NO "
					+ "  FROM NOTE N, TAG T, TAG_RGB R " 
					+ " WHERE N.NOTE_NO = T.NOTE_NO "
					+ "   AND T.RGB_NO = R.RGB_NO "
					+ "   AND N.SHARE ='Y' "
					;
					
		if(tagNm == null || tagNm.equals("") || tagNm.equals("#")) {
		} else {
			sql += "   	  AND N.NOTE_NO = (SELECT T2.NOTE_NO FROM TAG T2 WHERE T2.NOTE_NO = T.NOTE_NO AND T2.TAG_NM LIKE '안%') "; 
		}
		
		sql += " GROUP BY N.NOTE_NO "
			+ " ORDER BY N.NOTE_NO DESC " ;
		 
		try {
			conn = DBconn.getConn();
			ps = conn.prepareStatement(sql);
			System.out.println(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				TagSearch tagSearch = new TagSearch();
				
				tagSearch.setTitle(rs.getString("title"));
				tagSearch.setPhoto(rs.getString("photo"));
				tagSearch.setTagNm(rs.getString("tag_nm"));
				tagSearch.setRgbCode(rs.getString("rgb_code"));
				tagSearch.setLikedCount(rs.getString("liked_count"));
				tagSearch.setLiked(rs.getString("liked"));
				tagSearch.setContent(rs.getNString("content"));
				tagSearch.setRgstDt(rs.getDate("rgst_dt"));
				tagSearch.setNo(rs.getInt("no"));
				tagSearch.setNoteNo(rs.getInt("note_no"));
				
				list.add(tagSearch);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBconn.close(conn, ps, rs);
		}
		return list;
	}
	
	// 좋아요 
	public boolean insertLiked(int noteNo, int no) {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = " INSERT INTO LIKED (NOTE_NO, NO) VALUES (?, ?) ";
		try {
			conn = DBconn.getConn();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, noteNo);
			ps.setInt(2, no);
			int n = ps.executeUpdate();
			if(n == 1) {
				flag = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBconn.close(conn, ps);
		}
		return flag;
	}
	
	// 좋아요 삭제 
	public boolean deleteLiked(int likedNo) {
		boolean flag = false;
		String sql = " DELETE FROM LIKED WHERE LIKED_NO = ? ";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBconn.getConn();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, likedNo);
			int n = ps.executeUpdate();
			if(n == 1) {
				flag = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBconn.close(conn, ps);
		}
		return flag;
	}

	// 댓글 
	public List<TagSearch> selectReply(int noteNo) {
		List<TagSearch> list = new ArrayList<TagSearch>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = " SELECT M.NO, M.ID, R.CONTENT AS REPLYS, R.RE_NO FROM REPLY R, MEMBER M "
					+ " WHERE R.NO = M.NO AND R.NOTE_NO = " + noteNo + " ORDER BY RE_NO DESC ";
		try {
			conn = DBconn.getConn();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				TagSearch tagSearch = new TagSearch();
				
				tagSearch.setNo(Integer.parseInt(rs.getString("no")));
				tagSearch.setId(rs.getString("id"));
				tagSearch.setReplys(rs.getString("replys"));
				tagSearch.setReNo(Integer.parseInt(rs.getString("re_no")));
				
				list.add(tagSearch);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBconn.close(conn, ps, rs);
		}
		
		return list;
	}
}
