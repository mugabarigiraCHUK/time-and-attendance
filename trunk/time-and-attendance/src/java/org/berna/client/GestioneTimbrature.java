/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Berna
 */
public class GestioneTimbrature extends LayoutContainer {

    private ContentPanel panel = null;
    private FormData formData = new FormData();
    ComboBox<BeanModel> comboAzienda = null;
    ListStore<BeanModel> storeAziende = new ListStore<BeanModel>();
    private AziendaServiceAsync dstoreSvcAzienda = GWT.create(AziendaService.class);
    List<String> aziendeDiCompetenza = new ArrayList<String>();
    private Status status = new Status();

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new CenterLayout());

        panel = new ContentPanel();
        panel.setBodyStyle("padding: 6px");
        panel.setFrame(true);
        panel.setHeading("Gestione timbrature");
        panel.setWidth(400);
        panel.setHeight(200);
        FormPanel form = new FormPanel();
        form = createForm();
        panel.add(form);

        //ToolBar statusBar = new ToolBar();
        //status.setWidth(350);
        //statusBar.add(status);
        //panel.setBottomComponent(statusBar);

        caricaDati();
        add(panel);
    }

    private FormPanel createForm() {
        FormPanel formUpload = new FormPanel();
        formUpload.setHeaderVisible(false);
        formUpload.setBorders(false);

        comboAzienda = new ComboBox<BeanModel>();
        comboAzienda.setFieldLabel("Azienda");
        comboAzienda.setEmptyText("Seleziona un'azienda...");
        comboAzienda.setDisplayField("denominazione");
        comboAzienda.setWidth(150);
        comboAzienda.setEditable(false);
        comboAzienda.setStore(storeAziende);
        comboAzienda.setTypeAhead(true);
        comboAzienda.setTriggerAction(TriggerAction.ALL);
        comboAzienda.setAllowBlank(false);
        formUpload.add(comboAzienda, formData);

        FileUploadField file = new FileUploadField();
        file.setName("import-file");
        file.setAllowBlank(false);
        file.setFieldLabel("Timbratura");
        formUpload.add(file, formData);

        Button crea = new Button("Aggiungi presenza");
        crea.setIcon(IconHelper.create("/resources/grafica/x16/add2.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        crea.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                //TODO importazione timbrature
            }
        });

        formUpload.setButtonAlign(HorizontalAlignment.CENTER);
        formUpload.addButton(crea);
        FormButtonBinding binding = new FormButtonBinding(formUpload);
        binding.addButton(crea);

        return formUpload;
    }

    public void caricaDati() {
        // Initialize the service proxy.
        if (dstoreSvcAzienda == null) {
            dstoreSvcAzienda = GWT.create(AziendaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            public void onSuccess(ArrayList result) {
                BeanModelFactory factory = BeanModelLookup.get().getFactory(Azienda.class);
                if (result != null) {
                    Iterator it1 = result.iterator();
                    while (it1.hasNext()) {
                        Azienda azienda = (Azienda) it1.next();
                        if (!(Login.loggedUser.getUserrole().equals("APP_ADMIN"))) {
                            if (aziendeDiCompetenza != null) {
                                Iterator it2 = aziendeDiCompetenza.iterator();
                                while (it2.hasNext()) {
                                    Long idAzienda = Long.parseLong((String) it2.next());
                                    if (azienda.getId().equals(idAzienda)) {
                                        BeanModel aziendaModel = factory.createModel(azienda);
                                        storeAziende.add(aziendaModel);
                                    }
                                }
                            }
                        } else {
                            BeanModel aziendaModel = factory.createModel(azienda);
                            storeAziende.add(aziendaModel);
                        }
                    }
                }
                comboAzienda.setStore(storeAziende);
                status.setStatus("Dati caricati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        aziendeDiCompetenza = Login.loggedUser.getidAziende();
        storeAziende.removeAll();
        dstoreSvcAzienda.carica(callback);
    }
}
