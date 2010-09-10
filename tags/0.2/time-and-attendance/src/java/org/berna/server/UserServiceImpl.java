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

import org.berna.client.User;
import org.berna.client.UserService;

/**
 *
 * @author Berna
 */
public class UserServiceImpl extends RemoteServiceServlet implements UserService {

    public ArrayList carica() {
        ArrayList array = null;
        List risultati = UserUtil.getList();
        array = Utils.listToArray(risultati);
        System.out.println("Caricamento dati eseguito con successo");
        return array;
    }

    public void salva(ArrayList list) {
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                UserUtil.save((User)it.next());
            }
        }
    }

    public void cancella(ArrayList list) {
        if (list != null) {
        	System.out.println("delete size: " + list.size()); //<-- da eliminare la riga
            Iterator it = list.iterator();
            while (it.hasNext()) {
                UserUtil.remove((User)it.next());
            }
        }
    }

    @Override
    public ArrayList caricaLavoratori() {
        ArrayList array = null;
        List risultati = UserUtil.getListLavoratori();
        array = Utils.listToArray(risultati);
        System.out.println("Caricamento dati eseguito con successo");
        return array;
    }
}
