package eyobkt.restapichangemonitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MonitorDaoImpl implements MonitorDao {  
  
  private Connection connection;    
  
  public MonitorDaoImpl(Connection connection) throws SQLException {  
    if (connection == null) {
      throw new IllegalArgumentException();      
    }
    
    this.connection = connection;
  }
    
  public void insertMonitor(String url, String email, long contentLength) throws SQLException {
    String sql = "INSERT INTO monitor "  
               + "VALUES(?, ?, ?, null)";
    PreparedStatement ps = null;
    try {      
      ps = connection.prepareStatement(sql);
      ps.setString(1, url);
      ps.setString(2, email);
      ps.setLong(3, contentLength);
      ps.executeUpdate(); 
    } catch (SQLException e) {
      if (e.getErrorCode() == 1062) {
        throw new PrimaryKeyConstraintViolationException();
      } else {
        throw e;
      }
    } finally {
      if (ps != null) {
        ps.close();        
      }
    }
  }
  
  public int deleteMonitor(String url, String email) throws SQLException {    
    String sql = "DELETE FROM monitor "
               + "WHERE url = ? AND email = ?";
    PreparedStatement ps = null;
    try {      
      ps = connection.prepareStatement(sql);
      ps.setString(1, url);
      ps.setString(2, email);
      return ps.executeUpdate(); 
    } catch (SQLException e) {
      throw e;
    } finally {
      if (ps != null) {
        ps.close();        
      }
    }
  }
  
  public ResultSet selectAllMonitors() throws SQLException {
    String sql = "SELECT * "
               + "FROM monitor";
    Statement s = null;
    try {      
      s = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE
          , ResultSet.CONCUR_UPDATABLE); 
      return s.executeQuery(sql);
    } catch (SQLException e) {
      throw e;
    } finally {
      if (s != null) {
        s.close();        
      }
    }    
  }
  
  public void updateMonitor(String url, String email, long contentLength) throws SQLException {
    String sql = "UPDATE monitor "
               + "SET last_content_length = ? "
               + "WHERE url = ? AND email = ?";
    PreparedStatement ps = null;
    try {      
      ps = connection.prepareStatement(sql);
      ps.setLong(1, contentLength);
      ps.setString(2, url);
      ps.setString(3, email);    
      ps.executeUpdate();    
    } catch (SQLException e) {
      throw e;
    } finally {
      if (ps != null) {
        ps.close();        
      }
    }   
  }
  
  public void closeConnection() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}