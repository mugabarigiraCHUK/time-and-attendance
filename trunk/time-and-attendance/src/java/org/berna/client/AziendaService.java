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
@RemoteServiceRelativePath("aziendaservice")
public interface AziendaService extends RemoteService {
    public ArrayList carica();
    public void salva(ArrayList list);
    public void cancella(ArrayList list);
    //public Azienda getAzienda(Long id);
}
