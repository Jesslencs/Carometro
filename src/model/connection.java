package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class connection {
	
	private Connection con;
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/dbcarometro";
    private String user = "jessdeveloper";
    private String password = "1234567";
    
    public Connection conectar() {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}