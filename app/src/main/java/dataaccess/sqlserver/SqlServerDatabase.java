package dataaccess.sqlserver;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlServerDatabase {
    public Boolean insertUserAccount(String firstName, String email, String encryptedPassword) {
        Connection conn = connectToSqlServerDb();
        try {
            Statement statement = conn.createStatement();
            int rs = statement.executeUpdate("insert into [dbo].[User] (FirstName,Email,EncryptedPassword) values ('" + firstName + "','" + email + "','" + encryptedPassword + "');");
            return true;
        } catch (SQLException e) {
            String exception = e.getMessage();

        }
        return false;

    }

    public Boolean insertBlowfishKey(String encrpytionKey, String email) {
        Connection conn = connectToSqlServerDb();
        try {
            Statement statement = conn.createStatement();
            int rs = statement.executeUpdate("insert into [dbo].[Blowfish] (BlowfishKey,User_UserId) values ('"+encrpytionKey+"',\n" +
                    "(select [dbo].[User].UserId from [dbo].[User] where [dbo].[User].Email = '"+email+"')\n" +
                    ");");
            return true;
        } catch (Exception e) {


        }
        return false;


    }

    public Connection connectToSqlServerDb() {
        Connection conn;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String conUrl = "jdbc:jtds:sqlserver://192.168.1.252:1433;databaseName=Koobook_db;user=sa;password=admin;";
            conn = DriverManager.getConnection(conUrl);
            return conn;
        } catch (Exception e) {
            Log.w("Error message: ", e.getMessage());
            return null;
        }

    }
}
