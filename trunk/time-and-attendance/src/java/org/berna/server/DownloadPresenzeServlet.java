/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.berna.client.Azienda;
import org.berna.client.Lavoratore;
import org.berna.client.PersonaFisica;
import org.berna.client.Presenza;
import org.berna.client.TipologiaLavoro;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Utente
 */
public class DownloadPresenzeServlet extends HttpServlet {

    Writer w = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param re
     * quest servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String xml = "";
        
        String s = request.getParameter("idAzienda");
        Long idAzienda=Long.parseLong(s);

        s = request.getParameter("mese");
        int mese = Integer.parseInt(s);

        s = request.getParameter("anno");
        int anno = Integer.parseInt(s);

        Document doc = creaPresenze(idAzienda, mese, anno);
        xml = xml2String(doc);
        //response.setContentType("text/html;charset=UTF-8");
        response.setContentType("text/xml");

        //response.setHeader("Expires", "0");
        //response.setHeader("Cache-Control", "no-cache");
        //response.setHeader("Pragma", "public");
        String fileName = "dump.xml";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // response.setCharacterEncoding("utf-8");
        //response.setHeader("Transfer-Encoding", "Chunked");
        //response.setBufferSize(baos.size());
        //response.flushBuffer();

        PrintWriter out = response.getWriter();
        try {
            out.println(xml);
        }     catch(Exception ex){
            ex.printStackTrace();
        } finally {
            out.close();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private Document creaPresenze(Long idAzienda, int mese, int anno) {
        //System.out.println(String.valueOf(idAzienda));
        //System.out.println(String.valueOf(mese));
        //System.out.println(String.valueOf(anno));

        List<Azienda> list = AziendaUtil.getList();
        ArrayList aziende = Utils.listToArray(list);
        String piva = Azienda.idToPiva(idAzienda, aziende);
        //System.out.println("Partita iva: "+String.valueOf(piva));

        Document doc = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DownloadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        Node prs = doc.createElement("Presenze");
        doc.appendChild(prs);

        List<Presenza> presenze = null;
        mese = mese - 1;
        anno = anno - 1900;
        Date date1 = new Date(anno, mese, 1);
        int giorno = DateUtils.getMaxDaysInMointh(anno, mese);
        Date date2 = new Date(anno, mese, giorno);
        presenze = PresenzaUtil.getList(date1, date2, idAzienda);

        for (Presenza presenza : presenze) {

            //Ottenere codice fiscale a partire dall'idLavoratore
            Long idLavoratore=presenza.getIdLavoratore();
            List<Lavoratore> list2 = LavoratoreUtil.getList();
            ArrayList<Lavoratore> lavoratori = Utils.listToArray(list2);
            Long idPersonaFisica = Lavoratore.idToidPersonaFisica(idLavoratore, lavoratori);
            //System.out.println("id persona fisica: "+String.valueOf(idPersonaFisica));
            List<PersonaFisica> list3 = PersonaFisicaUtil.getList();
            ArrayList<PersonaFisica> personeFisiche = Utils.listToArray(list3);
            String cf = PersonaFisica.idToCf(idPersonaFisica, personeFisiche);
            //System.out.println("cf: "+String.valueOf(cf));

            int tipologia=0;
            ArrayList tipologieLavoro = TipologiaLavoro.generaTipologie();
            Iterator it = tipologieLavoro.iterator();
            while (it.hasNext()) {
                TipologiaLavoro tip = (TipologiaLavoro) it.next();
                if(tip.getNome().equals(presenza.getTipo())){
                    tipologia=tip.getCodice();
                }
            }

            Node pr = doc.createElement("Presenza");
            prs.appendChild(pr);

            addNode(doc, pr, "Id_Presenza", String.valueOf(presenza.getId()));
            addNode(doc, pr, "Piva_Azienda", piva);
            addNode(doc, pr, "CF_Lavoratore", cf);
            addNode(doc, pr, "Data", String.valueOf(presenza.getDataPresenza()));
            addNode(doc, pr, "Quantita", String.valueOf(presenza.getQuantita()));
            addNode(doc, pr, "Tipo", String.valueOf(tipologia));

        } // end for

        return doc;

    }

    private String xml2String(Document doc) {
        String xmlString = null;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //initialize StreamResult with File object to save to file
            //initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            xmlString = result.getWriter().toString();
            System.out.println(xmlString);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(DownloadServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(DownloadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xmlString;
    }

    private void addNode(Document doc, Node node, String tag, String value) {
        Node nome = doc.createElement(tag);
        nome.setTextContent(value);
        node.appendChild(nome);
    }
}
