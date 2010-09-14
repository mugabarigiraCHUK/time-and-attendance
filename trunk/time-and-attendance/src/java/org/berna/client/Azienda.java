/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Berna
 */
@Entity
public class Azienda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private String nome;
    @Basic
    private String cognome;
    @Basic
    private String ragioneSociale;
    @Basic
    private String cf;
    @Basic
    private String piva;
    @Basic
    private String denominazione;

    public Azienda() {
    }

    public Azienda(String nome, String cognome, String ragioneSociale, String cf, String piva, String denominazione) {
        this.nome = nome;
        this.cognome = cognome;
        this.ragioneSociale = ragioneSociale;
        this.cf = cf;
        this.piva = piva;
        this.denominazione = denominazione;

    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static String idToDenominazione(Long id, ArrayList<Azienda> list) {
        String denominazione = "Lista nulla";
        if (list != null) {
            denominazione = "Non trovato";
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Azienda azienda = (Azienda) it.next();
                if (azienda.getId().equals(id)) {
                    denominazione = azienda.getDenominazione();
                    return denominazione;
                }
            }
        }
        return denominazione;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        if (id == null) {
            hash = super.hashCode();
        } else {
            hash = id.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Azienda)) {
            return false;
        }
        Azienda other = (Azienda) object;
        if (this.id == null || other.id == null) {
            return false;
        } else if (this.id.equals(other.id)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "org.berna.client.Azienda [id=" + id + "]";
    }
}
