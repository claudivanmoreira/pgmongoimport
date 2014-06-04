/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter;

import br.com.adsifcz.postgresmongoimporter.core.beans.Documento;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Element;

/**
 *
 * @author claudivan
 */
public class SchemaParser {

    public Documento getSchemaDoDocumento(Element nodeDocumento) {

        Documento documentResult = createDocumento(nodeDocumento);
        String query = nodeDocumento.getChild("entity").getAttributeValue("query");

        //Pega todos os elementos da tag <embedded>
        List<Element> nodesEmbeddeds = nodeDocumento.getChildren("embedded");

        //Aqui lê os documentos embedded
        List<Documento> documentosEmbedded = new ArrayList<>();
        for (Element nodeEmbedded : nodesEmbeddeds) {
            Documento embeddedDocumento = createDocumento(nodeEmbedded);
            documentosEmbedded.add(embeddedDocumento);
        }

        documentResult.setQuery(query);
        documentResult.setEmbeddedDocumentos(documentosEmbedded);

        return documentResult;
    }

    private Documento createDocumento(Element node) {

        String nome = node.getAttributeValue("nome");
        String tipo = node.getAttributeValue("type");
        String query = node.getAttributeValue("query");
        System.out.println("Tipo " + tipo);

        Documento documento = new Documento();
        documento.setNome(nome);
        documento.setTipo(tipo);
        documento.setQuery(query);

        //Verifica se a query tem parametros
        List<Element> params = node.getChildren("queryParam");
        if (params != null) {
            
            List <String> queryParameteters = new ArrayList<>();
            
            for (Element param: params) {
            
                queryParameteters.add(param.getAttributeValue("nome"));
            }
            
            documento.setQueryParameters(queryParameteters);
        }

        Map<String, String> embeddedColunas = new HashMap<>();

        List<Element> colunas = node.getChildren("coluna");

        for (Element coluna : colunas) {
            if (coluna.getAttributeValue("pk") != null) {
                documento.setNomePk(coluna.getAttributeValue("nome"));
            }
            embeddedColunas.put(coluna.getAttributeValue("nome"), coluna.getAttributeValue("tipo"));
        }

        documento.setMapColunas(embeddedColunas);

        return documento;
    }
}
