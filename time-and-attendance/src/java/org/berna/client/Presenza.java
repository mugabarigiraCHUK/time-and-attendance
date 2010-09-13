/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author Berna
 */
@Entity
public class Presenza implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private Long idLavoratore;
    @Basic
    private Long idAzienda;
    @Basic
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataPresenza;
    @Basic
    private int quantita;
    @Basic
    private String tipo;
    @Basic
    private Long idSede; //da implementare

    public Presenza() {
    }

    public Presenza(Long idLavoratore, Long idAzienda, Date dataPresenza, int quantita, String tipo) {
        this.idLavoratore = idLavoratore;
        this.idAzienda=idAzienda;
        this.dataPresenza = dataPresenza;
        this.quantita = quantita;
        this.tipo = tipo;
    }

    public Date getDataPresenza() {
        return dataPresenza;
    }

    public void setDataPresenza(Date dataPresenza) {
        this.dataPresenza = dataPresenza;
    }

    public Long getIdAzienda() {
        return idAzienda;
    }

    public void setIdAzienda(Long idAzienda) {
        this.idAzienda = idAzienda;
    }

    public Long getIdLavoratore() {
        return idLavoratore;
    }

    public void setIdLavoratore(Long idLavoratore) {
        this.idLavoratore = idLavoratore;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Presenza)) {
            return false;
        }
        Presenza other = (Presenza) object;
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
        return "org.berna.client.Presenza [id=" + id + "]";
    }
}
