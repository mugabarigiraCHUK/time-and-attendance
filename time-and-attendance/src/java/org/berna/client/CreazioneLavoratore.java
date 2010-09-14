/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Iterator;

public class CreazioneLavoratore extends LayoutContainer {

    private FormBinding formBindings;
    private AziendaServiceAsync dstoreSvcAzienda = GWT.create(AziendaService.class);
    private PersonaFisicaServiceAsync dstoreSvcPF = GWT.create(PersonaFisicaService.class);
    private LavoratoreServiceAsync dstoreSvcLAV = GWT.create(LavoratoreService.class);
    //private UserServiceAsync dstoreSvcUser = GWT.create(UserService.class);
    ListStore<BeanModel> storePF = new ListStore<BeanModel>();
    ListStore<BeanModel> storeAziende = new ListStore<BeanModel>();
    //ListStore<BeanModel> storeUser = new ListStore<BeanModel>();
    private Status status = new Status();
    private FormData formData = new FormData();
    SimpleComboBox<String> qualifica = null;
    MyDateField dAss = null;
    TextField<String> idPersonaFisica = null;
    TextField<String> nome = null;
    TextField<String> cognome = null;
    ComboBox<BeanModel> comboAzienda = null;
    //ComboBox<BeanModel> comboUser = null;
    List<String> aziendeDiCompetenza = null;

    @SuppressWarnings("unchecked")
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setStyleAttribute("margin", "10px");
        setLayout(new CenterLayout());

        ContentPanel cp = new ContentPanel();

        cp.setHeading("Creazione lavoratore");
        cp.setFrame(true);
        cp.setSize(Consts.LarghezzaFinestra, Consts.AltezzaFinestra);
        cp.setLayout(new RowLayout(Orientation.HORIZONTAL));

        final Grid<BeanModel> grid = createGrid();
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        grid.getSelectionModel().addListener(Events.SelectionChange,
                new Listener<SelectionChangedEvent<BeanModel>>() {

                    @Override
                    public void handleEvent(SelectionChangedEvent<BeanModel> be) {
                        if (be.getSelection().size() > 0) {
                            qualifica.clear();
                            dAss.clear();
                            formBindings.bind((ModelData) be.getSelection().get(0));
                        } else {
                            formBindings.unbind();
                        }
                    }
                });
        cp.add(grid, new RowData(.6, 1));
        FormPanel panel = createForm();
        formBindings = new FormBinding(panel, true);
        formBindings.setStore((Store) grid.getStore());

        cp.add(panel, new RowData(.4, 1));

        ToolBar statusBar = new ToolBar();
        status.setWidth(350);
        statusBar.add(status);
        cp.setBottomComponent(statusBar);

