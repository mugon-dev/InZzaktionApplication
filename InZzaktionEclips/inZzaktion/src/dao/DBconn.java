package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBconn {


	public static Connection getConn() {
		Connection conn = null;
		
		try {
			
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds =(DataSource)envContext.lookup("jdbc/Inzzaktion");
				conn = ds.getConnection();
		}catch(Exception ex) {
			
			ex.printStackTrace();
		}
		
		return conn;
	}
	
	public static void close(Connection conn, PreparedStatement ps, 
			ResultSet rs) {
		try {
			if(rs != null)rs.close();
			if(ps!= null)ps.close();
			if(conn!=null)conn.close();
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}
			}
	
	
	public static void close(Connection conn, PreparedStatement ps) {
		try {
			
			if(ps!= null)ps.close();
			if(conn!=null)conn.close();
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}
			}
		
	
	}

