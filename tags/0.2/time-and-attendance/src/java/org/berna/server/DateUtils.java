/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.server;

import java.util.ArrayList;

/**
 *
 * @author Berna
 */
public class DateUtils {

    public static int getMaxDaysInMointh(int year, int month) {
        ArrayList array=new ArrayList();
        array=creaArray(year);
        int n=(Integer)array.get(month);
        return n;
    }

    public static ArrayList creaArray(int year){
        ArrayList list = new ArrayList();
        list.add(31); //Gennaio
        list.add(28); //Febbraio
        list.add(31); //Marzo
        list.add(30); //Aprile
        list.add(31); //Maggio
        list.add(30); //Giugno
        list.add(31); //Luglio
        list.add(31); //Agosto
        list.add(30); //Settembre
        list.add(31); //Ottobre
        list.add(30); //Novembre
        list.add(31); //Dicembre
        int resto = year%4;
        //Controllo anno bisestile
        if(resto!=0){
            return list;
        } else {
            list.set(1, 29);
            return list;
        }
    }


}