        add(cp);
    }

    private FormPanel createForm() {
        FormPanel panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setBorders(true);

        idPersonaFisica = new TextField<String>();
        idPersonaFisica.setName("id");
        idPersonaFisica.setFieldLabel("Id PF");
        idPersonaFisica.setVisible(false);
        idPersonaFisica.setAllowBlank(false);
        panel.add(idPersonaFisica, formData);

        nome = new TextField<String>();
        nome.setName("nome");
        nome.setFieldLabel("Nome");
        nome.setEnabled(false);
        nome.setAllowBlank(false);
        panel.add(nome, formData);

        cognome = new TextField<String>();
        cognome.setName("cognome");
        cognome.setFieldLabel("Cognome");
        cognome.setEnabled(false);
        cognome.setAllowBlank(false);
        panel.add(cognome, formData);

        FieldSet fieldSet = new FieldSet();
        fieldSet.setHeading("Dati lavoratore");
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(75);
        fieldSet.setLayout(layout);

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
        fieldSet.add(comboAzienda, formData);

        qualifica = new SimpleComboBox<String>();
        qualifica.setFieldLabel("Qualifica");
        qualifica.setName("qualifica");
        qualifica.setAllowBlank(false);
        qualifica.setForceSelection(true);
        qualifica.setEditable(false);
        qualifica.setWidth(150);
        qualifica.setTriggerAction(TriggerAction.ALL);
        qualifica.setEmptyText("Seleziona una qualifica...");
        qualifica.add("Operaio");
        qualifica.add("Impiegato");
        qualifica.add("Dirigente");
        qualifica.add("Apprendista non assic.");
        qualifica.add("Apprendista assic.");
        qualifica.add("Lavoratore a domicilio");
        qualifica.add("Equiparato o intermedio");
        qualifica.add("Viaggiatore o piazzista");
        qualifica.add("Dirigente");
        qualifica.add("Atipica");
        qualifica.add("Lavoratore domestico");
        qualifica.add("Pilota");
        qualifica.add("Pilota in addestramento");
        qualifica.add("Pilota collaudatore");
        qualifica.add("Tecnico di volo");
        qualifica.add("Tecnico di volo in addestr.");
        qualifica.add("Tecnico di volo per collaudi");
        qualifica.add("Assistente di volo");
        qualifica.add("Giornalista");
        qualifica.add("Lavoratore quadro");
        qualifica.add("Apprendista qualif. impiegato");
        qualifica.add("Lav.autonomo spettacolo");
        qualifica.add("Apprendista qualif. operaio");
        qualifica.add("Lav. escluso da contribuzione prev. e assist.");

        fieldSet.add(qualifica, formData);

        dAss = new MyDateField();
        dAss.setName("dataAssunzione");
        dAss.setFieldLabel("Assunzione");
        dAss.setAllowBlank(false);
        fieldSet.add(dAss, formData);

        /*FieldSet userFieldSet = new FieldSet();
        userFieldSet.setHeading("Associa un account");
        userFieldSet.setCheckboxToggle(true);
        FormLayout layout2 = new FormLayout();
        layout2.setLabelWidth(75);
        userFieldSet.setLayout(layout2);
        comboUser = new ComboBox<BeanModel>();
        comboUser.setFieldLabel("Username");
        comboUser.setEmptyText("Seleziona un username...");
        comboUser.setDisplayField("username");
        comboUser.setWidth(150);
        comboUser.setEditable(false);
        comboUser.setStore(storeUser);
        comboUser.setTypeAhead(true);
        comboUser.setTriggerAction(TriggerAction.ALL);
        userFieldSet.add(comboUser);
        fieldSet.add(userFieldSet, formData);*/

        panel.add(fieldSet);

        Button crea = new Button("Crea lavoratore");
        crea.setIcon(IconHelper.create("/resources/grafica/x16/add2.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        crea.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {

                String str = idPersonaFisica.getValue();
                Long idPersonaFisica = Long.parseLong(str);
                Azienda azienda = comboAzienda.getValue().getBean();
                Long idAzienda = azienda.getId();
                Lavoratore nuovoLavoratore = new Lavoratore(qualifica.getSimpleValue(), dAss.getValue(), idPersonaFisica, /*Login.loggedUser.getIdAzienda()*/ idAzienda);
                salvaLavoratore(nuovoLavoratore);
            }
        });

        panel.addButton(crea);
        panel.setButtonAlign(HorizontalAlignment.CENTER);
        FormButtonBinding binding = new FormButtonBinding(panel);
        binding.addButton(crea);

        return panel;
    }

    private Grid<BeanModel> createGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("nome");
        column.setHeader("Nome");
        column.setWidth(200);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("cognome");
        column.setHeader("Cognome");
        column.setWidth(200);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("cf");
        column.setHeader("Codice Fiscale");
        column.setWidth(220);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("dataNascita");
        column.setHeader("Data nascita");
        column.setWidth(120);
        column.setDateTimeFormat(DateTimeFormat.getMediumDateFormat());
        configs.add(column);

        storePF.setMonitorChanges(true);
        caricaDatiPersoneFisiche();

        ColumnModel cm = new ColumnModel(configs);

        Grid<BeanModel> grid = new Grid<BeanModel>(storePF, cm);
        grid.getView().setEmptyText("Inserire almeno una persona fisica");
        grid.setBorders(false);
        grid.setAutoExpandColumn("nome");
        grid.setBorders(true);

        return grid;
    }

    public void caricaDatiPersoneFisiche() {
        // Initialize the service proxy.
        if (dstoreSvcPF == null) {
            dstoreSvcPF = GWT.create(PersonaFisicaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
            public void onSuccess(ArrayList result) {
                //personeFisiche = result;
                BeanModelFactory factory = BeanModelLookup.get().getFactory(PersonaFisica.class);
                if (result != null) {
                    Iterator it = result.iterator();
                    while (it.hasNext()) {
                        Object personaFisica = it.next();
                        BeanModel personaFisicaModel = factory.createModel(personaFisica);
                        storePF.add(personaFisicaModel);
                    }
                }
                caricaAziende();
                //status.setStatus("Dati caricati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        //personeFisiche.clear();
        storePF.removeAll();
        dstoreSvcPF.carica(callback);
    }

    private void caricaAziende() {
        // Initialize the service proxy.
        if (dstoreSvcAzienda == null) {
            dstoreSvcAzienda = GWT.create(AziendaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
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
                comboAzienda.setStore(storeAziende);
                status.setStatus("Dati caricati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        aziendeDiCompetenza = Login.loggedUser.getidAziende();
        storeAziende.removeAll();
        dstoreSvcAzienda.carica(callback);
    }

    /*private void caricaUsers() {
    // Initialize the service proxy.
    if (dstoreSvcUser == null) {
    dstoreSvcUser = GWT.create(UserService.class);
    }

    AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

    public void onFailure(Throwable caught) {
    status.setStatus("Problemi di comunicazione col server", baseStyle);
    }

    public void onSuccess(ArrayList result) {
    //users = result;
    //caricaAziende();
    BeanModelFactory factory = BeanModelLookup.get().getFactory(User.class);
    if (result != null) {
    Iterator it = result.iterator();
    while (it.hasNext()) {
    Object user = it.next();
    BeanModel userModel = factory.createModel(user);
    storeUser.add(userModel);
    }
    }
    status.setStatus("Dati caricati con successo", baseStyle);
    }
    };
    // Make the call to the stock price service.
    storeUser.removeAll();
    dstoreSvcUser.caricaLavoratori(callback);
    }*/
    private void salvaLavoratore(Lavoratore lavoratore) {
        // Initialize the service proxy.
        if (dstoreSvcLAV == null) {
            dstoreSvcLAV = GWT.create(LavoratoreService.class);
        }
        AsyncCallback<java.lang.Boolean> callback = new AsyncCallback<java.lang.Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi col server", baseStyle);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result == true) {
                    status.setStatus("Lavoratore salvato con successo", baseStyle);
                } else {
                    status.setStatus("Lavoratore già inserito", baseStyle);
                }
            }
        };
        // Make the call to the stock price service.
        dstoreSvcLAV.salva(lavoratore, callback);
    }
}
