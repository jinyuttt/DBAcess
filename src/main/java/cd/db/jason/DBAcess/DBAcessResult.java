/**    
 * 文件名：DBAcessResult.java    
 *    
 * 版本信息：    
 * 日期：2018年3月17日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.db.jason.DBAcess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**    
 *     
 * 项目名称：DBAcess    
 * 类名称：DBAcessResult    
 * 类描述：  面向用户的封装类  
 * 创建人：jinyu    
 * 创建时间：2018年3月17日 下午1:54:51    
 * 修改人：jinyu    
 * 修改时间：2018年3月17日 下午1:54:51    
 * 修改备注：    
 * @version     
 *     
 */
public class DBAcessResult {
    DBAcess db=new DBAcess();
    private   String error="";
    public  void setDB(String name)
    {
        db.setDBType(name);
    }
    /**
     * 获取错误
     * @return
     */
    public String getEror()
    {
        return error;
    }
    /**
     * 
     * @Title: executeDMLSql   
     * @Description: 执行SQL    insert,update,delete
     * @param sql
     * @return 影响行
     * @throws SQLException      
     */
        public int executeDMLSql(String sql) 
        {
            try {
                return db.executeDMLSql(sql);
            } catch (SQLException e) {
                error=e.getMessage();
              
            }
            return 0;
        }
        
        /**
         * 
         * @Title: executeQueryRS   
         * @Description: 查询  
         * @param sql
         * @param params
         * @return      
         * ResultSet      
         * @throws
         */
        public  ResultSet  executeQueryRS(String sql,Object[]params)
        {
               try {
                return db.executeQueryRS(sql, params);
            } catch (SQLException e) {
               error=e.getMessage();
            }
            return null;
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
        public ResultSet executeQuerySql(String sql)
        { 
             try {
             return   db.executeQuerySql(sql);
            } catch (SQLException e) {
               error=e.getMessage();
               
            }
            return null;
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
        public void executeBatchSql(String[]sqls) 
        {
                try {
                    db.executeBatchSql(sqls);
                } catch (SQLException e) {
                  error=e.getMessage();
                }
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
        public int executeBatchSql(String sql,Object[][]param,int commitNum) 
        {
              try {
            return    db.executeBatchSql(sql, param,commitNum);
            } catch (SQLException e) {
                 error=e.getMessage();
            }
            return 0;
        }
        /**   
         * insert update delete SQL语句的执行的统一方法   
         * @param sql SQL语句   
         * @param params 参数数组，若没有参数则为null   
         * @return 受影响的行数   
         */      
        public int executeUpdate(String sql, Object[] params) {      
            try {
                return db.executeUpdate(sql, params);
            } catch (SQLException e) {
             error=e.getMessage();
            }
            return 0;  
        }      
        /**   
         * SQL 查询将查询结果：一行一列   
         * @param sql SQL语句   
         * @param params 参数数组，若没有参数则为null   
         * @return 结果集   
         */      
        public Object executeQuerySingle(String sql, Object[] params) {      
                try {
                    return db.executeQuerySingle(sql, params);
                } catch (SQLException e) {
                      error=e.getMessage();
                }
                return null;
        }      
          
        /**   
         * 获取结果集，并将结果放在List中   
         *    
         * @param sql  SQL语句   
         *         params  参数，没有则为null    
         * @return List   
         *                       结果集   
         */      
        public List<Object> excuteQuery(String sql, Object[] params) {      
                  try {
                  return  db.excuteQuery(sql, params);
                } catch (SQLException e) {
                error=e.getMessage();
                }
                return null;
        }      
              
        /**   
           * 存储过程带有一个输出参数的方法   
         * @param sql 存储过程语句   
         * @param params 参数数组   
         * @param outParamPos 输出参数位置   
         * @param SqlType 输出参数类型   
         * @return 输出参数的值   
         */      
        public Object excuteQuery(String sql, Object[] params,int outParamPos, int SqlType) {      
             try {
            return    db.excuteQuery(sql, params, outParamPos, SqlType);
            } catch (SQLException e) {
                   error=e.getMessage();
            }
            return null;
                
        }
        public void closeDB() {
            db.closeDB();
            
        }   
}
