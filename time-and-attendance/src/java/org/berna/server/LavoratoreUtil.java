/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.berna.client.Azienda;
import org.berna.client.Lavoratore;

/**
 *
 * @author Berna
 */
public class LavoratoreUtil {

    public static List getList() {
        List risultati = null;
        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM " + Lavoratore.class.getName());
        q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
        risultati = q.getResultList();
        return risultati;
    }

    public static List getList(Azienda azienda) {
        List risultati = null;
        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM Lavoratore l WHERE l.idAzienda = " + azienda.getId());
        q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
        risultati = q.getResultList();
        return risultati;
    }

    public static void save(Lavoratore lavoratore) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            em.persist(lavoratore);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    static void remove(Lavoratore lavoratore) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            Query q = em.createQuery("DELETE FROM Lavoratore l WHERE l.id = " + lavoratore.getId());
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
