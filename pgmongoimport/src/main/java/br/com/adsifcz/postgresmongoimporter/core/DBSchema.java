/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter.core;

import br.com.adsifcz.postgresmongoimporter.core.beans.Documento;
import br.com.adsifcz.postgresmongoimporter.core.Unmarhaller;
import br.com.adsifcz.postgresmongoimporter.core.beans.Sequencia;
import br.com.adsifcz.postgresmongoimporter.util.JDBCConnectionFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 *
 * @author claudivan
 */
public class DBSchema {

    private Unmarhaller unmarhaller;
    private Connection connection;

    public DBSchema() {
        unmarhaller = Unmarhaller.createUnmarhaller();
    }

    public List<DBObject> getDBObjectsSequencia() {
        List<DBObject> dBObjectsSequencias = new ArrayList<>();
        try {
            //Monta os objetos Sequencia mapeados no XML
            List<Sequencia> sequencias = (List<Sequencia>) unmarhaller.unmarshal(Sequencia.class);
            for (Sequencia sequencia : sequencias) {
                //Faz a consulta que recupera o valor atual da sequencia
                ResultSet resultSet = getResultSetFromQuery(sequencia.getQuery(), false);
                while (resultSet.next()) {
                    //Cria um novo mongo JSON com os dados no resultSet
                    DBObject dBObject = createDBObjectFromResultSet(resultSet, sequencia.getMapColunas(), new BasicDBObject());
                    dBObject.put("_id", sequencia.getNome());
                    dBObjectsSequencias.add(dBObject);
                }
            }
        } catch (SQLException | IOException | JDOMException ex) {
            Logger.getLogger(Unmarhaller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dBObjectsSequencias;
    }

    public List<DBObject> getDBObjectsFromDocumento(Documento documento) {
        List<DBObject> dBObjectsSequencias = new ArrayList<>();
        try {
            //Faz a consulta que recupera os dados do Documento mapeado
            ResultSet resultSetDocumentoPrincipal = getResultSetFromQuery(documento.getQuery(), false);
            //Percorre o resultado
            while (resultSetDocumentoPrincipal.next()) {
                //Cria um novo mongo JSON com os dados no resultSet
                DBObject documentoPrincial = createDBObjectFromResultSet(resultSetDocumentoPrincipal, documento.getMapColunas(), new BasicDBObject());
                // Se tem documentos aninhados
                if (!documento.getEmbeddedDocumentos().isEmpty()) {
                    // Percorre todos os documentos
                    for (Documento embeddedDocumento : documento.getEmbeddedDocumentos()) {

                        //Utilizada para armazenar os objetos a serem aninhados quando a query obtiver mais de 1 resultado
                        List<DBObject> listEmbeddedDBObjectsDocumentos = null;
                        if (embeddedDocumento.getTipo().equals("array")) {
                            listEmbeddedDBObjectsDocumentos = new ArrayList<>();
                        }
                        DBObject dBObjectEmbeddedDocumento = new BasicDBObject();

                        // Verifica se é necessário uma Query extra para preencher o documento
                        if (embeddedDocumento.getQuery() != null) {

                            //Chama o método que constroi a query
                            String sqlQuery = prepareSqlQueryForDocument(resultSetDocumentoPrincipal, embeddedDocumento);

                            // Executa a Query extra para preencher o documento embedded
                            ResultSet resultSetDocumentoEmbedded = getResultSetFromQuery(sqlQuery, true);

                            while (resultSetDocumentoEmbedded.next()) {

                                // Lê o resultSet da consulta com os campos do documento a ser aninhado
                                dBObjectEmbeddedDocumento = createDBObjectFromResultSet(resultSetDocumentoEmbedded, embeddedDocumento.getMapColunas(), new BasicDBObject());

                                // Se a lista para armazenar vários objetos foi instanciada anteriormente
                                if (listEmbeddedDBObjectsDocumentos != null) {
                                    listEmbeddedDBObjectsDocumentos.add(dBObjectEmbeddedDocumento);
                                }
                            }

                        } else {
                            // Caso não hava query para executar, preenche o documento aninhado com o primeiro resultSet
                            dBObjectEmbeddedDocumento = createDBObjectFromResultSet(resultSetDocumentoPrincipal, embeddedDocumento.getMapColunas(), new BasicDBObject());
                        }

                        // Se a query do documento aninhado retornou mais de um documento a ser aninhado
                        if (listEmbeddedDBObjectsDocumentos != null) {
                            // Adiciona a lista de documentos embededds no documento principal
                            documentoPrincial.put(embeddedDocumento.getNome(), listEmbeddedDBObjectsDocumentos);

                            //Se o documento não tiver vazio
                        } else if (!dBObjectEmbeddedDocumento.toMap().isEmpty()) {
                            // Adiciona o documento embededd no documento principal
                            documentoPrincial.put(embeddedDocumento.getNome(), dBObjectEmbeddedDocumento);
                        }
                    }
                }

                dBObjectsSequencias.add(documentoPrincial);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Unmarhaller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dBObjectsSequencias;
    }

    private DBObject createDBObjectFromResultSet(ResultSet resultSet, Map<String, String> mapColunas, DBObject dBObject) throws SQLException {

        for (String nomeDaColuna : mapColunas.keySet()) {
            switch (mapColunas.get(nomeDaColuna)) {
                case "Long":
                    if (nomeDaColuna.equals("id")) {
                        dBObject.put("_id", resultSet.getLong(nomeDaColuna));
                    } else {
                        dBObject.put(nomeDaColuna, resultSet.getLong(nomeDaColuna));
                    }
                    break;
                case "String":
                    dBObject.put(nomeDaColuna, resultSet.getString(nomeDaColuna));
                    break;
                case "Double":
                    dBObject.put(nomeDaColuna, resultSet.getDouble(nomeDaColuna));
                    break;
                case "Integer":
                    dBObject.put(nomeDaColuna, resultSet.getInt(nomeDaColuna));
                    break;
                case "Date":
                    dBObject.put(nomeDaColuna, resultSet.getTimestamp(nomeDaColuna));
                    break;
            }
        }
        return dBObject;
    }

    private ResultSet getResultSetFromQuery(String sqlQuery, boolean isParametrizada) throws SQLException {

        if (connection == null || connection.isClosed()) {
            connection = JDBCConnectionFactory.createPostgresConnection();
        }

        ResultSet resultSet;
        if (isParametrizada) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery();
        } else {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sqlQuery);
        }
        return resultSet;

    }

    private String prepareSqlQueryForDocument(ResultSet resultSet, Documento documento) throws SQLException {
        String sqlQuery = documento.getQuery();
        // Verifica se a query tem parametros
        if (documento.getQueryParameters() != null) {
            //Seta os parametros na query
            List<String> parameterNames = documento.getQueryParameters();
            for (int i = 0; i < parameterNames.size(); i++) {
                //Varre o resultSet e vai setando o valor na posição do parametro
                sqlQuery = sqlQuery.replace("?" + i, resultSet.getString(parameterNames.get(i)));
            }
        }
        return sqlQuery;
    }

    private Integer getQtdeRowsInResultSet(ResultSet resultSet) throws SQLException {
        int qtdeRows = 0;
        if (resultSet.last()) {
            qtdeRows = resultSet.getRow();
            resultSet.beforeFirst();
        }
        return qtdeRows;
    }
}
