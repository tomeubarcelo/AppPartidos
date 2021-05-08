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
public class Clasificacion {
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
    

}