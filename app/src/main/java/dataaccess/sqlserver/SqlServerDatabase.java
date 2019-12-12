package dataaccess.sqlserver;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlServerDatabase {
    public Boolean insertUserAccount(String email,String firstName, String encryptedPassword){
         Connection conn = connectToSqlServerDb();
        try{
            Statement statement = conn.createStatement();
            statement.executeQuery("insert into []")

        } catch(SQLException e){


        }

    }

    public Boolean insertBlowfishKey(String encrpytionKey){


    }

    public Connection connectToSqlServerDb(){
        Connection conn;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String conUrl = "jdbc:jtds:sqlserver://localhost:1433;databaseName=Koobook_Db;user=sa;password=admin;";
            conn = DriverManager.getConnection(conUrl);
            return conn;
        }catch(Exception e){
            Log.w("Error message: ", e.getMessage());
            return null;
        }

    }
}
