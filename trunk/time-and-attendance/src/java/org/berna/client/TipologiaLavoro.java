/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.client;

import java.util.ArrayList;

/**
 *
 * @author Utente
 */
public class TipologiaLavoro {
    private String nome;
    private int codice;

    public TipologiaLavoro(String nome, int codice) {
        this.nome = nome;
        this.codice = codice;
    }

    public int getCodice() {
        return codice;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static ArrayList generaTipologie(){
        ArrayList array= new ArrayList();
        array.add(new TipologiaLavoro("Lavoro ordinario", 0));
        array.add(new TipologiaLavoro("Lavoro straordinario (25%)", 1));
        array.add(new TipologiaLavoro("Lavoro straordinario (30%)", 2));
        array.add(new TipologiaLavoro("Lavoro festivo", 3));
        array.add(new TipologiaLavoro("Lavoro notturno", 4));
        array.add(new TipologiaLavoro("Lavoro festivo notturno", 5));
        array.add(new TipologiaLavoro("Lavoro straordinario festivo", 6));
        array.add(new TipologiaLavoro("Lavoro a turni", 7));
        return array;
    }

}
