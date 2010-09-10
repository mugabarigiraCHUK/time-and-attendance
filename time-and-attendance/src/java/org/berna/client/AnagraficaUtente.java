package org.berna.client;

import com.extjs.gxt.ui.client.data.ModelData;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelProcessor;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DualListField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AnagraficaUtente extends LayoutContainer {

    private AziendaServiceAsync dstoreSvcAzienda = GWT.create(AziendaService.class);
    private UserServiceAsync dstoreSvcUser = GWT.create(UserService.class);
    private LavoratoreServiceAsync dstoreSvcLAV = GWT.create(LavoratoreService.class);
    ListStore<BeanModel> storeUtenti = new ListStore<BeanModel>();
    ArrayList<User> users = new ArrayList<User>();
    ArrayList<String> userRoles = new ArrayList<String>();
    ListStore<BeanModel> storeLavoratori = new ListStore<BeanModel>();
    ListStore<BeanModel> storeAziende = new ListStore<BeanModel>();
    ListStore<BeanModel> storeAziendeAssegnate = new ListStore<BeanModel>();
    ListStore<BeanModel> storeAziendeNonAssegnate = new ListStore<BeanModel>();
    ArrayList<Azienda> aziende = new ArrayList<Azienda>();
    CheckBoxSelectionModel<BeanModel> sm = new CheckBoxSelectionModel<BeanModel>();
    private Status status = new Status();
    SimpleComboBox<String> comboRole = null;
    User userSelezionato = null;
    DualListField<BeanModel> lists = null;
    ArrayList<BeanModel> listBean = null;
    ComboBox<BeanModel> comboAzienda = null;
    ComboBox<BeanModel> comboLavoratore = null;
    private FormData formData = new FormData("60%");

    @Override
    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);
        setLayout(new FlowLayout(10));

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        configs.add(sm.getColumn());

        ColumnConfig column = new ColumnConfig();
        column.setId("username");
        column.setHeader("Username");
        column.setWidth(150);

        TextField<String> text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        configs.add(column);

        column = new ColumnConfig();
        column.setId("password");
        column.setHeader("Password");
        column.setWidth(150);

        text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        configs.add(column);

        column = new ColumnConfig();
        column.setId("email");
        column.setHeader("E-Mail");
        column.setWidth(150);

        text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        configs.add(column);

        column = new ColumnConfig();
        column.setId("idLavoratore");
        column.setHeader("Lavoratore assegnato");
        column.setWidth(150);
        configs.add(column);

        comboRole = new SimpleComboBox<String>();
        comboRole.setForceSelection(true);
        comboRole.setTriggerAction(TriggerAction.ALL);
        comboRole.setEditable(false);
        userRoles = UserRole.getList();
        comboRole.add(userRoles);

        CellEditor editorRole = new CellEditor(comboRole) {

            @Override
            public Object preProcessValue(Object value) {
                if (value == null) {
                    return value;
                }
                return comboRole.findModel(value.toString());
            }

            @Override
            public Object postProcessValue(Object value) {
                if (value == null) {
                    return value;
                }
                if (((ModelData) value).get("value").equals("APP_ADMIN")) {
                    storeAziendeAssegnate.add(listBean);
                    storeAziendeNonAssegnate.removeAll();
                    lists.disable();
                    comboAzienda.disable();
                    comboLavoratore.disable();
                }
                if (((ModelData) value).get("value").equals("HR_ADMIN")) {
                    lists.enable();
                    comboAzienda.disable();
                    comboLavoratore.disable();
                }
                if (((ModelData) value).get("value").equals("LAVORATORE")) {
                    storeAziendeNonAssegnate.add(listBean);
                    storeAziendeAssegnate.removeAll();
                    lists.disable();
                    comboAzienda.enable();
                    comboLavoratore.enable();
                }
                return ((ModelData) value).get("value");
            }
        };

        column = new ColumnConfig();
        column.setId("userrole");
        column.setHeader("Livello");
        column.setWidth(130);
        column.setEditor(editorRole);
        configs.add(column);

        caricaAziende();
        status.setBusy("Caricamento dati in corso...");

        ColumnModel cm = new ColumnModel(configs);

        ContentPanel cp = new ContentPanel();
        cp.setHeading("Anagrafica utenti");
        cp.setFrame(true);
        //cp.setIcon(Resources.ICONS.table());
        cp.setSize(Consts.LarghezzaFinestra, Consts.AltezzaFinestra);
        cp.setLayout(new RowLayout(Orientation.VERTICAL));

        final EditorGrid<BeanModel> grid = new EditorGrid<BeanModel>(storeUtenti, cm);
        grid.setSelectionModel(sm);
        grid.setAutoExpandColumn("username");
        grid.setBorders(true);
        grid.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);
        grid.getSelectionModel().addListener(Events.SelectionChange,
                new Listener<SelectionChangedEvent<BeanModel>>() {

                    public void handleEvent(SelectionChangedEvent<BeanModel> be) {
                        if (be.getSelection().size() == 1) {
                            BeanModel model = be.getSelection().get(0);
                            userSelezionato = model.getBean();
                            List<String> idAziende = new ArrayList<String>();
                            idAziende = userSelezionato.getidAziende();

                            storeAziendeAssegnate.removeAll();
                            storeAziendeNonAssegnate.removeAll();
                            listBean = new ArrayList<BeanModel>();
                            listBean = (ArrayList<BeanModel>) storeAziende.getModels();

                            if (model.get("userrole").equals("APP_ADMIN")) {
                                storeAziendeAssegnate.add(listBean);
                                lists.disable();
                                comboAzienda.disable();
                                comboLavoratore.disable();
                            } else if (model.get("userrole").equals("LAVORATORE")) {
                                storeAziendeNonAssegnate.add(listBean);
                                lists.disable();
                                comboAzienda.enable();
                                comboLavoratore.enable();
                            } else {
                                comboAzienda.disable();
                                comboLavoratore.disable();
                                lists.enable();
                                storeAziendeNonAssegnate.add(listBean);
                                if (idAziende != null) {

                                    Iterator it = idAziende.iterator();
                                    while (it.hasNext()) {
                                        String s = (String) it.next();
                                        Long id = Long.parseLong(s);
                                        Azienda azienda = getAziendaFromId(id);
                                        BeanModelFactory factory = BeanModelLookup.get().getFactory(Azienda.class);
                                        BeanModel modello = factory.createModel(azienda);
                                        storeAziendeAssegnate.add(modello);
                                        BeanModel m = null;
                                        m = storeAziendeAssegnate.findModel(modello);
                                        if (m != null) {
                                            storeAziendeNonAssegnate.remove(modello);
                                        }
                                    }
                                }
                            }

                        } else {
                            storeAziendeAssegnate.removeAll();
                            storeAziendeNonAssegnate.removeAll();
                        }
                    }
                });

        cp.add(grid, new RowData(1, .5));

        FormPanel pannelloAziende = createForm();
        cp.add(pannelloAziende, new RowData(1, .5));

        ToolBar toolBar = new ToolBar();
        Button addButton = new Button("Aggiungi utente");
        addButton.setIcon(IconHelper.create("/resources/grafica/x16/add2.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (aziende == null || aziende.isEmpty()) {
                    //TODO messaggio di errore: aggiungere azienda
                } else {
                    BeanModelFactory factory = BeanModelLookup.get().getFactory(User.class);
                    String ruolo = userRoles.get(0);
                    User user = new User("NuovoUserName", "NuovaPassword", "NuovaEmail", ruolo);
                    ArrayList<Long> idaziende = null;
                    user.setIdAziende(idaziende);
                    BeanModel model = factory.createModel(user);

                    grid.stopEditing();
                    users.add(user);
                    storeUtenti.insert(model, 0);
                    grid.startEditing(storeUtenti.indexOf(model), 0);
                }
            }
        });
        toolBar.add(addButton);
        toolBar.add(new SeparatorToolItem());

        Button remove = new Button("Rimuovi selezionati");
        remove.setIcon(IconHelper.create("/resources/grafica/x16/delete2.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        remove.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!(sm.getSelectedItems()).isEmpty()) {
                    MessageBox.confirm("Confirm", "Sei sicuro di voler eliminare gli elemtni selezionati?", cancellazione);
                } else {
                    MessageBox.alert("Errore", "Selezionare uno o più elementi", cancellazione);
                }
            }
            //conferma per l'eliminazione
            final Listener<MessageBoxEvent> cancellazione = new Listener<MessageBoxEvent>() {

                public void handleEvent(MessageBoxEvent ce) {
                    Button btn = ce.getButtonClicked();
                    if (btn.getText().equals("Yes")) {
                        List<BeanModel> modelsDaRimuovere = sm.getSelectedItems();
                        ArrayList<User> usersDaRimuovere = new ArrayList();
                        if (modelsDaRimuovere != null) {
                            Iterator it = modelsDaRimuovere.iterator();
                            while (it.hasNext()) {
                                Object model = it.next();
                                storeUtenti.remove((BeanModel) model);
                                User user = ((BeanModel) model).getBean();
                                usersDaRimuovere.add(user);
                                users.remove(user);
                            }
                            cancellaDati(usersDaRimuovere);
                        }
                    }
                }
            };
        });
        toolBar.add(remove);


        ToolBar statusBar = new ToolBar();
        status.setWidth(350);
        statusBar.add(status);
        cp.setBottomComponent(statusBar);

        cp.setTopComponent(toolBar);
        cp.setButtonAlign(HorizontalAlignment.CENTER);

        Button resetButton = new Button("Reset", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                storeUtenti.rejectChanges();
            }
        });
        resetButton.setIcon(IconHelper.create("/resources/grafica/x16/undo.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        cp.addButton(resetButton);

        Button saveButton = new Button("Save", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                storeUtenti.commitChanges();
                ArrayList<Long> idAziende = null;
                if (userSelezionato.getUserrole().equals("HR_ADMIN")) {
                    ArrayList<BeanModel> list = (ArrayList<BeanModel>) storeAziendeAssegnate.getModels();
                    if (list != null) {
                        idAziende = new ArrayList<Long>();
                        Iterator it = list.iterator();
                        while (it.hasNext()) {
                            BeanModel model = (BeanModel) it.next();
                            Azienda az = model.getBean();
                            Long idAzienda = az.getId();
                            idAziende.add(idAzienda);
                        }
                    }
                    userSelezionato.setIdAziende(idAziende);
                }
                if (userSelezionato.getUserrole().equals("LAVORATORE")) {
                    BeanModel lavoratoreModel = comboLavoratore.getValue();
                    Lavoratore lavoratore = lavoratoreModel.getBean();
                    userSelezionato.setIdLavoratore(lavoratore.getId());
                }
                salvaDati(users);
            }
        });
        saveButton.setIcon(IconHelper.create("/resources/grafica/x16/save.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        cp.addButton(saveButton);

        add(cp);
    }

    private FormPanel createForm() {
        FormPanel panel = new FormPanel();
        panel.setHeaderVisible(false);

        //FieldSet fieldSetHradmin = new FieldSet();
        //fieldSetHradmin.setHeading("HR_ADMIN");
        //FormLayout layout = new FormLayout();
        //layout.setLabelWidth(75);
        //fieldSetHradmin.setLayout(layout);

        lists = new DualListField<BeanModel>();
        lists.setFieldLabel("Aziende di competenza");

        ListField<BeanModel> from = lists.getFromList();
        from.setDisplayField("denominazione");
        storeAziendeAssegnate.setStoreSorter(new StoreSorter<BeanModel>());
        from.setStore(storeAziendeAssegnate);

        ListField<BeanModel> to = lists.getToList();
        to.setDisplayField("denominazione");
        storeAziendeNonAssegnate.setStoreSorter(new StoreSorter<BeanModel>());
        to.setStore(storeAziendeNonAssegnate);

        //fieldSetHradmin.add(lists);

        panel.add(lists, formData);

        FieldSet fieldSetLavoratore = new FieldSet();
        fieldSetLavoratore.setHeading("Assegna un lavoratore ad un account LAVORATORE");
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(75);
        fieldSetLavoratore.setLayout(layout);

        comboAzienda = new ComboBox<BeanModel>();
        comboAzienda.setFieldLabel("Azienda");
        comboAzienda.setEmptyText("Seleziona un'azienda...");
        comboAzienda.setDisplayField("denominazione");
        comboAzienda.setWidth(150);
        comboAzienda.setEditable(false);
        comboAzienda.setStore(storeAziende);
        comboAzienda.setTypeAhead(true);
        comboAzienda.setTriggerAction(TriggerAction.ALL);
        comboAzienda.addListener(Events.SelectionChange,
                new Listener<SelectionChangedEvent<BeanModel>>() {

                    @Override
                    public void handleEvent(SelectionChangedEvent<BeanModel> be) {
                        Azienda azienda = comboAzienda.getValue().getBean();
                        caricaComboLavoratori(azienda);
                    }
                });
        fieldSetLavoratore.add(comboAzienda, formData);

        comboLavoratore = new ComboBox<BeanModel>();
        comboLavoratore.setFieldLabel("Lavoratore");
        comboLavoratore.setEmptyText("Seleziona un lavoratore...");
        comboLavoratore.setDisplayField("id");
        comboLavoratore.setWidth(150);
        comboLavoratore.setEditable(false);
        comboLavoratore.setStore(storeLavoratori);
        comboLavoratore.setTypeAhead(true);
        comboLavoratore.setTriggerAction(TriggerAction.ALL);
        fieldSetLavoratore.add(comboLavoratore, formData);

        panel.add(fieldSetLavoratore);

        panel.setWidth(550);

        return panel;
    }

    private Azienda getAziendaFromId(Long idAzienda) {
        if (aziende != null) {
            Iterator it = aziende.iterator();
            while (it.hasNext()) {
                Azienda azienda = (Azienda) it.next();
                if (azienda.getId().equals(idAzienda)) {
                    return azienda;
                }
            }
        }
        return null;
    }

    private void caricaDati() {
        // Initialize the service proxy.
        if (dstoreSvcUser == null) {
            dstoreSvcUser = GWT.create(UserService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            public void onSuccess(ArrayList result) {
                users = result;
                //caricaAziende();
                BeanModelFactory factory = BeanModelLookup.get().getFactory(User.class);
                if (result != null) {
                    Iterator it = result.iterator();
                    while (it.hasNext()) {
                        Object user = it.next();
                        BeanModel userModel = factory.createModel(user);
                        storeUtenti.add(userModel);
                    }
                }
                status.setStatus("Dati caricati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        users.clear();
        storeUtenti.removeAll();
        dstoreSvcUser.carica(callback);
    }

    private void salvaDati(ArrayList<User> users) {
        // Initialize the service proxy.
        if (dstoreSvcUser == null) {
            dstoreSvcUser = GWT.create(UserService.class);
        }
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            public void onSuccess(Void result) {
                //I dati vengono ricaricati per ottenere gli ID assegnati dal DataStore alle entità appena aggiunte
                caricaAziende();
            }
        };
        // Make the call to the stock price service.
        dstoreSvcUser.salva(users, callback);
    }

    private void cancellaDati(ArrayList users) {
        // Initialize the service proxy.
        if (dstoreSvcUser == null) {
            dstoreSvcUser = GWT.create(UserService.class);
        }
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            public void onSuccess(Void result) {
                status.setStatus("Dati cancellati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        dstoreSvcUser.cancella(users, callback);
    }

    public void caricaAziende() {
        // Initialize the service proxy.
        if (dstoreSvcAzienda == null) {
            dstoreSvcAzienda = GWT.create(AziendaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            public void onSuccess(ArrayList result) {
                aziende = result;
                BeanModelFactory factory = BeanModelLookup.get().getFactory(Azienda.class);
                if (result != null) {
                    Iterator it = result.iterator();
                    while (it.hasNext()) {
                        Object azienda = it.next();
                        BeanModel aziendaModel = factory.createModel(azienda);
                        storeAziendeNonAssegnate.add(aziendaModel);
                        storeAziende.add(aziendaModel);
                    }
                }
                caricaDati();
            }
        };
        // Make the call to the stock price service.
        aziende.clear();
        storeAziendeNonAssegnate.removeAll();
        storeAziendeAssegnate.removeAll();
        storeAziende.removeAll();

        dstoreSvcAzienda.carica(callback);
    }

    private void caricaComboLavoratori(Azienda azienda) {
        // Initialize the service proxy.
        if (dstoreSvcLAV == null) {
            dstoreSvcLAV = GWT.create(LavoratoreService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
            public void onSuccess(ArrayList result) {
                BeanModelFactory factory = BeanModelLookup.get().getFactory(Lavoratore.class);
                if (result != null) {
                    Iterator it = result.iterator();
                    while (it.hasNext()) {
                        Object lavoratore = it.next();
                        BeanModel lavoratoreModel = factory.createModel(lavoratore);
                        storeLavoratori.add(lavoratoreModel);
                    }
                }
                status.setStatus("Dati caricati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        storeLavoratori.removeAll();
        dstoreSvcLAV.carica(azienda, callback);
    }
} // end class

