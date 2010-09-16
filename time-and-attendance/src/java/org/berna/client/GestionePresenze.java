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
import com.extjs.gxt.ui.client.data.ModelProcessor;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
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

public class GestionePresenze extends LayoutContainer {

    private PresenzaServiceAsync dstoreSvcPresenza = GWT.create(PresenzaService.class);
    private PersonaFisicaServiceAsync dstoreSvcPF = GWT.create(PersonaFisicaService.class);
    private LavoratoreServiceAsync dstoreSvcLAV = GWT.create(LavoratoreService.class);
    private AziendaServiceAsync dstoreSvcAZ = GWT.create(AziendaService.class);
    ListStore<BeanModel> storeLavoratori = new ListStore<BeanModel>();
    ListStore<BeanModel> storeAziende = new ListStore<BeanModel>();
    ListStore<BeanModel> storePresenze = new ListStore<BeanModel>();
    ArrayList<Presenza> presenze = new ArrayList<Presenza>();
    ArrayList<PersonaFisica> personeFisiche = new ArrayList<PersonaFisica>();
    ArrayList<Azienda> aziende = new ArrayList<Azienda>();
    ArrayList<Lavoratore> lavoratori = new ArrayList<Lavoratore>();
    CheckBoxSelectionModel<BeanModel> sm = new CheckBoxSelectionModel<BeanModel>();
    private Status status = new Status();
    Grid<BeanModel> gridLavoratori = null;
    Grid<BeanModel> gridPresenze = null;
    ColumnModel cmLavoratori = null;
    ColumnModel cmPresenze = null;
    ComboBox<BeanModel> comboAziende = null;
    List<String> aziendeDiCompetenza = null;
    FormPanel form = null;
    private FormBinding formBindings;
    private FormData formData = new FormData();
    private FormData formData2 = new FormData();
    NumberField quantita = null;
    MyDateField dataPresenza = null;
    TextField<String> idLavoratore = null;
    TextField<String> nome = null;
    TextField<String> cognome = null;
    SimpleComboBox<String> comboTipo = null;
    SimpleComboBox<String> comboMese = null;
    SimpleComboBox<String> comboAnno = null;
    SimpleComboBox<String> comboSede = null;
    Lavoratore lavoratoreSelezionato = null;

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new CenterLayout());

        //Colonne per la tabella lavoratori
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("nome");
        column.setHeader("Nome");
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("cognome");
        column.setHeader("Cognome");
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("cf");
        column.setHeader("Codice fiscale");
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("azienda");
        column.setHeader("Azienda");
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("dataAssunzione");
        column.setHeader("Data assunzione");
        column.setWidth(100);
        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        configs.add(column);

        column = new ColumnConfig();
        column.setId("qualifica");
        column.setHeader("Qualifica");
        column.setWidth(150);
        configs.add(column);

        caricaAziende();

        cmLavoratori = new ColumnModel(configs);

        //Colonne per tabella presenze
        List<ColumnConfig> configs2 = new ArrayList<ColumnConfig>();
        configs2.add(sm.getColumn());

        column = new ColumnConfig();
        column.setId("dataPresenza");
        column.setHeader("Data");
        column.setWidth(100);
        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        configs2.add(column);

        column = new ColumnConfig();
        column.setId("tipo");
        column.setHeader("Tipo");
        column.setWidth(150);
        configs2.add(column);

        column = new ColumnConfig();
        column.setId("quantita");
        column.setHeader("Quantita");
        column.setWidth(150);
        configs2.add(column);

        cmPresenze = new ColumnModel(configs2);


        ContentPanel cp = new ContentPanel();
        //cp.setBodyBorder(false);
        //cp.setIcon(Resources.ICONS.table());
        cp.setHeading("Gestione presenze");
        cp.setSize(Consts.LarghezzaFinestra, Consts.AltezzaFinestra);
        cp.setLayout(new RowLayout(Orientation.VERTICAL));


        FormPanel panel = new FormPanel();

        LayoutContainer main = new LayoutContainer();
        main.setLayout(new ColumnLayout());

        panel.setHeaderVisible(false);
        panel.setBorders(true);
        panel.setWidth(Consts.LarghezzaFinestra);
        //panel.add(new Label("Azienda: "));
        comboAziende = new ComboBox<BeanModel>();
        comboAziende.setEmptyText("Seleziona un'azienda...");
        comboAziende.setEditable(false);
        //comboAziende.setOriginalValue(null);
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
                        filtraLavoratori();
                    }
                });
        if (Login.loggedUser.getUserrole().equals("LAVORATORE")) {
            comboAziende.disable();
        }
        main.add(comboAziende, formData2);

        //panel.add(new Label("Mese: "));
        comboMese = new SimpleComboBox<String>();
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
        comboMese.addListener(Events.SelectionChange,
                new Listener<SelectionChangedEvent<BeanModel>>() {

                    @Override
                    public void handleEvent(SelectionChangedEvent<BeanModel> be) {
                        if (comboMese.getSimpleValue() != "" && comboAnno.getSimpleValue() != "" && lavoratoreSelezionato != null) {
                            caricaPresenze(Integer.parseInt(comboMese.getSimpleValue()), Integer.parseInt(comboAnno.getSimpleValue()), lavoratoreSelezionato);
                        }
                    }
                });
        //main.add(comboMese, new ColumnData(.3));
        main.add(comboMese, formData2);

        //panel.add(new Label("Anno: "));
        comboAnno = new SimpleComboBox<String>();
        comboAnno.setFieldLabel("Anno");
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
        comboAnno.addListener(Events.SelectionChange,
                new Listener<SelectionChangedEvent<BeanModel>>() {

                    @Override
                    public void handleEvent(SelectionChangedEvent<BeanModel> be) {
                        if (comboMese.getSimpleValue() != "" && comboAnno.getSimpleValue() != "" && lavoratoreSelezionato != null) {
                            caricaPresenze(Integer.parseInt(comboMese.getSimpleValue()), Integer.parseInt(comboAnno.getSimpleValue()), lavoratoreSelezionato);
                        }
                    }
                });
        main.add(comboAnno, formData2);

        panel.add(main);

        cp.add(panel, new RowData(1, .1));

        gridLavoratori = new Grid<BeanModel>(storeLavoratori, cmLavoratori);
        gridLavoratori.setAutoExpandColumn("qualifica");
        gridLavoratori.getView().setEmptyText("Nessun lavoratore assunto in questa azienda");
        gridLavoratori.setBorders(true);
        gridLavoratori.setStripeRows(true);

        gridLavoratori.setModelProcessor(new ModelProcessor<BeanModel>() {

            @Override
            public BeanModel prepareData(BeanModel model) {
                if (model != null) {
                    Lavoratore lavoratore = model.getBean();
                    ArrayList<String> array = new ArrayList<String>();
                    array = PersonaFisica.idToNome(lavoratore.getIdPersonaFisica(), personeFisiche);
                    String nome = array.get(0);
                    String cognome = array.get(1);
                    String cf = array.get(2);
                    String azienda = Azienda.idToDenominazione(lavoratore.getIdAzienda(), aziende);
                    model.set("nome", nome);
                    model.set("cognome", cognome);
                    model.set("cf", cf);
                    model.set("azienda", azienda);
                }
                return model;
            }
        });

        gridLavoratori.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gridLavoratori.getSelectionModel().addListener(Events.SelectionChange,
                new Listener<SelectionChangedEvent<BeanModel>>() {

                    @Override
                    public void handleEvent(SelectionChangedEvent<BeanModel> be) {
                        if (be.getSelection().size() > 0) {
                            formBindings.bind((ModelData) be.getSelection().get(0));

                            BeanModel model = be.getSelection().get(0);
                            lavoratoreSelezionato = model.getBean();
                            if (comboMese.getSimpleValue() != "" && comboAnno.getSimpleValue() != "") {
                                int mese = Integer.parseInt(comboMese.getSimpleValue());
                                int anno = Integer.parseInt(comboAnno.getSimpleValue());
                                caricaPresenze(mese, anno, lavoratoreSelezionato);
                            }

                        } else {
                            formBindings.unbind();
                        }
                    }
                });

        cp.add(gridLavoratori, new RowData(1, .3));


        gridPresenze = new Grid<BeanModel>(storePresenze, cmPresenze);
        gridPresenze.setSelectionModel(sm);
        gridPresenze.setAutoExpandColumn("quantita");
        gridPresenze.getView().setEmptyText("Nessuna presenza inserita per questo lavoratore in questo periodo");
        gridPresenze.setBorders(true);
        gridPresenze.setStripeRows(true);
        cp.add(gridPresenze, new RowData(1, .3));


        form = createForm();
        formBindings = new FormBinding(form, true);
        formBindings.setStore(gridLavoratori.getStore());
        if (!(Login.loggedUser.getUserrole().equals("LAVORATORE"))) {
            cp.add(form, new RowData(1, .3));
        }

        ToolBar statusBar = new ToolBar();
        status.setWidth(350);
        statusBar.add(status);
        cp.setBottomComponent(statusBar);

        /*cp.setButtonAlign(HorizontalAlignment.CENTER);
        cp.addButton(new Button("Reset", new SelectionListener<ButtonEvent>() {

        @Override
        public void componentSelected(ButtonEvent ce) {
        storeLavoratori.rejectChanges();
        }
        }));

        cp.addButton(new Button("Save", new SelectionListener<ButtonEvent>() {

        @Override
        public void componentSelected(ButtonEvent ce) {
        storeLavoratori.commitChanges();
        salvaDati(lavoratori);
        }
        }));*/

        add(cp);
    }

    private FormPanel createForm() {
        FormPanel panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setBorders(true);

        idLavoratore = new TextField<String>();
        idLavoratore.setName("id");
        idLavoratore.setFieldLabel("Id LAV");
        idLavoratore.setVisible(false);
        idLavoratore.setAllowBlank(false);
        panel.add(idLavoratore, formData);

        FieldSet fieldSet = new FieldSet();
        fieldSet.setHeading("Dati presenza da aggiungere");
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(75);
        fieldSet.setLayout(layout);

        comboTipo = new SimpleComboBox<String>();
        comboTipo.setFieldLabel("Tipologia");
        comboTipo.setForceSelection(true);
        comboTipo.setEditable(false);
        comboTipo.setAllowBlank(false);
        comboTipo.setWidth(150);
        comboTipo.setTriggerAction(TriggerAction.ALL);
        ArrayList tipologieLavoro = TipologiaLavoro.generaTipologie();
        Iterator it = tipologieLavoro.iterator();
            while (it.hasNext()) {
                TipologiaLavoro tip = (TipologiaLavoro) it.next();
                comboTipo.add(tip.getNome());
            }
        fieldSet.add(comboTipo, formData);

        dataPresenza = new MyDateField();
        dataPresenza.setName("dataPresenza");
        dataPresenza.setFieldLabel("Data");
        dataPresenza.setAllowBlank(false);
        /*dataPresenza.getDatePicker().setMinDate(null);
        int year = 2010;
        int month = 3;
        Date d1 = new Date(year, month, 1);
        int last_day = DateUtils.getMaxDaysInMointh(year, month);
        Date d2 = new Date(year, month, last_day);
        dataPresenza.getDatePicker().setMinDate(d1);
        dataPresenza.getDatePicker().setMaxDate(d2);*/
        fieldSet.add(dataPresenza, formData);


        quantita = new NumberField();
        quantita.setName("quantita");
        quantita.setFieldLabel("Quantita");
        quantita.setAllowBlank(false);
        fieldSet.add(quantita, formData);

        comboSede = new SimpleComboBox<String>();
        comboSede.setFieldLabel("Sede");
        //comboSede.setForceSelection(true);
        comboSede.setEditable(false);
        comboSede.setAllowBlank(false);
        comboSede.setWidth(150);
        comboSede.setTriggerAction(TriggerAction.ALL);
        comboSede.setEmptyText("Selezionare una sede");
        comboSede.setEnabled(false);
        fieldSet.add(comboSede, formData);

        panel.add(fieldSet);

        Button crea = new Button("Aggiungi presenza");
        crea.setIcon(IconHelper.create("/resources/grafica/x16/add2.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        crea.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                String str = idLavoratore.getValue();
                Long idLavoratore = Long.parseLong(str);
                BeanModel aziendaModel = comboAziende.getValue();
                Azienda azienda = aziendaModel.getBean();
                Long idAzienda = azienda.getId();
                str = String.valueOf(quantita.getValue());
                int q = Integer.parseInt(str);
                Presenza nuovaPresenza = new Presenza(idLavoratore, idAzienda, dataPresenza.getValue(), q, comboTipo.getSimpleValue());
                salvaPresenza(nuovaPresenza);
            }
        });

        panel.setButtonAlign(HorizontalAlignment.CENTER);
        panel.addButton(crea);
        FormButtonBinding binding = new FormButtonBinding(panel);
        binding.addButton(crea);

        Button remove = new Button("Rimuovi selezionati");
        remove.setIcon(IconHelper.create("/resources/grafica/x16/delete2.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        remove.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!(sm.getSelectedItems()).isEmpty()) {
                    MessageBox.confirm("Confirm", "Sei sicuro di voler eliminare le presenze selezionate?", cancellazione);
                } else {
                    MessageBox.alert("Errore", "Selezionare una o più presenze dall'elenco", cancellazione);
                }
            }
            //conferma per l'eliminazione
            final Listener<MessageBoxEvent> cancellazione = new Listener<MessageBoxEvent>() {

                @Override
                public void handleEvent(MessageBoxEvent ce) {
                    Button btn = ce.getButtonClicked();
                    if (btn.getText().equals("Yes")) {
                        List<BeanModel> modelsDaRimuovere = sm.getSelectedItems();
                        ArrayList<Presenza> presenzeDaRimuovere = new ArrayList();
                        if (modelsDaRimuovere != null) {
                            Iterator it = modelsDaRimuovere.iterator();
                            while (it.hasNext()) {
                                Object model = it.next();
                                storePresenze.remove((BeanModel) model);
                                Presenza presenza = ((BeanModel) model).getBean();
                                presenzeDaRimuovere.add(presenza);
                                presenze.remove(presenza);
                            }
                            cancellaPresenze(presenzeDaRimuovere);
                        }
                    }
                }
            };
        });
        panel.addButton(remove);

        return panel;
    }

    //Prima carica le aziende, poi le persone fisiche e in fine i lavoratori
    public void caricaAziende() {
        // Initialize the service proxy.
        if (dstoreSvcAZ == null) {
            dstoreSvcAZ = GWT.create(AziendaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
            public void onSuccess(ArrayList result) {
                aziende = result;
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
                caricaPersonaFisiche();
            }
        };
        // Make the call to the stock price service.
        status.setBusy("Caricamento dati in corso...");
        aziendeDiCompetenza = Login.loggedUser.getidAziende();
        storeAziende.removeAll();
        aziende.clear();
        dstoreSvcAZ.carica(callback);
    }

    private void caricaPersonaFisiche() {
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
                personeFisiche = result;
                caricaLavoratori();
            }
        };
        // Make the call to the stock price service.
        personeFisiche.clear();
        dstoreSvcPF.carica(callback);
    }

    private void caricaLavoratori() {
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
                lavoratori = result;
                filtraLavoratori();// <-- questo serve per fare in modo che tornando all'anagrafica lavoratori, la tabella si riaggiorni
                status.setStatus("Dati caricati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        lavoratori.clear();
        storeLavoratori.removeAll();
        dstoreSvcLAV.carica(callback);
    }

    private void caricaPresenze(int mese, int anno, Lavoratore lavoratore) {
        // Initialize the service proxy.
        if (dstoreSvcPresenza == null) {
            dstoreSvcPresenza = GWT.create(PresenzaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
            public void onSuccess(ArrayList result) {
                presenze = result;
                BeanModelFactory factory = BeanModelLookup.get().getFactory(Presenza.class);
                if (result != null) {
                    Iterator it = result.iterator();
                    while (it.hasNext()) {
                        Object presenza = it.next();
                        BeanModel presenzaModel = factory.createModel(presenza);
                        storePresenze.add(presenzaModel);
                    }
                }
                status.setStatus("Presenze caricate con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        presenze.clear();
        storePresenze.removeAll();
        dstoreSvcPresenza.carica(mese, anno, lavoratore, callback);
    }

    private void cancellaPresenze(ArrayList<Presenza> presenzeDaRimuovere) {
        // Initialize the service proxy.
        if (dstoreSvcLAV == null) {
            dstoreSvcLAV = GWT.create(PresenzaService.class);
        }
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
            public void onSuccess(Void result) {
                status.setStatus("Presenze cancellate con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        dstoreSvcPresenza.cancella(presenzeDaRimuovere, callback);
    }

    private void salvaPresenza(Presenza nuovaPresenza) {
        // Initialize the service proxy.
        if (dstoreSvcPresenza == null) {
            dstoreSvcPresenza = GWT.create(PresenzaService.class);
        }
        AsyncCallback<java.lang.Boolean> callback = new AsyncCallback<java.lang.Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi col server", baseStyle);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result == true) {
                    status.setStatus("Presenza salvata con successo", baseStyle);
                    //int mese = Integer.parseInt(comboMese.getSimpleValue());
                    //int anno = Integer.parseInt(comboAnno.getSimpleValue());
                    caricaPresenze(Integer.parseInt(comboMese.getSimpleValue()), Integer.parseInt(comboAnno.getSimpleValue()), lavoratoreSelezionato);
                } else {
                    status.setStatus("Presenza già inserita", baseStyle);
                }
            }
        };
        // Make the call to the stock price service.
        dstoreSvcPresenza.salva(nuovaPresenza, callback);
    }

    //Serve per riempire la tabella con solo i lavoratori appartenenti all'azienda selezionata
    private void filtraLavoratori() {
        storeLavoratori.removeAll();
        if (comboAziende.getValue() != null || Login.loggedUser.getUserrole().equals("LAVORATORE")) {
            BeanModelFactory factory = BeanModelLookup.get().getFactory(Lavoratore.class);
            if (lavoratori != null) {
                Iterator it = lavoratori.iterator();
                while (it.hasNext()) {
                    Object lavoratore = it.next();
                    BeanModel lavoratoreModel = factory.createModel(lavoratore);
                    if (Login.loggedUser.getUserrole().equals("LAVORATORE")) {
                        if (lavoratoreModel.get("id").equals(Login.loggedUser.getIdLavoratore())) {
                            storeLavoratori.add(lavoratoreModel);
                        }
                    } else {
                        BeanModel aziendaModel = comboAziende.getValue();
                        if (aziendaModel.get("id").equals(lavoratoreModel.get("idAzienda"))) {
                            storeLavoratori.add(lavoratoreModel);
                        }
                    }
                }
            }
        }
    }
}
