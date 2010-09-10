/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import java.util.ArrayList;
import java.util.List;
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
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Iterator;

public class AnagraficaLavoratore extends LayoutContainer {

    private PersonaFisicaServiceAsync dstoreSvcPF = GWT.create(PersonaFisicaService.class);
    private LavoratoreServiceAsync dstoreSvcLAV = GWT.create(LavoratoreService.class);
    private AziendaServiceAsync dstoreSvcAZ = GWT.create(AziendaService.class);
    ListStore<BeanModel> storeLavoratori = new ListStore<BeanModel>();
    ListStore<BeanModel> storeAziende = new ListStore<BeanModel>();
    ArrayList<PersonaFisica> personeFisiche = new ArrayList<PersonaFisica>();
    ArrayList<Azienda> aziende = new ArrayList<Azienda>();
    ArrayList<Lavoratore> lavoratori = new ArrayList<Lavoratore>();
    CheckBoxSelectionModel<BeanModel> sm = new CheckBoxSelectionModel<BeanModel>();
    private Status status = new Status();
    EditorGrid<BeanModel> grid = null;
    ColumnModel cm = null;
    ComboBox<BeanModel> comboAziende = null;
    List<String> aziendeDiCompetenza = null;

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FlowLayout(10));

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        configs.add(sm.getColumn());

        ColumnConfig column = new ColumnConfig();
        column.setId("id");
        column.setHeader("ID");
        column.setWidth(150);
        configs.add(column);

        column = new ColumnConfig();
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

        TextField<String> text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        configs.add(column);

        caricaAziende();
        status.setBusy("Caricamento dati in corso...");

        cm = new ColumnModel(configs);

        ContentPanel cp = new ContentPanel();
        //cp.setBodyBorder(false);
        //cp.setIcon(Resources.ICONS.table());
        cp.setHeading("Anagrafica lavoratore");
        cp.setSize(Consts.LarghezzaFinestra, Consts.AltezzaFinestra);
        cp.setLayout(new RowLayout(Orientation.VERTICAL));

        FormPanel panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setBorders(true);
        panel.setWidth(Consts.LarghezzaFinestra);
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

                    public void handleEvent(SelectionChangedEvent<BeanModel> be) {
                        filtraLavoratori();
                    }
                });
        panel.add(comboAziende);
        cp.add(panel, new RowData(1, .1));

        grid = new EditorGrid<BeanModel>(storeLavoratori, cm);
        grid.setSelectionModel(sm);
        //grid.setStyleAttribute("borderTop", "none");
        grid.setAutoExpandColumn("qualifica");
        grid.setBorders(true);
        grid.setStripeRows(true);

        grid.setModelProcessor(new ModelProcessor<BeanModel>() {

            @Override
            public BeanModel prepareData(BeanModel model) {
                if (model != null) {
                    Lavoratore lavoratore = model.getBean();
                    ArrayList<String> array = new ArrayList<String>();
                    array = PersonaFisica.idToNome(lavoratore.getIdPersonaFisica(), personeFisiche);
                    String nome = array.get(0);
                    String cognome = array.get(1);
                    String azienda = Azienda.idToNome(lavoratore.getIdAzienda(), aziende);
                    model.set("nome", nome);
                    model.set("cognome", cognome);
                    model.set("azienda", azienda);
                }
                return model;
            }
        });

        cp.add(grid, new RowData(1, .9));

        ToolBar toolBar = new ToolBar();
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
                        ArrayList<Lavoratore> lavoratoriDaRimuovere = new ArrayList();
                        if (modelsDaRimuovere != null) {
                            Iterator it = modelsDaRimuovere.iterator();
                            while (it.hasNext()) {
                                Object model = it.next();
                                storeLavoratori.remove((BeanModel) model);
                                Lavoratore lavoratore = ((BeanModel) model).getBean();
                                lavoratoriDaRimuovere.add(lavoratore);
                                lavoratori.remove(lavoratore);
                            }
                            cancellaDati(lavoratoriDaRimuovere);
                        }
                    }
                }
            };
        });
        toolBar.add(remove);
        cp.setTopComponent(toolBar);

        ToolBar statusBar = new ToolBar();
        status.setWidth(350);
        statusBar.add(status);
        cp.setBottomComponent(statusBar);

        cp.setButtonAlign(HorizontalAlignment.CENTER);
        Button resetButton = new Button("Reset", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                storeLavoratori.rejectChanges();
            }
        });
        resetButton.setIcon(IconHelper.create("/resources/grafica/x16/undo.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        cp.addButton(resetButton);

        Button saveButton = new Button("Save", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                storeLavoratori.commitChanges();
                salvaDati(lavoratori);
            }
        });
        saveButton.setIcon(IconHelper.create("/resources/grafica/x16/save.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        cp.addButton(saveButton);

        add(cp);
    }

    //Prima carica le aziende, poi le persone fisiche e in fine i lavoratori
    public void caricaAziende() {
        // Initialize the service proxy.
        if (dstoreSvcAZ == null) {
            dstoreSvcAZ = GWT.create(AziendaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

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

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

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

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

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

    private void cancellaDati(ArrayList<Lavoratore> lavoratoriDaRimuovere) {
        // Initialize the service proxy.
        if (dstoreSvcLAV == null) {
            dstoreSvcLAV = GWT.create(LavoratoreService.class);
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
        dstoreSvcLAV.cancella(lavoratoriDaRimuovere, callback);
    }

    private void salvaDati(ArrayList<Lavoratore> lavoratori) {
        // Initialize the service proxy.
        if (dstoreSvcLAV == null) {
            dstoreSvcLAV = GWT.create(LavoratoreService.class);
        }
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            public void onSuccess(Void result) {
                status.setStatus("Dati aggiornati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        dstoreSvcLAV.aggiorna(lavoratori, callback);
    }

    //Serve per riempire la tabella con solo i lavoratori appartenenti all'azienda selezionata
    private void filtraLavoratori() {
        storeLavoratori.removeAll();
        if (comboAziende.getValue() != null) {
            BeanModelFactory factory = BeanModelLookup.get().getFactory(Lavoratore.class);
            if (lavoratori != null) {
                Iterator it = lavoratori.iterator();
                while (it.hasNext()) {
                    Object lavoratore = it.next();
                    BeanModel lavoratoreModel = factory.createModel(lavoratore);
                    BeanModel aziendaModel = comboAziende.getValue();
                    if (aziendaModel.get("id").equals(lavoratoreModel.get("idAzienda"))) {
                        storeLavoratori.add(lavoratoreModel);
                    }
                }
            }
        }
    }
}
