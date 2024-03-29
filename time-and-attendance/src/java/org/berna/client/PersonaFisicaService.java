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
@RemoteServiceRelativePath("PersonaFisicaService")
public interface PersonaFisicaService extends RemoteService {
    public ArrayList carica();
    public ArrayList carica(Long idProprietario);
    public void salva(ArrayList list);
    public void cancella(ArrayList list);
}
