/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter.util;

import java.io.File;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author claudivan
 */
public class XMLDocumentBuild {

    static final String XML_SEQUENCIAS = "src/main/java/br/com/adsifcz/postgresmongoimporter/util/sequencias.xml";
    static final String XML_DOCUMENTOS = "src/main/java/br/com/adsifcz/postgresmongoimporter/util/documentos.xml";
    static final SAXBuilder saxBuilder = new SAXBuilder();

    public static Document getXMLSequencias() throws JDOMException, IOException {
        File xmlFileSequencias = new File(XML_SEQUENCIAS);
        return saxBuilder.build(xmlFileSequencias);
    }

    public static Document getXMLDocumentos() throws JDOMException, IOException {
        File xmlFileSequencias = new File(XML_DOCUMENTOS);
        return saxBuilder.build(xmlFileSequencias);
    }
}
