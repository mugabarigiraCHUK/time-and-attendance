/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.berna.client.LoginService;
import org.berna.client.User;

/**
 *
 * @author Berna
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    public User autenticazione(User user) {
        List risultati = null;
        User userTrovato = null;
        risultati = UserUtil.getList();
        if (risultati != null) {
            Iterator it = risultati.iterator();
            while (it.hasNext()) {
                User u = (User) it.next();
                if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
                    userTrovato = u;
                }
            }
        }
        return userTrovato;
    }
}
