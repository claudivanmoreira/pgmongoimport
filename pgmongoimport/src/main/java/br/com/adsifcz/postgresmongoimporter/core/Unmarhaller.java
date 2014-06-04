/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter.core;

import br.com.adsifcz.postgresmongoimporter.core.beans.Documento;
import br.com.adsifcz.postgresmongoimporter.core.beans.Sequencia;
import br.com.adsifcz.postgresmongoimporter.util.XMLDocumentBuild;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author claudivan
 */
public class Unmarhaller {

    private static Unmarhaller instance;

    private Unmarhaller() {
    }

    public static Unmarhaller createUnmarhaller() {
        if (instance == null) {
            instance = new Unmarhaller();
        }
        return instance;
    }

    public List unmarshal(Class classToUnmarshal) throws IOException, JDOMException {

        if (classToUnmarshal.getSimpleName().equals("Sequencia")) {
            return unmarshalSequencias();
        } else {
            return unmarshalTabelas();
        }
    }

    private List<Sequencia> unmarshalSequencias() throws JDOMException, IOException {

        Document xmlDocumentSequencias = XMLDocumentBuild.getXMLSequencias();

        List<Sequencia> listSequencias = new ArrayList<>();

        Element sequencias = xmlDocumentSequencias.getRootElement();

        List<Element> listElementosSequencias = sequencias.getChildren();

        for (Element sequencia : listElementosSequencias) {

            Element node = sequencia.getChild("campo");

            Sequencia seq = new Sequencia();

            seq.setNome(sequencia.getAttributeValue("id"));
            seq.setQuery(sequencia.getAttributeValue("query"));
            seq.getMapColunas().put(node.getAttributeValue("coluna"), node.getAttributeValue("tipo"));

            listSequencias.add(seq);
        }

        return listSequencias;
    }

    private List<Documento> unmarshalTabelas() throws JDOMException, IOException {
        //Lista a ser preenchida com os objetos do arquivo XML
        List<Documento> documentos = new ArrayList<>();

        //Carrega o arquivo XML
        Document xmlDocumentos = XMLDocumentBuild.getXMLDocumentos();

        //Captura o elemento root do documento
        Element mongoConfig = xmlDocumentos.getRootElement();

        //Recupera a lista de filhos do elemento root
        List<Element> xmlElementosDocumento = mongoConfig.getChildren();

        //Para cada elemento <documento>
        for (Element xmlDocumento : xmlElementosDocumento) {

            Documento documento = readXMLElementToDocumento(xmlDocumento);

            String query = xmlDocumento.getChild("entity").getAttributeValue("query");
            
            //Pega todos os elementos da tag <embedded>
            List<Element> embeddedXmlDocumentos = xmlDocumento.getChildren("embedded");

            //Para cada elemento <embedded>
            List<Documento> documentosEmbedded = new ArrayList<>();
            for (Element embeddedXmlDocumento : embeddedXmlDocumentos) {
                Documento embeddedDocumento = readXMLElementToDocumento(embeddedXmlDocumento);
                documentosEmbedded.add(embeddedDocumento);
            }

            documento.setQuery(query);
            
            //Seta os documentos embeddeds encontrados no documento principal
            documento.setEmbeddedDocumentos(documentosEmbedded);

            //Adiciona o documento na lista de documentos lido
            documentos.add(documento);
        }
        
        return documentos;
        
    }

    private Documento readXMLElementToDocumento(Element xmlElement) {

        String nome = xmlElement.getAttributeValue("nome");
        String query = xmlElement.getAttributeValue("query");
        String tipo = xmlElement.getAttributeValue("type");

        Documento documento = new Documento();
        documento.setNome(nome);
        documento.setQuery(query);
        documento.setTipo(tipo);


        List<Element> elementsQueryParam = xmlElement.getChildren("queryParam");
        //Verifica se a query tem parametros
        if (elementsQueryParam != null) {
            List<String> parametrosDaQuery = new ArrayList<>();
            for (Element elementQueryParam : elementsQueryParam) {
                parametrosDaQuery.add(elementQueryParam.getAttributeValue("nome"));
            }
            //Seta os parametros da query no documento
            documento.setQueryParameters(parametrosDaQuery);
        }

        // Armazena as informações sobre as colunas da tabela que vão para o documento
        Map<String, String> embeddedColunas = new HashMap<>();

        List<Element> colunas = xmlElement.getChildren("coluna");

        for (Element coluna : colunas) {
            //Recupera o nome da coluna será a chave primária do documento
            if (coluna.getAttributeValue("pk") != null) {
                documento.setNomePk(coluna.getAttributeValue("nome"));
            }
            embeddedColunas.put(coluna.getAttributeValue("nome"), coluna.getAttributeValue("tipo"));
        }

        // Seta no documento as colunas da tabela que irão virar campos no documento
        documento.setMapColunas(embeddedColunas);

        return documento;
    }
}
