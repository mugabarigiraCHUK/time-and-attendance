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
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
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
public class Dump extends LayoutContainer {

    private FormData formData=new FormData();
    private ContentPanel panel = null;
    ComboBox<BeanModel> comboAziende = null;
    List<String> aziendeDiCompetenza = null;
    SimpleComboBox<String> comboMese = null;
    SimpleComboBox<String> comboAnno = null;
    TextField<String> idAzienda = null;
    ListStore<BeanModel> storeAziende = new ListStore<BeanModel>();
    private AziendaServiceAsync dstoreSvc = GWT.create(AziendaService.class);

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new CenterLayout());

        panel = new ContentPanel();
        panel.setBodyStyle("padding: 6px");
        panel.setFrame(true);
        panel.setHeading("Esporta presenze");
        panel.setWidth(500);
        panel.setHeight(250);
        FormPanel form = createForm();
        panel.add(form);
        caricaDati();
        add(panel);
    }

    private FormPanel createForm() {
        final FormPanel panel = new FormPanel();
        panel.setFrame(true);
        panel.setHeaderVisible(false);
        String url = "/DownloadPresenzeServlet";
        panel.setAction(url);
       // panel.setEncoding(Encoding.MULTIPART);
        panel.setMethod(Method.GET);
        panel.setButtonAlign(HorizontalAlignment.CENTER);
        panel.setWidth(480);

        idAzienda = new TextField<String>();
        idAzienda.setName("idAzienda");
        idAzienda.setVisible(false);
        idAzienda.setAllowBlank(false);
        panel.add(idAzienda, formData);

        comboAziende = new ComboBox<BeanModel>();
        comboAziende.setName("azienda");
        comboAziende.setEmptyText("Seleziona un'azienda...");
        comboAziende.setEditable(false);
        comboAziende.setDisplayField("denominazione");
        comboAziende.setFieldLabel("Azienda");
        comboAziende.setWidth(200);
        comboAziende.setStore(storeAziende);
        comboAziende.setTypeAhead(true);
        comboAziende.setTriggerAction(TriggerAction.ALL);
        comboAziende.addListener(Events.SelectionChange,
                new Listener<SelectionChangedEvent<BeanModel>>() {

                    @Override
                    public void handleEvent(SelectionChangedEvent<BeanModel> be) {
                        BeanModel model = comboAziende.getValue();
                        Azienda azienda = model.getBean();
                        String str = String.valueOf(azienda.getId());
                        idAzienda.setValue(str);
                    }
                });
        panel.add(comboAziende, formData);

        comboMese = new SimpleComboBox<String>();
        comboMese.setName("mese");
        comboMese.setFieldLabel("Mese");
        comboMese.setForceSelection(true);
        comboMese.setEditable(false);
        comboMese.setWidth(150);
        comboMese.setTriggerAction(TriggerAction.ALL);
        comboMese.setEmptyText("Seleziona un mese...");
        comboMese.add("01");
        comboMese.add("02");
        comboMese.add("03");
        comboMese.add("04");
        comboMese.add("05");
        comboMese.add("06");
        comboMese.add("07");
        comboMese.add("08");
        comboMese.add("09");
        comboMese.add("10");
        comboMese.add("11");
        comboMese.add("12");
        panel.add(comboMese, formData);

        comboAnno = new SimpleComboBox<String>();
        comboAnno.setFieldLabel("Anno");
        comboAnno.setName("anno");
        comboAnno.setForceSelection(true);
        comboAnno.setEditable(false);
        comboAnno.setWidth(150);
        comboAnno.setTriggerAction(TriggerAction.ALL);
        comboAnno.setEmptyText("Seleziona un anno...");
        comboAnno.add("2010");
        comboAnno.add("2011");
        comboAnno.add("2012");
        comboAnno.add("2013");
        comboAnno.add("2014");
        comboAnno.add("2015");
        comboAnno.add("2016");
        comboAnno.add("2017");
        panel.add(comboAnno, formData);

        Button reset = new Button("Reset");
        reset.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                panel.reset();
            }
        });
        panel.addButton(reset);

        Button carica = new Button("Download");
        carica.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                panel.submit();
            }
        });
        panel.addButton(carica);
        FormButtonBinding binding = new FormButtonBinding(panel);
        binding.addButton(carica);

        return panel;
    }


    /*
    private FormPanel createForm2() {
        final FormPanel panel = new FormPanel();
        panel.setFrame(true);
        panel.setHeaderVisible(false);
        //String url = GWT.getModuleBaseURL() + "fileupload";
        String url = "/UploadServlet";
        panel.setAction(url);
        //panel.setAction(GWT.getModuleBaseURL() + "fileupload");
        panel.setEncoding(Encoding.MULTIPART);
        panel.setMethod(Method.POST);
        panel.setButtonAlign(HorizontalAlignment.CENTER);
        panel.setWidth(650);

        Radio radio = new Radio();
        radio.setBoxLabel("Esporta dati");
        radio.setValue(true);

        Radio radio2 = new Radio();
        radio2.setBoxLabel("Importa dati");

        RadioGroup radioGroup = new RadioGroup();
        radioGroup.setFieldLabel("Seleziona operazione");
        radioGroup.add(radio);
        radioGroup.add(radio2);
        //radioGroup.addPlugin(plugin);
        panel.add(radioGroup, formData);

        FileUploadField file = new FileUploadField();
        file.setName("import-file");
        file.setAllowBlank(false);
        file.setFieldLabel("File");
        panel.add(file, formData);

        Button reset = new Button("Reset");
        reset.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                panel.reset();
            }
        });
        panel.addButton(reset);

        Button carica = new Button("Carica");
        carica.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                //panel.submit();
                //if (!panel.isValid()) {
                //return;
                //}
                // normally would submit the form but for example no server set up to
                // handle the post
                // panel.submit();
                //MessageBox.info("Action", "Dati caricati", null);
            }
        });
        panel.addButton(carica);

        FormButtonBinding binding = new FormButtonBinding(panel);
        binding.addButton(carica);

        return panel;
    }*/
    
    public void caricaDati() {
        // Initialize the service proxy.
        if (dstoreSvc == null) {
            dstoreSvc = GWT.create(AziendaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            @Override
            public void onFailure(Throwable caught) {
                //status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
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
                //status.setStatus("Dati caricati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        aziendeDiCompetenza = Login.loggedUser.getidAziende();
        storeAziende.removeAll();
        dstoreSvc.carica(callback);
    }
} //end class
