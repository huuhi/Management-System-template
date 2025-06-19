package DAO.dao;


import Utils.DruidPoor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2024/12/8
 * 说明:最基本的Dao,其他dao的父类
 */

public class BasicDao<T> {
     private final static Logger log =  LoggerFactory.getLogger(BasicDao.class);
     QueryRunner qr=new QueryRunner();
     //提供增删改查的基本功能
     //增删改
     public int update(String sql,Object... params){
          Connection con= DruidPoor.con();
          try {
               con.setAutoCommit(false);
               int affect = qr.update(con, sql, params);
               con.commit();
               return affect;
          } catch (SQLException e) {
               try {
                    if(con!=null){
                         con.rollback();
                    }
               } catch (SQLException ex) {
                    log.error("回滚出现错误"+e);
                    throw new RuntimeException(ex);
               }
               log.error("增删改出现错误"+e);
               throw new RuntimeException(e);
          }finally {
               DruidPoor.closeCon(con);
          }
     }
     //查询多个数据
     public List<T> queryMultiple(String sql, Class<T> cl, Object... params){
          Connection con=DruidPoor.con();
          try {
               List<T> query = qr.query(con, sql, new BeanListHandler<>(cl), params);
               return query;
          } catch (SQLException e) {
               log.error("查询多条语句时出现错误"+e);
               throw new RuntimeException(e);
          } finally {
               DruidPoor.closeCon(con);
          }
     }
     // 查询全部数据
          public List<T> queryAll(String sql, Class<T> cl){
               Connection con=DruidPoor.con();
               try {
                    List<T> query = qr.query(con, sql, new BeanListHandler<>(cl));
                    return query;
               } catch (SQLException e) {
                    log.error("查询多条语句时出现错误"+e);
                    throw new RuntimeException(e);
               } finally {
                    DruidPoor.closeCon(con);
               }
           }

     //查询单个字段
     public T querySingle(String sql,Class<T> cl,Object... params){
          Connection con = DruidPoor.con();
          try {
               T query = qr.query(con, sql, new BeanHandler<>(cl), params);
               return query;
          } catch (SQLException e) {
               log.error("查找单个信息时出现错误"+e);
               throw new RuntimeException(e);
          } finally {
               DruidPoor.closeCon(con);
          }
     }
     //查询单个字段
     public Object queryField(String sql,Object... params){
          Connection con = DruidPoor.con();
          try {
               Object query = qr.query(con, sql, new ScalarHandler<>(), params);
               return query;
          } catch (SQLException e) {
               log.error("查询单个对象发生错误"+e);
               throw new RuntimeException(e);
          } finally {
               DruidPoor.closeCon(con);
          }

     }
     //批量处理
     public int[] batch(String sql,Object[]... params){
          Connection con = DruidPoor.con();
          try {
               int[] batch = qr.batch(con, sql, params);
               return batch;
          } catch (SQLException e) {
               throw new RuntimeException(e);
          }finally {
               DruidPoor.closeCon(con);
          }
     }

}
