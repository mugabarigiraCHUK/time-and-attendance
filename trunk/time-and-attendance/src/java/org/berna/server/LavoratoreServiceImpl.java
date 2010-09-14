/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.berna.client.Azienda;
import org.berna.client.Lavoratore;

import org.berna.client.LavoratoreService;

/**
 *
 * @author Berna
 */
public class LavoratoreServiceImpl extends RemoteServiceServlet implements LavoratoreService {

    public boolean salva(Lavoratore lavoratore) {
        List risultati = null;
        risultati = LavoratoreUtil.getList();
        if (risultati != null) {
            Iterator it = risultati.iterator();
            while (it.hasNext()) {
                Lavoratore lav = (Lavoratore) it.next();
                //Se il lavoratore esiste torna falso
                if (lavoratore.getIdPersonaFisica().equals(lav.getIdPersonaFisica()) && lavoratore.getDataAssunzione().equals(lav.getDataAssunzione()) && lavoratore.getIdAzienda().equals(lav.getIdAzienda())) {
                    return false;
                }
            }
        }
        LavoratoreUtil.save(lavoratore);
        return true;
    }

    public ArrayList carica() {
        List risultati = null;
        risultati = LavoratoreUtil.getList();
        ArrayList array = Utils.listToArray(risultati);
        System.out.println("Caricamento dati eseguito con successo");
        return array;
    }

    @Override
    public ArrayList carica(Azienda azienda) {
        List risultati = null;
        risultati = LavoratoreUtil.getList(azienda);
        ArrayList array = Utils.listToArray(risultati);
        System.out.println("Caricamento dati eseguito con successo");
        return array;
    }

    public void cancella(ArrayList list) {
        if (list != null) {
            System.out.println("delete size: " + list.size()); //<-- da eliminare la riga
            Iterator it = list.iterator();
            while (it.hasNext()) {
                LavoratoreUtil.remove((Lavoratore) it.next());
            }
        }
    }

    public void aggiorna(ArrayList list) {
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                LavoratoreUtil.save((Lavoratore) it.next());
            }
        }
    }

    @Override
    public Lavoratore caricaLavoratore(Long id) {
        Lavoratore lavoratore = null;
        lavoratore = LavoratoreUtil.getLavoratore(id);
        System.out.println("Caricamento dati eseguito con successo");
        return lavoratore;
    }
}
