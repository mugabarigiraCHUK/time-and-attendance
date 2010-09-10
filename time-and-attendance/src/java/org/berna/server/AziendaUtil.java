/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.berna.client.Azienda;

/**
 *
 * @author Berna
 */
public class AziendaUtil {

    public static List getList() {
        List risultati = null;
        try {
            EntityManager em = EMF.get().createEntityManager();
            Query q = em.createQuery("SELECT FROM " + Azienda.class.getName());
            q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
            risultati = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return risultati;
    }

    public static void save(Azienda azienda) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            em.persist(azienda);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void remove(Azienda azienda) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            Query q = em.createQuery("DELETE FROM Azienda a WHERE a.id = " + azienda.getId());
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
