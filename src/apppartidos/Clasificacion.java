/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apppartidos;

/**
 *
 * @author tomeu
 */
public class Clasificacion implements Comparable<Clasificacion> {
    private int posicio;
    private String equipo;
    private int guanyats;
    private int empatats;
    private int perduts;
    private int punts;
    
    //CONSTRUCTOR POR DEFECTO
    public Clasificacion(){
    }

    public Clasificacion(String equipo, int guanyats, int empatats, int perduts, int punts ){

        this.equipo = equipo;
        this.guanyats = guanyats;
        this.empatats = empatats;
        this.perduts = perduts;
        this.punts = punts;
    }
    public void suma3Puntos(){
        this.punts += 3;
    }
    public void suma0Puntos(){
        this.punts += 0;
    }
    public void suma1Punto(){
        this.punts += 1;
    }
    public void sumaEstadisticas(Clasificacion sumaEstadisticas)
    {
        guanyats += sumaEstadisticas.getGuanyats();
        empatats += sumaEstadisticas.getEmpatats();
        perduts += sumaEstadisticas.getPerduts();
        punts += sumaEstadisticas.getPunts();
    }
    public void actualizaPuntos (Clasificacion sumaEstadisticas){
        punts += sumaEstadisticas.getPunts();
    }
    public int getPosicio() {
        return posicio;
    }

    public void setPosicio(int posicio) {
        this.posicio = posicio;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public int getGuanyats() {
        return guanyats;
    }

    public void setGuanyats(int guanyats) {
        this.guanyats = guanyats;
    }

    public int getEmpatats() {
        return empatats;
    }

    public void setEmpatats(int empatats) {
        this.empatats = empatats;
    }

    public int getPerduts() {
        return perduts;
    }

    public void setPerduts(int perduts) {
        this.perduts = perduts;
    }

    public int getPunts() {
        return punts;
    }

    public void setPunts(int punts) {
        this.punts = punts;
    }

    public int compareTo(Clasificacion o) {
            if (punts < o.punts) {
                return -1;
            }
            if (punts > o.punts) {
                return 1;
            }
            return 0;
        }
    

}
