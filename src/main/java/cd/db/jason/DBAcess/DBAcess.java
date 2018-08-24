/**    
 * 文件名：DBAcess.java    
 *    
 * 版本信息：    
 * 日期：2018年3月17日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.db.jason.DBAcess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**    
 *     
 * 项目名称：DBAcess    
 * 类名称：DBAcess    
 * 类描述：数据库操作类   
 * 创建人：jinyu    
 * 创建时间：2018年3月17日 上午11:11:26    
 * 修改人：jinyu    
 * 修改时间：2018年3月17日 上午11:11:26    
 * 修改备注：    
 * @version     
 *     
 */
public class DBAcess {
    
   private String dbName="psql";
   
   /**
    * 设置数据库类型
    * @param name
    */
   public void  setDBType(String name)
   {
       this.dbName=name;
   }
       
    /**   
     * @Title: getConnection   
     * @Description: 获取连接
     * @return
     * @throws SQLException      
     * Connection      
     * @throws   
     */
     
    public Connection getConnection() throws SQLException
    {
        return PoolManager.getInstance().getConnection(this.dbName);
    }
    

/**
 * 
 * @Title: executeDMLSql   
 * @Description: 执行SQL    insert,update,delete
 * @param sql
 * @return 影响行
 * @throws SQLException      
 */
    public int executeDMLSql(String sql) throws SQLException
    {
        // 获得连接      
        Connection conn = getConnection();      
        // 调用SQL       
        Statement smt = conn.createStatement();
        return  smt.executeUpdate(sql);
    }
    

  /**
 * 
 * @Title: exeQuerySql   
 * @Description: 执行SQL查询  
 * @param sql
 * @return
 * @throws SQLException      
 * ResultSet      
 * @throws
 */
    public ResultSet executeQuerySql(String sql) throws SQLException
    {
           // 获得连接      
             Connection conn = getConnection();      
            // 调用SQL       
             Statement smt = conn.createStatement();
             return    smt.executeQuery(sql);
    }
    
    /**
     * 
     * @Title: executeBatchSql   
     * @Description: JDBC执行多条SQL语句  
     * @param sqls
     * @throws SQLException      
     * void      
     * @throws
     */
    public void executeBatchSql(String[]sqls) throws SQLException
    {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        Statement  ps=conn.createStatement();
        for(int i=0;i<sqls.length;i++)
        {
             ps.addBatch(sqls[i]);
        }
        ps.executeBatch();
        conn.commit();
    }
    
    /**
     * 
     * @Title: executeBatchSql   
     * @Description: 批量执行  
     * @param sql
     * @param param
     * @return
     * @throws SQLException      
     * int      
     * @throws
     */
    public int executeBatchSql(String sql,Object[][]param,int commitSize) throws SQLException
    {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        PreparedStatement   ps=conn.prepareStatement(sql);
        int num=0;
        for(int i=0;i<param.length;i++)
        {
          
            ps.setObject(i, param[i]);
            ps.addBatch();
            num++;
            if(num%commitSize==0&&commitSize>0)
            {
                ps.executeBatch();
                ps.clearBatch();
            }
        }
        ps.executeBatch();
        conn.commit();
        return 0;
    }
    /**   
     * insert update delete SQL语句的执行的统一方法   
     * @param sql SQL语句   
     * @param params 参数数组，若没有参数则为null   
     * @return 受影响的行数   
     * @throws SQLException 
     */      
    public int executeUpdate(String sql, Object[] params) throws SQLException {      
        // 受影响的行数      
        int affectedLine = 0;      
              
        
            // 获得连接      
             Connection conn = getConnection();      
            // 调用SQL       
             PreparedStatement pst = conn.prepareStatement(sql);      
                  
            // 参数赋值      
            if (params != null) {      
                for (int i = 0; i < params.length; i++) {      
                    pst.setObject(i + 1, params[i]);      
                }      
            }      
            /*在此 PreparedStatement 对象中执行 SQL 语句， 
                                          该语句必须是一个 SQL 数据操作语言（Data Manipulation Language，DML）语句，比如 INSERT、UPDATE 或 DELETE  
                                          语句；或者是无返回内容的 SQL 语句，比如 DDL 语句。    */  
            // 执行      
            affectedLine = pst.executeUpdate();  
      
        return affectedLine;      
    }      
      
