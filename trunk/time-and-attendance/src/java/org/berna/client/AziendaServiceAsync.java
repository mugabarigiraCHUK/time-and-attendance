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
public interface AziendaServiceAsync {

    public void carica(AsyncCallback<ArrayList> asyncCallback);

    public void salva(ArrayList list, AsyncCallback<Void> asyncCallback);

    public void cancella(ArrayList list, AsyncCallback<Void> asyncCallback);

    //public void getAzienda(Long id, AsyncCallback<Azienda> asyncCallback);
    
}
