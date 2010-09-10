package org.berna.server;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {

    private static EntityManagerFactory emfInstance = null;

    private EMF() {
    }

    public static EntityManagerFactory get() {
        if (emfInstance == null) {
            try {
                emfInstance = Persistence.createEntityManagerFactory("presenze");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return emfInstance;
    }
}
