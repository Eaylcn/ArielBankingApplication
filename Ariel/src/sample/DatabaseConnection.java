package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName = "ariel_db";
        String databaseUser = "root";
        String databasePassword = "password";// that is not my password
        String url = "jdbc:mysql://localhost/" + databaseName;

    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
    }catch (Exception e){
        e.printStackTrace();
        e.getCause();
    }

    return databaseLink;
    }

}
