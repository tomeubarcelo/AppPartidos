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
        Partido partido;
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XEPDB1","system","Mallorca-107");
            stmt = con.createStatement();
            rs = stmt.executeQuery("select local, visitant, golsLocal, golsVisitant, p.guanyador(local, golsLocal, visitant, golsVisitant) from partidos p");
            while (rs.next()) {                
                System.out.println(rs.getString(1) + " "+ rs.getString(3) + " vs "+rs.getString(4)+ " " + rs.getString(2)+ " -> " + rs.getString(5)  );
            }
            
            //insert
            /*partido = new Partido ("Atletico de Madrid", 2, "Sevilla", 2);
            String cadena = "insert into partidos values ('" + partido.getLocal() + "', '" + partido.getVisitant() + "' , "+
                    partido.getGolsLocal()+ ", " + partido.getGolsVisitant() + ")";
            System.out.println(cadena);
            stmt.execute(cadena);
            */
            //cerrar conexion
            stmt.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
        }
    }
    
}
