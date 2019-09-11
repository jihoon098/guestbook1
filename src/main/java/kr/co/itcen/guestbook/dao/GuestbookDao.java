package kr.co.itcen.guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.itcen.guestbook.vo.GuestbookVo;



public class GuestbookDao {
	
	public Boolean insert(String name, String password, String contents) {
		Boolean result = false;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select last_insert_id()");
			Long no = null;
			if(rs.next()) {
				no = rs.getLong(1);
			}else {
				no = 1L;
			}
			
			String sql = "insert into guestbook values(?, ?, ?, ?, ?)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, no);
			pstmt.setString(2, name);
			pstmt.setString(3, password);
			pstmt.setString(4, contents);
			pstmt.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			int count = pstmt.executeUpdate();
			result = (count == 1);
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				
				if(pstmt != null) {
					pstmt.close();
				}
				
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;		
	}
	
	private Connection getConnection() throws SQLException {
		Connection connection = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		
			String url = "jdbc:mariadb://192.168.1.124:3306/webdb?characterEncoding=utf8";
			connection = DriverManager.getConnection(url, "webdb", "000000");
		
		} catch (ClassNotFoundException e) {
			System.out.println("Fail to Loading Driver:" + e);
		}
		
		return connection;
	}



	public Boolean delete(Long no, String password) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		try {
			connection = getConnection();
			
			String sql = "delete from guestbook where no = ? and password = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, no);
			pstmt.setString(2, password);
			
			int count = pstmt.executeUpdate();
			if(count > 0) {
				return true;
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public List<GuestbookVo> getList() {
		List<GuestbookVo> result = new ArrayList<GuestbookVo>();
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = "select no, name, reg_date, contents from guestbook order by no asc";
			pstmt = connection.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String reg_date = rs.getString(3);
				String contents = rs.getString(4);
				
				GuestbookVo vo= new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setReg_date(reg_date);
				vo.setContents(contents);
				
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}	
}
