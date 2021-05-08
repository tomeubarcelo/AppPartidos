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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
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
        ResultSet rs = null;
        Partido partido;
        
        HashMap<String, Integer> clasificacion = new HashMap<>();
        
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XEPDB1","system","Mallorca-107");
            System.out.println("Connexio realitzada!");
            
            //crear sentencia
            stmt = con.createStatement();
            
            byte opcio = 0;
            //bucle para el menu 
            
            do {                
               opcio = menuOpcions();
                switch (opcio) {
                    case 1: 
                        //1a execucio
                        System.out.println("Introducir datos en tabla");
                        
                        //insert
                        Scanner sc = new Scanner (System.in);
                        System.out.println("Introduce equipo local: ");
                        String local = sc.next();
                        System.out.println("Introduce goles del equipo local: ");
                        int golsLocal = sc.nextInt();
                        System.out.println("Introduce equipo visitante: ");
                        String visitant = sc.next();
                        System.out.println("Introduce goles del equipo visitante: ");
                        int golsVisitant = sc.nextInt();
                        
                        partido = new Partido (local, golsLocal, visitant, golsVisitant);
                        String cadena = "insert into partidos values ('" + partido.getLocal() + "', '" + partido.getVisitant() + 
                            "' , "+ partido.getGolsLocal()+ ", " + partido.getGolsVisitant() + ")";
                        System.out.println(cadena);
                        stmt.execute(cadena);
                        
                        break;
                        
                    case 2:
                        //2A execucio
                        System.out.println("Mostrar partidos de la tabla");
                        rs = stmt.executeQuery("select local, visitant, golsLocal, golsVisitant, p.guanyador(local, golsLocal, visitant, golsVisitant) from partidos p");
                        while (rs.next()) {                
                            System.out.println(rs.getString(1) + " "+ rs.getString(3) + " vs "+rs.getString(4)+ " " + rs.getString(2)+ " -> " + rs.getString(5)  );
                        }
                        
                        break;
                        
                    case 3:
                        System.out.println("Generar clasificacion");
                        //3A execucio
                        rs = stmt.executeQuery("select local, visitant, golsLocal, golsVisitant, p.guanyador(local, golsLocal, visitant, golsVisitant) from partidos p");
                        while (rs.next()) {                
                            System.out.println(rs.getString(1) + " "+ rs.getString(3) + " vs "+rs.getString(4)+ " " + rs.getString(2)+ " -> " + rs.getString(5)  );
                            if (rs.getString(5).equals(rs.getString(1)) ) {
                                System.out.println("Ha ganado el equipo local");
                                clasificacion.put(rs.getString(1), 3);
                                clasificacion.put(rs.getString(2), 0);
                            } else if(rs.getString(5).equals(rs.getString(2)) ) {
                                System.out.println("Ha ganado el equipo visitante");
                                clasificacion.put(rs.getString(1), 0);
                                clasificacion.put(rs.getString(2), 3);
                            } else if(rs.getString(5).equals("Empate") ) {
                                System.out.println("Ha habido empate");
                                clasificacion.put(rs.getString(1), 1);
                                clasificacion.put(rs.getString(2), 1);
                            }
                            
                        }
                        System.out.println(clasificacion); 
                        final Map<String, Integer> sortedByCount = clasificacion.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                        
                        //mostrar hashmap ordenado
                        System.out.println(sortedByCount); 
                        
                        //mostrar clasificacion lista ordenada
                        sortedByCount.keySet().forEach(key -> {  
                            System.out.println(key+ " - " + sortedByCount.get(key)+ "p" );
                        });
                        break;

                        
                    case 4:
                        System.out.println("PROGRAMA FINALITZAT!!!");
                        break;
                    default:
                        System.out.println("Aquesta opció no existeix.");
                } 
            }while (opcio==1 || opcio == 2 || opcio==3 );
            

            //cerrar conexion
            stmt.close();
            rs.close();
            con.close();
            
        }catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
        } 
    }
    
    
        //MENÚ DE OPCIONS
    private static byte menuOpcions ()  {
        byte opcio=0;
        do{
            try{
                Scanner op = new Scanner (System.in);
                //menú d'opcions del programa
                System.out.println("1. Introducir datos en la tabla partidos.");
                System.out.println("2. Mostrar los partidos de la tabla con el resultado y el ganador o ”empate” usando la función de la BD.");
                System.out.println("3. Generar con las estructuras de java una clasificación obtenida a partir de la tabla partidos ordenada con el equipo con más puntos en primera posición (3 puntos por victoria, 1 punto por empate y 0 puntos por los partidos perdidos).");  
                System.out.println("4. Salir de la aplicación");
                System.out.println("Introdueix l'opcio elegida: ");
                opcio=op.nextByte();
                if (opcio < 1 || opcio > 4) {
                System.out.println("Escollir entre (1..4)!.");    
                }
            }    
            catch(Exception e){
                System.out.println(e.getMessage()+ " . Error al llegir del teclat(1..4)!.");
            }
            
        }while (opcio < 1 || opcio > 4);
        return opcio;
    } 
    
}
