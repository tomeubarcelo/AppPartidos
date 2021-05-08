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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
    
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    
    public static void main(String[] args) {
        
        Connection con;
        Statement stmt;
        ResultSet rs = null;
        Partido partido;
        
        //Creamos un array de objetos de la clase empleados
        Clasificacion arrayObjetos[]=new Clasificacion[10];
        
        HashMap<String, Clasificacion> clasificacion = new HashMap<>();
        
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XEPDB1","system","");
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
                            int iterador = 1;

                            if (rs.getString(5).equals(rs.getString(1)) ) {
                                System.out.println("Ha ganado el equipo local");
                                
                                Clasificacion equipoVictoriaLocal=new Clasificacion(rs.getString(1), 1, 0,0,3);
                                equipoVictoriaLocal.sumaEstadisticas(equipoVictoriaLocal);
                                //actualizar.actualizaPuntos(actualizar);
                                clasificacion.put(rs.getString(1), new Clasificacion(rs.getString(1),equipoVictoriaLocal.getGuanyats(), equipoVictoriaLocal.getEmpatats(),equipoVictoriaLocal.getPerduts(),equipoVictoriaLocal.getPunts()));
                                
                                Clasificacion equipoDerrotaVisitante=new Clasificacion(rs.getString(2), 0, 0,1,0);
                                clasificacion.put(rs.getString(2),new Clasificacion(rs.getString(2), equipoDerrotaVisitante.getGuanyats(), equipoDerrotaVisitante.getEmpatats(),equipoDerrotaVisitante.getPerduts(),equipoDerrotaVisitante.getPunts()));
                                
                                
                                //test
                                arrayObjetos[iterador]=new Clasificacion(rs.getString(1), 1, 0,0,3);
                                arrayObjetos[iterador+1]=new Clasificacion(rs.getString(2), 0, 0,1,0);

                            } else if(rs.getString(5).equals(rs.getString(2)) ) {
                                System.out.println("Ha ganado el equipo visitante");
                                
                                Clasificacion equipoDerrotaLocal=new Clasificacion(rs.getString(1), 0, 0,1,0);
                                equipoDerrotaLocal.sumaEstadisticas(equipoDerrotaLocal);
                                
                                clasificacion.put(rs.getString(1),new Clasificacion(rs.getString(1), equipoDerrotaLocal.getGuanyats(), equipoDerrotaLocal.getEmpatats(),equipoDerrotaLocal.getPerduts(),equipoDerrotaLocal.getPunts()));
                                
                                Clasificacion equipoVictoriaVisitante=new Clasificacion(rs.getString(2), 1, 0,0,3);
                                equipoVictoriaVisitante.sumaEstadisticas(equipoVictoriaVisitante);
                                clasificacion.put(rs.getString(2),new Clasificacion(rs.getString(2), equipoVictoriaVisitante.getGuanyats(), equipoVictoriaVisitante.getEmpatats(),equipoVictoriaVisitante.getPerduts(),equipoVictoriaVisitante.getPunts()));
                                
                                
                                //teast
                                arrayObjetos[iterador]=new Clasificacion(rs.getString(1), 0, 0,1,0);
                                arrayObjetos[iterador+1]=new Clasificacion(rs.getString(2), 1, 0,0,3);
                            } else if(rs.getString(5).equals("Empate") ) {
                                System.out.println("Ha habido empate");
                                
                                Clasificacion equipoEmpateLocal=new Clasificacion(rs.getString(1), 0, 1,0,1);
                                Clasificacion equipoEmpateVisitante=new Clasificacion(rs.getString(2), 0, 1,0,1);
                                
                                clasificacion.put(rs.getString(1), new Clasificacion(rs.getString(1),equipoEmpateLocal.getGuanyats(), equipoEmpateLocal.getEmpatats(),equipoEmpateLocal.getPerduts(),equipoEmpateLocal.getPunts()));
                                clasificacion.put(rs.getString(2),new Clasificacion(rs.getString(2), equipoEmpateVisitante.getGuanyats(), equipoEmpateVisitante.getEmpatats(),equipoEmpateVisitante.getPerduts(),equipoEmpateVisitante.getPunts()));
                                
                                //test
                                arrayObjetos[iterador]=new Clasificacion(rs.getString(1), 0, 1,0,1);
                                arrayObjetos[iterador+1]=new Clasificacion(rs.getString(2), 0, 1,0,1);
                            }
                        }
                        
                        //System.out.println("Equip-guanyats-emapatts-perduts-punts");
                        
                        
                   /* for (String key: clasificacion.keySet()){  
			System.out.println(key+ " "+clasificacion.get(key).getGuanyats() + " "+clasificacion.get(key).getEmpatats() + " "+clasificacion.get(key).getPerduts() + " " + clasificacion.get(key).getPunts());
                    }*/ 
                       /*final Map<String, Integer> sortedByCount = clasificacion.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                      */  
                        //mostrar hashmap ordenado
                       //System.out.println(clasificacion);
                        
                        //mostrar clasificacion lista ordenada
                       /* sortedByCount.keySet().forEach(key -> {  
                            System.out.println(key+ " - " + sortedByCount.get(key)+ "p" );
                        });*/
                        
                    //clasificacion 
                    List<Clasificacion> clasificacionOrdenada = new ArrayList<>(clasificacion.values());
                    
                    //ordenar por puntos
                    Collections.sort(clasificacionOrdenada, Comparator.comparing(Clasificacion::getPunts).reversed());

                    //mostrar clasifcacion
                    clasificacionOrdenada.forEach(p -> {
                        System.out.println(p.getEquipo()+" "+p.getGuanyats()+"G "+p.getEmpatats()+"E "
                                +p.getPerduts()+"P "+ ANSI_BLUE_BACKGROUND + ANSI_WHITE+p.getPunts()+"p"+ANSI_RESET);
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
