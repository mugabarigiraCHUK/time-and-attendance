/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
public class Lavoratore implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private String qualifica;
    @Basic
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataAssunzione;
    @Basic
    private Long idAzienda;
    @Basic
    private Long idPersonaFisica;

    public Lavoratore() {
    }

    public Lavoratore(String qualifica, Date dataAssunzione, Long idPersonaFisica, Long idAzienda) {
        this.qualifica = qualifica;
        this.dataAssunzione = dataAssunzione;
        this.idPersonaFisica = idPersonaFisica;
        this.idAzienda = idAzienda;
    }

    public Long getIdAzienda() {
        return idAzienda;
    }

    public void setIdAzienda(Long idAzienda) {
        this.idAzienda = idAzienda;
    }

    public Date getDataAssunzione() {
        return dataAssunzione;
    }

    public void setDataAssunzione(Date dataAssunzione) {
        this.dataAssunzione = dataAssunzione;
    }

    public Long getIdPersonaFisica() {
        return idPersonaFisica;
    }

    public void setIdPersonaFisica(Long idPersonaFisica) {
        this.idPersonaFisica = idPersonaFisica;
    }

    public String getQualifica() {
        return qualifica;
    }

    public void setQualifica(String qualifica) {
        this.qualifica = qualifica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static Long idToidPersonaFisica(Long id, ArrayList<Lavoratore> list) {
        Long idPF = null;
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
               Lavoratore lavoratore = (Lavoratore) it.next();
                if (lavoratore.getId().equals(id)) {
                    idPF = lavoratore.getIdPersonaFisica();
                    return idPF;
                }
            }
        }
        return idPF;
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
        if (!(object instanceof Lavoratore)) {
            return false;
        }
        Lavoratore other = (Lavoratore) object;
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
        return "org.berna.client.Lavoratore [id=" + id + "]";
    }
}
