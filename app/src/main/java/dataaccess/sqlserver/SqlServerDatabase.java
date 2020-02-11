package dataaccess.sqlserver;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlServerDatabase {

    //This method works by first connecting to the JDBC driver to connect to the SQL server database. After the connection has been established, if will then create a statement instance. Using that instance,
    //it will execute the update which involves passing in the query from this method's argument. If the query was successfully executed then the method will return true. If an exception was a caught then the method
    //will return false.
    public boolean executeUpdateStatement(String query) {
        Connection conn = connect();
        try {
            Statement statement = conn.createStatement();
            int rs = statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            String exception = e.getMessage();
            return false;
        }

    }

    //This method works by first connecting to the JDBC driver to connect to the SQL server database. After the connection has been established, if will then create a statement instance. Using that instance,
    //it will execute the query which involves passing in the query from this method's argument which should return a result set. After iterating through result set, the value of enum type "Returns"(passed into the method's arguments) is checked
    //to see which type the result set should be whether thats a string or integer. If the return type is a string the "output value" string will be set to the string that was extracted from the result. If the return
    //is a integer then the "output value" string will be set to the integer value that was extracted from the result which is then converted to a string before assigning to the "output value" string. The method then returns
    //this output value string. If an exception was thrown during the main execution then the method will return null.
    public String executeSelectStatement(String query, Returns returnType) {
        Connection conn = connect();
        String outputValue = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {

                if (returnType.equals(Returns.String)) {
                    outputValue =  rs.getString(1);
                } else {
                    int value = rs.getInt(1);
                    outputValue = Integer.toString(value);
                }

            } return outputValue;

        } catch (Exception e) {
            return null;

        }

    }

    //Used to denote the return type of the ResultSet
    public enum Returns {
        Int, String
    }


    //This method works by using the JDBC diver to connect to the SQL server database. If the connection was successful then the method will return the Connection. Otherwise, it will return null
    public Connection connect() {
        Connection conn;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String conUrl = "jdbc:jtds:sqlserver://10.209.140.70:1433;databaseName=Koobook_db;user=sa;password=admin;";
            conn = DriverManager.getConnection(conUrl);
            return conn;
        } catch (Exception e) {
            Log.w("Error message: ", e.getMessage());
            return null;
        }

    }
}
