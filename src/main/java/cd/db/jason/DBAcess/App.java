package cd.db.jason.DBAcess;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DBAcessResult test=new DBAcessResult();
        test.executeQuerySql("select * from test");
    }
}
