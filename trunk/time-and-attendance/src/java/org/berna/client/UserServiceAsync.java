/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

/**
 *
 * @author Berna
 */
public interface UserServiceAsync {
    public void carica(AsyncCallback<ArrayList> callback);

    public void salva(ArrayList list, AsyncCallback<Void> callback);

    public void cancella(ArrayList list, AsyncCallback<Void> callback);

    public void caricaLavoratori(AsyncCallback<ArrayList> asyncCallback);
}
