package dataaccess.sqlserver;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlServerDatabase {


    public boolean executeUpdateStatement(String query){
        Connection conn = connect();
        try {
            Statement statement = conn.createStatement();
            int rs = statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            String exception = e.getMessage();

        }
        return false;

    }

    public String executeSelectStatement(String query){
        Connection conn = connect();
        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            return rs.getString(1);
        }catch(Exception e){
            return null;

        }

    }


    public Connection connect() {
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
