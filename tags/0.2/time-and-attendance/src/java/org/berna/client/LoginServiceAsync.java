/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author Berna
 */
public interface LoginServiceAsync {
    public void autenticazione(User user, AsyncCallback<User> asyncCallback);
}
