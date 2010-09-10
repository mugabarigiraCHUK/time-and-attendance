/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.berna.client.User;
import org.berna.client.Azienda;

/**
 *
 * @author Berna
 */
public class UserUtil {

    public static List getList() {
        List risultati = null;
        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM " + User.class.getName());
        q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
        risultati = q.getResultList();
        return risultati;
    }

    public static List getListLavoratori() {
        List risultati = null;
        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM User WHERE userrole='LAVORATORE'");
        q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
        risultati = q.getResultList();
        return risultati;
    }

    public static void save(User user) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            em.persist(user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void remove(User user) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            Query q = em.createQuery("DELETE FROM User u WHERE u.id = " + user.getId());
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    //quando aggiungo un azienda bisogna aggironare le aziende assegnate agli admin (tutte)
    //metodo non più utilizzato in quanto l'admin vede tutte le ditte a prescindere da quelle che potrebbe avere assegnato
    public static void aggiornaAziendeAdmin() {
        List utenti = null;
        List aziende = null;
        ArrayList<Long> idAziende = new ArrayList<Long>();

        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM User WHERE userrole='APP_ADMIN'");
        q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
        utenti = q.getResultList();
        System.out.println("Admin trovati: " + String.valueOf(utenti.size()));

        q = em.createQuery("SELECT FROM Azienda");
        q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
        aziende = q.getResultList();
        System.out.println("Aziende trovate: " + String.valueOf(aziende.size()));
        em.close();

        if (aziende != null) {
            Iterator it1 = aziende.iterator();
            while (it1.hasNext()) {
                Azienda azienda = (Azienda) it1.next();
                Long id = azienda.getId();
                idAziende.add(id);
            }
        }

        if (utenti != null) {
            Iterator it2 = utenti.iterator();
            while (it2.hasNext()) {
                User user = (User) it2.next();
                user.setIdAziende(idAziende);
                save(user);
            }
        }

    }
}
