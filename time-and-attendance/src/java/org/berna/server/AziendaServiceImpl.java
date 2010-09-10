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

import org.berna.client.AziendaService;

/**
 *
 * @author Berna
 */
public class AziendaServiceImpl extends RemoteServiceServlet implements AziendaService {

    public ArrayList carica() {
        ArrayList array = null;
        List risultati = AziendaUtil.getList();
        array = Utils.listToArray(risultati);
        System.out.println("Caricamento dati eseguito con successo");
        return array;
    }

    public void salva(ArrayList list) {
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                AziendaUtil.save((Azienda)it.next());
            }
        }
    }

    public void cancella(ArrayList list) {
        if (list != null) {
        	System.out.println("delete size: " + list.size()); //<-- da eliminare la riga
            Iterator it = list.iterator();
            while (it.hasNext()) {
                AziendaUtil.remove((Azienda)it.next());
            }
        }
    }

    /*public Azienda getAzienda(Long id) {
        List risultati = null;
        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM " + Azienda.class.getName());
        q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
        risultati = q.getResultList();
        //ArrayList array=Utils.listToArray(risultati);
        if (risultati != null) {
            Iterator it = risultati.iterator();
            while (it.hasNext()) {
                Azienda azienda = (Azienda)it.next();
                if(azienda.getId()==id){
                    System.out.println("Azienda trovata con successo");
                    return azienda;
                }
            }
        }
        return null;
    }*/
}
