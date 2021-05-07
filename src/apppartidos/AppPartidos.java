/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apppartidos;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author tomeu
 * PROG10
 */
public class AppPartidos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Connection con;
        Statement stmt;
        ResultSet rs;
       
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XEPDB1","system","Mallorca-107");
            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from partidos");
            while (rs.next()) {                
                System.out.println(rs.getString("local") + " "+ rs.getString("golsLocal") + " vs "+rs.getString("visitant")+ " " + rs.getString("golsVisitant"));
            }
            stmt.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
        }
    }
    
}
