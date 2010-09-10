/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.berna.client.PersonaFisica;

/**
 *
 * @author Berna
 */
public class PersonaFisicaUtil {

   public static List getList() {
        List risultati = null;
        try {
        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM " + PersonaFisica.class.getName());
        q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
        risultati = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return risultati;
    }

    public static void save(PersonaFisica personaFisica) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            em.persist(personaFisica);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

   public static void remove(PersonaFisica personaFisica) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            Query q = em.createQuery("DELETE FROM PersonaFisica p WHERE p.id = " + personaFisica.getId());
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

} // end class

