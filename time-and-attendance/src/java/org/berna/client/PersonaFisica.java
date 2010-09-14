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
public class PersonaFisica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private String nome;
    @Basic
    private String cognome;
    @Basic
    private String cf;
    @Basic
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataNascita;

    public PersonaFisica() {
        //super();
    }

    public PersonaFisica(String nome, String cognome, String cf, Date dataNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.dataNascita = dataNascita;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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

    public static ArrayList<String> idToNome(Long id, ArrayList<PersonaFisica> list) {
        ArrayList array = new ArrayList();
        array.add("lista nulla");
        array.add("lista nulla");
        array.add("lista nulla");
        if (list != null) {
            array.set(0, "Non trovato");
            array.set(1, "Non trovato");
            array.set(2, "Non trovato");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PersonaFisica personaFisica = (PersonaFisica) it.next();
                if (personaFisica.getId().equals(id)) {
                    array.set(0, personaFisica.getNome());
                    array.set(1, personaFisica.getCognome());
                    array.set(2, personaFisica.getCf());
                    return array;
                }
            }
        }
        return array;
    }

    public static String idToCf(Long id, ArrayList<PersonaFisica> list) {
        String cf=null;
        if (list != null) {
            cf="non trovato";
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PersonaFisica personaFisica = (PersonaFisica) it.next();
                if (personaFisica.getId().equals(id)) {
                    cf=personaFisica.getCf();
                    return cf;
                }
            }
        }
        return cf;
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
        if (!(object instanceof PersonaFisica)) {
            return false;
        }
        PersonaFisica other = (PersonaFisica) object;
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
        return "org.berna.client.PersonaFisica [id=" + id + "]";
    }
}
