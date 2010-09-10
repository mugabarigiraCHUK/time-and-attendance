/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.server;


import javax.servlet.ServletOutputStream;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.InputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class UploadServlet extends HttpServlet {

    private static final Logger log =
            Logger.getLogger(UploadServlet.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {


        try {
            String xml = "";
            // Initializing the servlet response
            res.setContentType("text/xml");
            //  res.setHeader("Expires", "0");
            //  res.setHeader("Cache-Control", "no-cache");
            // res.setHeader("Pragma", "public");
            String fileName = "dump.xml";
            res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            res.setCharacterEncoding("utf-8");
            //    res.setHeader("Transfer-Encoding", "Chunked");
            //res.setBufferSize(baos.size());
            //res.flushBuffer();

            ServletFileUpload upload = new ServletFileUpload();

            ServletOutputStream out = res.getOutputStream();
            FileItemIterator iterator = upload.getItemIterator(req);
            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();
                InputStream stream = item.openStream();

                if (item.isFormField()) {
                    log.warning("Got a form field: " + item.getFieldName());
                } else {
                    log.warning("Got an uploaded file: " + item.getFieldName()
                            + ", name = " + item.getName());

                    // You now have the filename (item.getName() and the
                    // contents (which you can read from stream).  Here we just
                    // print them back out to the servlet output stream, but you
                    // will probably want to do something more interesting (for
                    // example, wrap them in a Blob and commit them to the
                    // datastore).


                    int len;
                    byte[] buffer = new byte[8192];
                    while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
                        //log.info("writing data...");
                        //out.write(buffer, 0, len);
                        //log.info("buffer: " + new String(buffer));
                        xml = xml + new String(buffer);
                    }




                }
            }

            log.info("end of while");

            dummy(xml);

            //log.info("writing data...");
            //out.write(xml.getBytes());
            out.flush();
            out.close();
            log.info("stream closed");
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void dummy(String xmlstring) {
        try {
            xmlstring = xmlstring.trim();
            log.info("xmlstring:\r\n" + xmlstring);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader reader = new StringReader(xmlstring);
            InputSource inputSource = new InputSource(reader);
            Document doc = db.parse(inputSource);

//            CharArrayReader characterStream = new CharArrayReader(xmlstring.toCharArray());
//            InputSource is = new InputSource(characterStream);
//            Document doc = db.parse(is);

            //doc.getDocumentElement().normalize();
            log.info("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("PersoneFisiche");
            log.info("cycling...");
            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element fstElmnt = (Element) fstNode;

                    NodeList idElmntLst = fstElmnt.getElementsByTagName("Id");
                    Element idElmnt = (Element) idElmntLst.item(0);
                    NodeList idList = idElmnt.getChildNodes();
                    log.info("Id : " + ((Node) idList.item(0)).getNodeValue());

                    NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("Nome");
                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                    NodeList fstNm = fstNmElmnt.getChildNodes();
                    log.info("First Name : " + ((Node) fstNm.item(0)).getNodeValue());

                    NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("Cognome");
                    Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
                    NodeList lstNm = lstNmElmnt.getChildNodes();
                    log.info("Last Name : " + ((Node) lstNm.item(0)).getNodeValue());
                }

            }
            reader.close();
        } catch (IOException e) {
            log.severe("Eccezione IO: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
