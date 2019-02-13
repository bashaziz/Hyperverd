//package Grey;

import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConnectionHelper 
{

    public static final String DATABASE_USER = "user";
    public static final String DATABASE_PASSWORD = "password";
    public static final String MYSQL_AUTO_RECONNECT = "autoReconnect";
    public static final String MYSQL_MAX_RECONNECTS = "maxReconnects";
    public static final String DATABASE_NO_ACCESS_TO_PROCEDURE_BODIES = "noAccessToProcedureBodies";
    public static final String DATABASE_CONN_TEST = "SELECT VERSION() AS VERSION";
    
    private static Connection dbconnetion;
    
    public ConnectionHelper() 
	{
        PropertiesIO.loadProperties();
    }

    public static Connection getConnection() throws SQLException, Exception 
	{
         String driver = "com.mysql.jdbc.Driver";       
         String host = PropertiesIO.readValue("host");
         String user = PropertiesIO.readValue("username");
         String password = PropertiesIO.readValue("password");
         String port = PropertiesIO.readValue("port");
         String database = PropertiesIO.readValue("database");
         
         Class.forName(driver);
         
         String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + database;
         String dbUsername = user;
         String dbPassword = password;

         java.util.Properties connProperties = new java.util.Properties();
         connProperties.put(DATABASE_USER, dbUsername);
         connProperties.put(DATABASE_PASSWORD, dbPassword);
         connProperties.put(MYSQL_AUTO_RECONNECT, "true");
         connProperties.put(MYSQL_MAX_RECONNECTS, "1");
         connProperties.put(DATABASE_NO_ACCESS_TO_PROCEDURE_BODIES, "true");
         
         if(dbconnetion == null )
         {
             dbconnetion =  DriverManager.getConnection(dbURL, connProperties);
         }
         return dbconnetion;
    }
    
    public boolean testConnection() 
	{
        boolean result = false;
        
        try
        {
            Connection con = getConnection();
            ResultSet rs;
            Statement st = con.createStatement();
            
            rs = st.executeQuery(DATABASE_CONN_TEST);

            while(rs.next()){
                result = true;
            }
            
        }catch(Exception ex)
		{
            System.err.println(ex.toString());
        }
        
        return result;
    }
}
