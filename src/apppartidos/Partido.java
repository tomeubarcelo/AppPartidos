/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apppartidos;

/**
 *
 * @author tomeu
 * PROG10
 */
class Partido {
    private String local;
    private int golsLocal;
    private String visitant;
    private int golsVisitant;
    
    //CONSTRUCTOR POR DEFECTO
    public Partido(){
    }
    
    public Partido(String local, int golsLocal, String visitant, int golsVisitant ){
        this.local = local;
        this.golsLocal = golsLocal;
        this.visitant = visitant;
        this.golsVisitant = golsVisitant;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getGolsLocal() {
        return golsLocal;
    }

    public void setGolsLocal(int golsLocal) {
        this.golsLocal = golsLocal;
    }

    public String getVisitant() {
        return visitant;
    }

    public void setVisitant(String visitant) {
        this.visitant = visitant;
    }

    public int getGolsVisitant() {
        return golsVisitant;
    }

    public void setGolsVisitant(int golsVisitant) {
        this.golsVisitant = golsVisitant;
    }
      
}
