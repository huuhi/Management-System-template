package Utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2024/12/8
 * 说明:连接池工具类
 */
public class DruidPoor {
     private static final DataSource dataSource;

     static {
          Properties properties = new Properties();
          try {
               properties.load(new FileInputStream(Resource.class.getResource("/druid.properties").getFile()));
               dataSource = DruidDataSourceFactory.createDataSource(properties);
          } catch (Exception e) {
               throw new RuntimeException(e);
          }
     }
     public static Connection con(){
          try {
               return dataSource.getConnection();
          } catch (SQLException e) {
               throw new RuntimeException(e);
          }
     }
     public static void Close(Statement s, ResultSet res,Connection con){
          try {
               if(res!=null){
                    res.close();
               }
               if(s!=null){
                    s.close();
               }
               if(con!=null){
                    con.close();
               }
          } catch (SQLException e) {
               throw new RuntimeException(e);
          }

     }
     public static void closeCon(Connection con){
          if(con!=null){
               try {
                    con.close();
               } catch (SQLException e) {
                    throw new RuntimeException(e);
               }
          }
     }
}
