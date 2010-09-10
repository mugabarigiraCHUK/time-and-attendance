/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.berna.client.PersonaFisica;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Berna
 */
public class DownloadServlet extends HttpServlet {

    private static final String BOOKSTORE_XML = "./bookstore-jaxb.xml";
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

        Document doc = dummy();
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

    private Document dummy() {
        Document doc = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DownloadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        Node pfs = doc.createElement("PersoneFisiche");
        doc.appendChild(pfs);
        List<PersonaFisica> persone = PersonaFisicaUtil.getList();
        for (PersonaFisica persona : persone) {

            Node pf = doc.createElement("PersonaFisica");
            pfs.appendChild(pf);

            addNode(doc, pf, "Id", String.valueOf(persona.getId()));
            addNode(doc, pf, "Nome", persona.getNome());
            addNode(doc, pf, "Cognome", persona.getCognome());

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
} // end class

