/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.berna.client.Azienda;
import org.berna.client.Lavoratore;
import org.berna.client.Presenza;

/**
 *
 * @author Berna
 */
public class PresenzaUtil {

    public static void save(Presenza presenza) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            em.persist(presenza);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static List getList() {
        List risultati = null;
        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM " + Presenza.class.getName());
        q.setHint("datanucleus.appengine.datastoreReadConsistency", "EVENTUAL");
        risultati = q.getResultList();
        return risultati;
    }

    public static List getList(Date data1, Date data2, Lavoratore lavoratore) {        
        List risultati = null;
        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM Presenza p WHERE p.idLavoratore = " + lavoratore.getId() + " AND p.dataPresenza BETWEEN :start AND :end");
        q.setParameter("start", data1, TemporalType.DATE);
        q.setParameter("end", data2, TemporalType.DATE);
        risultati = q.getResultList();
        return risultati;
    }

    public static List getList(Date data1, Date data2, Long idAzienda) {
        List risultati = null;
        EntityManager em = EMF.get().createEntityManager();
        Query q = em.createQuery("SELECT FROM Presenza p WHERE p.idAzienda = " + idAzienda + " AND p.dataPresenza BETWEEN :start AND :end");
        q.setParameter("start", data1, TemporalType.DATE);
        q.setParameter("end", data2, TemporalType.DATE);
        risultati = q.getResultList();
        return risultati;
    }

    static void remove(Presenza presenza) {
        EntityManager em = EMF.get().createEntityManager();
        try {
            Query q = em.createQuery("DELETE FROM Presenza p WHERE p.id = " + presenza.getId());
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
