/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.berna.client.PersonaFisicaService;
import org.berna.client.PersonaFisica;

/**
 *
 * @author Berna
 */
public class PersonaFisicaServiceImpl extends RemoteServiceServlet implements PersonaFisicaService {

    public ArrayList carica() {
        List risultati = null;
        risultati=PersonaFisicaUtil.getList();
        ArrayList array=Utils.listToArray(risultati);
        System.out.println("Caricamento dati eseguito con successo");
        return array;
    }

    public void salva(ArrayList list) {
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PersonaFisicaUtil.save((PersonaFisica) it.next());
            }
        }
    }

    public void cancella(ArrayList list) {
        if (list != null) {
        	System.out.println("delete size: " + list.size()); //<-- da eliminare la riga
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PersonaFisicaUtil.remove((PersonaFisica) it.next());
            }
        }
    }
}