    /**   
     * SQL 查询将查询结果直接放入ResultSet中   
     * @param sql SQL语句   
     * @param params 参数数组，若没有参数则为null   
     * @return 结果集   
     * @throws SQLException 
     */      
    public ResultSet executeQueryRS(String sql, Object[] params) throws SQLException {      
        ResultSet rst = null;
      
            // 获得连接      
             Connection conn = this.getConnection();
            // 调用SQL      
             PreparedStatement pst = conn.prepareStatement(sql);      
            // 参数赋值      
            if (params != null) {      
                for (int i = 0; i < params.length; i++) {      
                    pst.setObject(i + 1, params[i]);   
                }      
            }      
                  
            // 执行      
            rst = pst.executeQuery();      
      
       
      
        return rst;      
    }      
          
    /**   
     * SQL 查询将查询结果：一行一列   
     * @param sql SQL语句   
     * @param params 参数数组，若没有参数则为null   
     * @return 结果集   
     * @throws SQLException 
     */      
    public Object executeQuerySingle(String sql, Object[] params) throws SQLException {      
        Object object = null;      
           
            // 获得连接      
            Connection conn = this.getConnection();      
                  
            // 调用SQL      
            PreparedStatement pst = conn.prepareStatement(sql);      
                  
            // 参数赋值      
            if (params != null) {      
                for (int i = 0; i < params.length; i++) {      
                    pst.setObject(i + 1, params[i]);      
                }      
            }      
                  
            // 执行      
            ResultSet rst = pst.executeQuery();      
      
            if(rst.next()) {      
                object = rst.getObject(1);      
            }        
      
        return object;      
    }      
      
  
    /**
     * 
     * @Title: excuteQuery   
     * @Description: 查询数据放入List 
     * @param sql
     * @param params
     * @return
     * @throws SQLException      
     * List<Object>      
     * @throws
     */
    public List<Object> excuteQuery(String sql, Object[] params) throws SQLException {      
        // 执行SQL获得结果集      
        ResultSet rs = executeQueryRS(sql, params); 
        // 创建ResultSetMetaData对象      
        ResultSetMetaData rsmd = null;
        // 结果集列数      
        int columnCount = 0;      
        try {      
            rsmd = rs.getMetaData(); 
            // 获得结果集列数      
            columnCount = rsmd.getColumnCount();      
        } catch (SQLException e1) {      
            System.out.println(e1.getMessage());      
        }      
        // 创建List      
        List<Object> list = new ArrayList<Object>();
            // 将ResultSet的结果保存到List中      
            while (rs.next()) {      
                Map<String, Object> map = new HashMap<String, Object>();      
                for (int i = 1; i <= columnCount; i++) {      
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));      
                }      
                list.add(map);//每一个map代表一条记录，把所有记录存在list中      
            } 
        return list;      
    }      
          
    /**   
     *   存储过程带有一个输出参数的方法   
     * @param sql 存储过程语句   
     * @param params 参数数组   
     * @param outParamPos 输出参数位置   
     * @param SqlType 输出参数类型   
     * @return 输出参数的值   
     * @throws SQLException 
     */      
    public Object excuteQuery(String sql, Object[] params,int outParamPos, int SqlType) throws SQLException {      
        Object object = null;      
        Connection conn =getConnection();
      
            // 调用存储过程      
            // prepareCall:创建一个 CallableStatement 对象来调用数据库存储过程。  
            CallableStatement callableStatement = conn.prepareCall(sql);      
                  
            // 给参数赋值      
            if(params != null) {      
                for(int i = 0; i < params.length; i++) {      
                    callableStatement.setObject(i + 1, params[i]);      
                }      
            }      
                  
            // 注册输出参数      
            callableStatement.registerOutParameter(outParamPos, SqlType);      
                  
            // 执行      
            callableStatement.execute();      
                  
            // 得到输出参数      
            object = callableStatement.getObject(outParamPos);      
                   
        return object;      
      
            
    }

    public void closeDB() {
        PoolManager.getInstance().closeCurDB();
        
    }      
}
