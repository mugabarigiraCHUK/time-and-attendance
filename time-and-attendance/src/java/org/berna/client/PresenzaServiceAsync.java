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
public interface PresenzaServiceAsync {

    public void salva(Presenza presenza, AsyncCallback<java.lang.Boolean> asyncCallback);

    public void carica(int mese, int anno, Lavoratore lavoratore, AsyncCallback<ArrayList> asyncCallback);

    public void cancella(ArrayList list, AsyncCallback<Void> asyncCallback);

    public void carica(int mese, int anno, Azienda azienda, AsyncCallback<ArrayList> asyncCallback);


    
}
