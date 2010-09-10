/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author Berna
 */
@RemoteServiceRelativePath("loginservice")
public interface LoginService extends RemoteService {
    public User autenticazione(User user);
}
