package org.berna.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private String username;
    @Basic
    private String password;
    @Basic
    private String email;
    @Basic
    private String userrole;
    @Basic
    private String idAziende; //id aziende assegnate, per account HR_ADMIN
    @Basic
    private Long idLavoratore; //id lavoratore assegnato, per account LAVORATORE


    public User() {
    }

    public User(String username, String password, String email, String userrole) {
        //this.idAziende=new ArrayList<Long>();
        this.username = username;
        this.password = password;
        this.email = email;
        this.userrole = userrole;
        this.idAziende=null;
        this.idLavoratore=null;
    }

    /*public ArrayList<Long> getIdAziende() {
    return idAziende;
    }

    public void setIdAziende(ArrayList<Long> idAziende) {
    this.idAziende = idAziende;
    }*/
    public List<String> getidAziende() {
        if(this.idAziende == null) {
            return null;
        }
        return Arrays.asList(idAziende.split(";"));
    }

    public void setIdAziende(ArrayList<Long> idAziende) {        
        if (idAziende != null) {
            this.idAziende = "";
            Iterator it = idAziende.iterator();
            while (it.hasNext()) {
                String azienda=String.valueOf(it.next());
                this.idAziende=this.idAziende+azienda+";";
            }
            this.idAziende=this.idAziende.substring(0, this.idAziende.length()-1);
        } else {
            this.idAziende = null;
        }
    }

    public Long getIdLavoratore() {
        return idLavoratore;
    }

    public void setIdLavoratore(Long idLavoratore) {
        this.idLavoratore = idLavoratore;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
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
        return "org.berna.client.User [id=" + id + "]";
    }
}
