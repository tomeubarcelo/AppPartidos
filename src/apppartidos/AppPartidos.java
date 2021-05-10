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
     //Se crea un ArrayList para guardar objetos de tipo Coche.
    static ArrayList<Clasificacion> clasificacionLista = new ArrayList();
    
    public static void main(String[] args) throws Exception {
        
        Connection con;
        Statement stmt;
        ResultSet rs = null;
        Partido partido;
        
        //Creamos un array de objetos de la clase clasificacion
        Clasificacion arrayObjetos[]=new Clasificacion[10];
        
        int arrayPuntos[] = null;
        
        HashMap<String, Clasificacion> clasificacion = new HashMap<>();
        
        Clasificacion actualizar = new Clasificacion();
        
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
                        if (!contieneSoloLetras(local)) {
                            throw new Exception("Solo debe contener letras");
                        }
                        
                        System.out.println("Introduce goles del equipo local: ");
                        int golsLocal = sc.nextInt();
                        
                        System.out.println("Introduce equipo visitante: ");
                        String visitant = sc.next();
                        if (!contieneSoloLetras(visitant)) {
                            throw new Exception("Solo debe contener letras");
                        }
                        
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
                            //System.out.println(rs.getString(1) + " "+ rs.getString(3) + " vs "+rs.getString(4)+ " " + rs.getString(2)+ " -> " + rs.getString(5)  );

                            if (rs.getString(5).equals(rs.getString(1)) ) { //comprueba que gana el equipo local
                                //System.out.println("Ha ganado el equipo local");
                                
                                //objeto para equipo con victoria local
                                int suma3 = 3;
                                Clasificacion equipoVictoriaLocal=new Clasificacion(rs.getString(1), 1, 0,0,suma3);
                                suma3 = suma3 + equipoVictoriaLocal.getPunts();
                                equipoVictoriaLocal.setPunts(suma3);
                                //añadimos valores al HashMap
                                clasificacion.put(rs.getString(1), new Clasificacion(rs.getString(1),equipoVictoriaLocal.getGuanyats(), equipoVictoriaLocal.getEmpatats(),equipoVictoriaLocal.getPerduts(),equipoVictoriaLocal.getPunts()));
                                                                        
                                //objeto para equipo con derrota visitante
                                int suma0 = 0;
                                Clasificacion equipoDerrotaVisitante=new Clasificacion(rs.getString(2), 0, 0,1,suma0);
                                
                                suma0 = suma0 + equipoDerrotaVisitante.getPunts();
                                equipoDerrotaVisitante.setPunts(suma0);
                                //añadimos valores al HashMap

                                clasificacion.put(rs.getString(2),new Clasificacion(rs.getString(2), equipoDerrotaVisitante.getGuanyats(), equipoDerrotaVisitante.getEmpatats(),equipoDerrotaVisitante.getPerduts(),equipoDerrotaVisitante.getPunts()));
                                
                                //boolean que comprueba si en el array de objetos ya existe el equipo local que gana
                                boolean trobatEquipVictoriaLocal = false;
                                for (int i=0;i<clasificacionLista.size();i++) {     
                                    if (clasificacionLista.get(i).getEquipo().equals(equipoVictoriaLocal.getEquipo())) {
                                        //sumar 3 puntos al equipo local por la victoria
                                        int sumaPuntos = 3;
                                        sumaPuntos = sumaPuntos + clasificacionLista.get(i).getPunts();
                                        clasificacionLista.get(i).setPunts(sumaPuntos);
                                        trobatEquipVictoriaLocal = true;
                                    } else{
                                        //no existe dicho equipo en el arrayList
                                        trobatEquipVictoriaLocal = false;
                                    }
                                }
                                if (!trobatEquipVictoriaLocal) { //si no existe lo añadimos
                                    clasificacionLista.add(equipoVictoriaLocal);
                                } else if (trobatEquipVictoriaLocal){
                                    /*
                                    int sumaPuntos = 3;
                                    sumaPuntos = sumaPuntos + equipoVictoriaLocal.getPunts();
                                    equipoVictoriaLocal.setPunts(sumaPuntos);
                                    */
                                }
                                
                                //boolean que comprueba si en el array de objetos ya existe el equipo visitante perdedor
                                boolean trobatEquipDerrotaVisitant = false;
                                for (int i=0;i<clasificacionLista.size();i++) {
                                    if (clasificacionLista.get(i).getEquipo().equals(equipoDerrotaVisitante.getEquipo())) {
                                        int sumaPuntos = 0;
                                        sumaPuntos = sumaPuntos + clasificacionLista.get(i).getPunts();
                                        clasificacionLista.get(i).setPunts(sumaPuntos);
                                        trobatEquipDerrotaVisitant = true;
                                    } else{
                                        trobatEquipDerrotaVisitant = false;
                                    } 
                                }
                                if (!trobatEquipDerrotaVisitant) {
                                    clasificacionLista.add(equipoDerrotaVisitante);
                                }else if(trobatEquipDerrotaVisitant){
                                    //equipoDerrotaVisitante.suma0Puntos();
                                }
                                
                                
                            } else if(rs.getString(5).equals(rs.getString(2)) ) {
                                //si el equipo visitante consigue la victoria
                                //System.out.println("Ha ganado el equipo visitante");
                                
                                //crear equipo local perdedor y añadirlo al Hashmap
                                int suma0 = 0;
                                Clasificacion equipoDerrotaLocal=new Clasificacion(rs.getString(1), 0, 0,1,suma0);
                                
                                suma0 = suma0 + equipoDerrotaLocal.getPunts();
                                equipoDerrotaLocal.setPunts(suma0);
                                clasificacion.put(rs.getString(1),new Clasificacion(rs.getString(1), equipoDerrotaLocal.getGuanyats(), equipoDerrotaLocal.getEmpatats(),equipoDerrotaLocal.getPerduts(),equipoDerrotaLocal.getPunts()));
                                
                                
                                //crear equipo visitante ganador y añadirlo al Hashmap
                                int suma3 = 3;
                                Clasificacion equipoVictoriaVisitante=new Clasificacion(rs.getString(2), 1, 0,0,suma3);
                                
                                suma3 = suma3 + equipoVictoriaVisitante.getPunts();
                                equipoVictoriaVisitante.setPunts(suma3);
                                clasificacion.put(rs.getString(2),new Clasificacion(rs.getString(2), equipoVictoriaVisitante.getGuanyats(), equipoVictoriaVisitante.getEmpatats(),equipoVictoriaVisitante.getPerduts(),equipoVictoriaVisitante.getPunts()));

                                
                                //boolean que comprueba si en el array de objetos ya existe el equipo local perdedor
                                boolean trobatEquipDerrotaLocal = false;
                                for (int i=0;i<clasificacionLista.size();i++) {
                                    if (clasificacionLista.get(i).getEquipo().equals(equipoDerrotaLocal.getEquipo())) {
                                        int sumaPuntos = 0;
                                        sumaPuntos = sumaPuntos + clasificacionLista.get(i).getPunts();
                                        clasificacionLista.get(i).setPunts(sumaPuntos);
                                        trobatEquipDerrotaLocal = true;
                                    } else{
                                        //System.out.println("NO Lo contiene EQUIPO DERROTA LOCAL");
                                        //clasificacionLista.add(equipoDerrotaLocal);
                                        trobatEquipDerrotaLocal = false;
                                    }
                                }
                                
                                if (!trobatEquipDerrotaLocal) {
                                    clasificacionLista.add(equipoDerrotaLocal);
                                }else if(trobatEquipDerrotaLocal){
                                    //equipoDerrotaLocal.suma0Puntos();
                                }
                                
                                //boolean que comprueba si en el array de objetos ya existe el equipo visitante ganador
                                boolean trobatEquipVictoriaVisitant= false;
                                for (int i=0;i<clasificacionLista.size();i++) {
                                    if (clasificacionLista.get(i).getEquipo().equals(equipoVictoriaVisitante.getEquipo())) {
                                        int sumaPuntos = 3;
                                        sumaPuntos = sumaPuntos + clasificacionLista.get(i).getPunts();
                                        clasificacionLista.get(i).setPunts(sumaPuntos);
                                        trobatEquipVictoriaVisitant= true;
                                    } else{
                                        //System.out.println("NO Lo contiene EQUIPO victoria visitante"); 
                                        trobatEquipVictoriaVisitant= false;
                                    }
                                }
                                if (!trobatEquipVictoriaVisitant) {
                                    clasificacionLista.add(equipoVictoriaVisitante);
                                }else if(trobatEquipVictoriaVisitant){
                                    //equipoVictoriaVisitante.suma3Puntos();
                                }

                            } else if(rs.getString(5).equals("Empate") ) {
                                //hay empate
                                //System.out.println("Ha habido empate");
                                
                                //equipo local
                                int suma1 = 1;
                                Clasificacion equipoEmpateLocal=new Clasificacion(rs.getString(1), 0, 1,0,suma1);
                                
                                suma1 = suma1 + equipoEmpateLocal.getPunts();
                                equipoEmpateLocal.setPunts(suma1);
                                clasificacion.put(rs.getString(1), new Clasificacion(rs.getString(1),equipoEmpateLocal.getGuanyats(), equipoEmpateLocal.getEmpatats(),equipoEmpateLocal.getPerduts(),equipoEmpateLocal.getPunts()));
                                
                                
                                //equipo visitante
                                int suma1V = 1;
                                Clasificacion equipoEmpateVisitante=new Clasificacion(rs.getString(2), 0, 1,0,suma1V);
                                
                                suma1V = suma1V + equipoEmpateVisitante.getPunts();
                                equipoEmpateVisitante.setPunts(suma1V);
                                clasificacion.put(rs.getString(2),new Clasificacion(rs.getString(2), equipoEmpateVisitante.getGuanyats(), equipoEmpateVisitante.getEmpatats(),equipoEmpateVisitante.getPerduts(),equipoEmpateVisitante.getPunts()));
                                
                                
                                //boolean que comprueba si en el array de objetos ya existe el equipo local con empate
                                boolean trobatEquipEmpateLocal= false;
                                for (int i=0;i<clasificacionLista.size();i++) {
                                    
                                    if (clasificacionLista.get(i).getEquipo().equals(equipoEmpateLocal.getEquipo())) {
                                        int sumaPuntos = 1;
                                        sumaPuntos = sumaPuntos + clasificacionLista.get(i).getPunts();
                                        clasificacionLista.get(i).setPunts(sumaPuntos);
                                        trobatEquipEmpateLocal= true;
                                    } else{
                                        //System.out.println("NO lo continee equipoEmpateLocal");
                                        trobatEquipEmpateLocal= false;
                                    }
                                }
                                if (!trobatEquipEmpateLocal) {
                                    clasificacionLista.add(equipoEmpateLocal);
                                }else if(trobatEquipEmpateLocal){
                                    //equipoEmpateLocal.suma1Punto();
                                }
                                
                                //boolean que comprueba si en el array de objetos ya existe el equipo visitante con empate
                                boolean trobatEquipEmpateVisitant= false;
                                for (int i=0;i<clasificacionLista.size();i++) {
                                    
                                    if (clasificacionLista.get(i).getEquipo().equals(equipoEmpateVisitante.getEquipo())) {
                                        //System.out.println("lo continee equipoEmpateLocal");
                                        int sumaPuntos = 1;
                                        sumaPuntos = sumaPuntos + clasificacionLista.get(i).getPunts();
                                        clasificacionLista.get(i).setPunts(sumaPuntos);
                                        trobatEquipEmpateVisitant= true;
                                    } else{
                                        //System.out.println("NO lo continee equipoEmpateLocal");
                                        trobatEquipEmpateVisitant= false;
                                    }
                                }
                                if (!trobatEquipEmpateVisitant) {
                                    clasificacionLista.add(equipoEmpateVisitante);
                                }else if(trobatEquipEmpateVisitant){
                                    //equipoEmpateVisitante.suma1Punto();
                                }

                            }
                        }
                        
                   //System.out.println("Equip-guanyats-emapatts-perduts-punts");

                    //clasificacion 
                    List<Clasificacion> clasificacionOrdenada = new ArrayList<>(clasificacion.values());
                    
                    //ordenar por puntos
                    Collections.sort(clasificacionOrdenada, Comparator.comparing(Clasificacion::getPunts).reversed());

                    //mostrar clasifcacion
                    clasificacionOrdenada.forEach(p -> {
                        System.out.println(p.getEquipo()+" "+p.getGuanyats()+"G "+p.getEmpatats()+"E "
                                +p.getPerduts()+"P "+ ANSI_BLUE_BACKGROUND + ANSI_WHITE+p.getPunts()+"p"+ANSI_RESET);
                   });
                    

                       // System.out.println(clasificacionLista.size());
                 for(int i = 0; i< clasificacionLista.size(); i++)
                    System.out.println(clasificacionLista.get(i).getEquipo() + clasificacionLista.get(i).getPunts());     
                
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
    
        //metodo que comprueba si un valor es numerico o no
    public static boolean isNumeric(String cadena){  
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException e){	
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    //metodo para comprobar si un string solo contiene letras
    public static boolean contieneSoloLetras(String cadena) {
        //metodo para comprobar si una cadena contiene solamente letras
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
                return false;
            }
        }
        return true;
    }
}
