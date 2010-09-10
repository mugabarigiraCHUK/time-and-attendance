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
public interface LavoratoreServiceAsync {
    public void salva(Lavoratore lavoratore, AsyncCallback<java.lang.Boolean> callback);

    public void carica(AsyncCallback<ArrayList> asyncCallback);

    public void cancella(ArrayList list, AsyncCallback<Void> asyncCallback);

    public void aggiorna(ArrayList list, AsyncCallback<Void> asyncCallback);

    public void carica(Azienda azienda, AsyncCallback<ArrayList> asyncCallback);
}
