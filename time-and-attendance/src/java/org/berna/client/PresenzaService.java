/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

/**
 *
 * @author Berna
 */
@RemoteServiceRelativePath("presenzaservice")
public interface PresenzaService extends RemoteService {
    public boolean salva(Presenza presenza);
    public ArrayList carica(int mese, int anno, Lavoratore lavoratore);
    public ArrayList carica(int mese, int anno, Long idAzienda);
    public void cancella(ArrayList list);
}
