/**    
 * 文件名：JarPath.java    
 *    
 * 版本信息：    
 * 日期：2018年8月24日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.db.jason.DBAcess;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

/**    
 *     
 * 项目名称：DBAcess    
 * 类名称：JarPath    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年8月24日 上午11:13:23    
 * 修改人：jinyu    
 * 修改时间：2018年8月24日 上午11:13:23    
 * 修改备注：    
 * @version     
 *     
 */
public class JarPath {
    public static String appLocaltion()
    {
      URL url = PoolManager.class.getProtectionDomain().getCodeSource().getLocation();
      if(url==null)
      {
          url=ClassLoader.getSystemClassLoader().getResource("");
      }
      String filePath = null;  
      try {  
          filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码  
      } catch (Exception e) {  
          e.printStackTrace();  
      }  
      if (filePath.endsWith(".jar")) {// 可执行jar包运行的结果里包含".jar"  
          // 截取路径中的jar包名  
          filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);  
      }  
        
      File file = new File(filePath);  
      return file.getAbsolutePath();//得到windows下的正确路径 
    }
}
