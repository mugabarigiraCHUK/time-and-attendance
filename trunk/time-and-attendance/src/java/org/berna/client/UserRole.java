/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;


import java.io.Serializable;
import java.util.ArrayList;
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
public class UserRole implements Serializable {

    public static final long serialVersionUID = 1L;
    /*public static final int LAVORATORE = 0;
    public static final int HR_ADMIN = 1;
    public static final int APP_ADMIN = 2;*/
    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String encodedKey;

    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
    private String id;
     */

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;
*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Basic
    int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UserRole() {
    }

    public UserRole(int type) {
        this.type = type;
    }

    static public ArrayList<String> getList() {
        ArrayList list = new ArrayList();
        list.add("LAVORATORE");
        list.add("HR_ADMIN");
        list.add("APP_ADMIN");
        return list;
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
        if (!(object instanceof UserRole)) {
            return false;
        }
        UserRole other = (UserRole) object;
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
        return "org.berna.client.UserRole [id=" + id + "]";
    }
}
