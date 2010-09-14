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
@RemoteServiceRelativePath("lavoratoreservice")
public interface LavoratoreService extends RemoteService {
    public ArrayList carica();
    public ArrayList carica(Azienda azienda);
    public boolean salva(Lavoratore lavoratore);
    public void cancella(ArrayList list);
    public void aggiorna(ArrayList list);
    public Lavoratore caricaLavoratore(Long id);
}
