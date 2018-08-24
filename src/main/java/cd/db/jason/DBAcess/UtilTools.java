/**    
 * 文件名：UtilTools.java    
 *    
 * 版本信息：    
 * 日期：2018年4月21日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.db.jason.DBAcess;

/**    
 *     
 * 项目名称：DBAcess    
 * 类名称：UtilTools    
 * 类描述：    公共判断
 * 创建人：jinyu    
 * 创建时间：2018年4月21日 上午10:56:31    
 * 修改人：jinyu    
 * 修改时间：2018年4月21日 上午10:56:31    
 * 修改备注：    
 * @version     
 *     
 */
public class UtilTools {
    
    /**
     * 字符串是否是null or  empty
     * @param str
     * @return
     */
public static boolean IsNullorEmpty(String str)
{
    if(str==null||str.isEmpty())
    {
        return true;
    }
    else
    {
        return false;
    }
}
}
