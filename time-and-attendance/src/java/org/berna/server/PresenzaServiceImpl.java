/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.berna.client.Azienda;
import org.berna.client.Lavoratore;
import org.berna.client.Presenza;

import org.berna.client.PresenzaService;

/**
 *
 * @author Berna
 */
public class PresenzaServiceImpl extends RemoteServiceServlet implements PresenzaService {

    @Override
    public boolean salva(Presenza presenza) {
        List risultati = null;
        risultati = PresenzaUtil.getList();
        if (risultati != null) {
            Iterator it = risultati.iterator();
            while (it.hasNext()) {
                Presenza pres = (Presenza) it.next();
                //Se la presenza in quella data e di quel tipo, è già stata inserita torna falso
                if (presenza.getIdLavoratore().equals(pres.getIdLavoratore()) && presenza.getDataPresenza().equals(pres.getDataPresenza()) && presenza.getTipo().equals(pres.getTipo())) {
                    return false;
                }
            }
        }
        PresenzaUtil.save(presenza);
        return true;
    }

    @Override
    public ArrayList carica(int mese, int anno, Lavoratore lavoratore) {
        List risultati = null;
        mese = mese-1;
        anno = anno-1900;
        Date date1 = new Date(anno, mese, 1);
        int giorno=DateUtils.getMaxDaysInMointh(anno, mese);
        Date date2 = new Date(anno, mese, giorno);
        risultati = PresenzaUtil.getList(date1, date2, lavoratore);
        ArrayList array = Utils.listToArray(risultati);
        System.out.println("Trovati: " + String.valueOf(array.size()));
        System.out.println("Caricamento dati eseguito con successo");
        return array;
    }

    @Override
    public ArrayList carica(int mese, int anno, Long idAzienda) {
        List risultati = null;
        mese = mese-1;
        anno = anno-1900;
        Date date1 = new Date(anno, mese, 1);
        int giorno=DateUtils.getMaxDaysInMointh(anno, mese);
        Date date2 = new Date(anno, mese, giorno);
        risultati = PresenzaUtil.getList(date1, date2, idAzienda);
        ArrayList array = Utils.listToArray(risultati);
        System.out.println("Trovati: " + String.valueOf(array.size()));
        System.out.println("Caricamento dati eseguito con successo");
        return array;
    }

    @Override
    public void cancella(ArrayList list) {
        if (list != null) {
        	System.out.println("delete size: " + list.size()); //<-- da eliminare la riga
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PresenzaUtil.remove((Presenza)it.next());
            }
        }
    }

    /*public void salva(ArrayList list) {
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PresenzaUtil.save((Presenza)it.next());
            }
        }
    }*/
    
}
