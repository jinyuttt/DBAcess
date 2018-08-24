/**    
 * 文件名：DBClass.java    
 *    
 * 版本信息：    
 * 日期：2018年8月24日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.db.jason.DBAcess;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**    
 *     
 * 项目名称：DBAcess    
 * 类名称：DBClass    
 * 类描述：   jdbc加载器
 * 创建人：jinyu    
 * 创建时间：2018年8月24日 上午10:54:23    
 * 修改人：jinyu    
 * 修改时间：2018年8月24日 上午10:54:23    
 * 修改备注：    
 * @version     
 *     
 */
 class DBDriversClassLoader {
    private static class Sington
    {
        private static DBDriversClassLoader instance=new DBDriversClassLoader();
    }
    private static final Logger logger = LoggerFactory.getLogger(DBDriversClassLoader.class);
    public URLClassLoader drivers=null;
    private String jdbcDriversPath="";
    
    /**
     * driverDir设置的目录下的jar包，只获取最后一个存在的
     */
    private HashMap<String,String> mapJar=new HashMap<String,String>();
    
    /**
       * 根目录
     */
    static String applocaltion=".";
    
    /**
     * 驱动目录
     */
    static String driverDir="jdbcdrivers";
    
    /**
     * 
    * @Title: setdrivers
    * @Description: 设置驱动根目录，默认查找子目录jdbcdrivers
    * @param rootPath    参数
    * @return void    返回类型
     */
    public static void setdrivers(String rootPath)
    {
        applocaltion=rootPath;
    }
  static DBDriversClassLoader getInstance()
  {
    return Sington.instance; 
  }
  private  DBDriversClassLoader()
  {
      initClassLoader();
  }
  
  /**
   * 
  * @Title: isLoader
  * @Description: 扩展目录中jdbc驱动中是否有了新的驱动包
  * @return    参数
  * @return boolean    返回类型
   */
  public boolean isLoader()
  {
      if(jdbcDriversPath==null||jdbcDriversPath.isEmpty())
      {
          return false;
      }
      File dir=new File(jdbcDriversPath);
      if(!dir.exists())
      {
          return false;
      }
      else
      {
         File[] fs= dir.listFiles();
         if(fs.length>this.mapJar.size())
         {
             //说明有扩展中有增加
             return true;
         }
         else
         {
             //遍历
             for(int i=0;i<fs.length;i++)
             {
                 if(!this.mapJar.containsKey(fs[i].getName()))
                 {
                    return true;
                 }
             }
         }
      }
    return false;
  }
  /**
   * 
  * @Title: initClassLoader
  * @Description: 初始化加载器
  * @return void    返回类型
   */
   public  void  initClassLoader()
   {
       //4类路径，插件路径，系统目录，默认路径，其它路径
       //
       if(drivers!=null)
       {
           try {
            drivers.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
       }
       StringBuffer log=new StringBuffer();
       log.append("找到的包：");
       String app1=System.getProperty("user.dir").toLowerCase().trim();
       String app2=applocaltion;
       String app3=JarPath.appLocaltion().toLowerCase().trim();
       if(app2!=null)
       {
           app2=app2.toLowerCase().trim();
       }
       List<String> loadPath=new ArrayList<String>();
      if(!loadPath.contains(app1))
      {
          loadPath.add(app1);
      }
      if(!loadPath.contains(app2))
      {
          loadPath.add(app2);
      }

      if(!loadPath.contains(app3))
      {
          loadPath.add(app3);
      }

      if(!loadPath.contains("."))
      {
          loadPath.add(".");
      }
      int size=loadPath.size();
      List<URL> lsturls=new ArrayList<URL>();
     for(int i=0;i<size;i++)
     {
            File f=new File(loadPath.get(i));
            if(f.exists()&&f.isDirectory())
            {
                File[] lstFiles=f.listFiles();
                for(int j=0;j<lstFiles.length;j++)
                {
                    File curF=lstFiles[j];
                    if(curF.getName().toLowerCase().trim().endsWith(".jar"))
                    {
                        try {
                            lsturls.add(curF.toURI().toURL());
                            log.append(curF.getName());
                            log.append(",");
                        } catch (MalformedURLException e) {
                          
                            e.printStackTrace();
                        }
                      
                    }
                }
            }
             f=new File(loadPath.get(i)+"/"+driverDir);
             if(f.exists()&&f.isDirectory())
             {
                 
                 jdbcDriversPath=f.getAbsolutePath();
                 this.mapJar.clear();
                 File[] lstFiles=f.listFiles();
                 for(int j=0;j<lstFiles.length;j++)
                 {
                     File curF=lstFiles[j];
                     if(curF.getName().toLowerCase().trim().endsWith(".jar"))
                     {
                         try {
                            lsturls.add(curF.toURI().toURL());
                            log.append(curF.getName());
                            log.append(",");
                            this.mapJar.put(curF.getName(), null);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                       
                     }
                 }
             }
            
        } 
     //路径
     URL[] urls=new URL[lsturls.size()];
     lsturls.toArray(urls);
     drivers=new URLClassLoader(urls,ClassLoader.getSystemClassLoader());
     logger.debug(log.toString());
   }
    
}
