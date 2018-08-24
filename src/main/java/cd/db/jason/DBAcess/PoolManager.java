/**
 * 
 */
package cd.db.jason.DBAcess;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 
 *     
 * 项目名称：DBAcess    
 * 类名称：PoolManager    
 * 类描述：   数据库连接管理
 * 创建人：jinyu    
 * 创建时间：2018年8月24日 上午10:53:00    
 * 修改人：jinyu    
 * 修改时间：2018年8月24日 上午10:53:00    
 * 修改备注：    
 * @version     
 *
 */
public class PoolManager {
    private static final Logger logger = LoggerFactory.getLogger(PoolManager.class);
    private static class Single
    {
        private final static   PoolManager dBService=new PoolManager();
    }
    public static PoolManager getInstance(){
        return Single.dBService;
    }
    private  ConcurrentHashMap<String,HikariDataSource> map=new ConcurrentHashMap<String,HikariDataSource>();

    private String applocaltion=".";
    // 调用SQL      
    PreparedStatement pst = null;
    //连接
    private ConcurrentHashMap<String, Connection> mapCon=new ConcurrentHashMap<String, Connection>();
   
    private ReentrantLock lock_obj=new ReentrantLock();
    private PoolManager()
    {
        initPath();
    }
    
    /**
     * 
    * @Title: initPath
    * @Description: 处理路径问题
    * @return void    返回类型
     */
    private void initPath()
    {
        //首先检查程序启动路径是否存在配置；
        String appPath=JarPath.appLocaltion();
        String dirPath=appPath+"/dbconfig";
        File dir=new File(dirPath);
        if(dir.exists())
        {
            this.applocaltion=appPath;
        }
    }
    
    /**
      * 调用前任意点设置
    * @Title: setConfigPath
    * @Description:设置配置根目录，查找目录的dbconfig子目录
    * @param rootPath    参数
    * @return void    返回类型
     */
    public void setConfigPath(String rootPath)
    {
        this.applocaltion=rootPath;
    }
    /**
      * 初始化配置连接
     * @param name
     */
 private  HikariDataSource initConfig(String name)
 {
     //
     ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
     Thread.currentThread().setContextClassLoader(DBDriversClassLoader.getInstance().drivers);
     System.out.println("读取DB配置路径："+applocaltion);
     StringBuffer buf=new StringBuffer();
     buf.append(applocaltion);
     buf.append("/dbconfig/");
     buf.append(name);
     buf.append("_dbpool.properties");
     File conf=new File(buf.toString());
     if(!conf.exists())
     {
         //继续处理路径，防止插件路径
         //构造函数已经采用加载器路径
         System.out.println("没有文件："+conf.getAbsolutePath());
         logger.info("没有文件："+conf.getAbsolutePath());
         return null;
     }
     else
     {
         System.out.println("读取DB配置："+buf.toString());
         logger.info("读取DB配置："+buf.toString());
     }
     //多次线程发送替换也没有关系;连接池最后都会销毁
     lock_obj.lock();//同步锁阻塞方法内
     //判断一次
     HikariDataSource dataSource=null;
     dataSource=map.getOrDefault(name, null);//再次取出，没有才加入
     if(dataSource==null)
     {
         if(!map.isEmpty())
         {
           if(DBDriversClassLoader.getInstance().isLoader())
           {
               //重新加载
               this.stop();
           }
         }
         HikariConfig config = new HikariConfig(buf.toString());
        dataSource= new HikariDataSource(config);
         map.put(name, dataSource);
     }
     lock_obj.unlock();
     //置回
     Thread.currentThread().setContextClassLoader(threadClassLoader);
     return dataSource;
 }

 
 /**
  * 获取线程ID
  * @return
  */
 private String getThreadId()
 {
     return String.valueOf(Thread.currentThread().getId());
 }
 /**
  * 获取连接
  * @param dbName db
  * @return
  *
  */
public Connection getConnection(String dbName)  {
    try {
        Connection con=null;
        HikariDataSource   dataSource=map.getOrDefault(dbName, null);
        if(dataSource==null)
        {
            //同步方法
            dataSource=initConfig(dbName);
        }
        if(dataSource!=null)
              con= dataSource.getConnection();
        return con;
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }    
   }

/**
 * 关闭数据库
 */
public void closeCurDB()
{
    Connection con = mapCon.getOrDefault(this.getThreadId(), null);
    if(con!=null)
    {
        try {
            con.close();
        } catch (SQLException e) {
         
            e.printStackTrace();
        }
    }
}

/**
 *  停止
 *  关闭所有连接
 */
public void stop()  {
    lock_obj.lock();
    for(HikariDataSource v:map.values())
    {
        v.close();
    }
    map.clear();
    DBDriversClassLoader.getInstance().initClassLoader();
    //加载器也重启处理
    lock_obj.unlock();
}


}
