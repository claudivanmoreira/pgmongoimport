/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter;

import static br.com.adsifcz.postgresmongoimporter.Tester.migratorBase;
import br.com.adsifcz.postgresmongoimporter.core.beans.Documento;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author claudivan
 */
public class PgReader {

    private Connection connection;

    public List<DBObject> readerPGTable(Documento documentSchema) throws SQLException {

        List<DBObject> dBObjects = new ArrayList<>();

        //Executa a query para recuperar o agregado
        ResultSet resultSet = executeQuery(documentSchema.getQuery());

        while (resultSet.next()) {

            DBObject dBObject = new BasicDBObject();
            dBObject = readDBObjectFromResultSet(resultSet, documentSchema.getMapColunas(), dBObject);
            // Se tem documentos aninhados
            if (!documentSchema.getEmbeddedDocumentos().isEmpty()) {


                // Para cada documento lê o resultSet com os campos do documento
                for (Documento embeddedDocumento : documentSchema.getEmbeddedDocumentos()) {

                    //Utilizado para verificar se há mais de 1 resultado na query dos documentos aninhados (relacionamentos 1 -> N)
                    int qtdeResultadosQueryEmbeddeds = 0;

                    //Utilizada para armazenar os objetos a serem aninhados quando a query obtiver mais de 1 resultado
                    List<DBObject> listDBObjectsQueryEmbeddeds = new ArrayList<>();

                    DBObject dbObjectEmbedded = new BasicDBObject();

                    // Verifica se é necessário uma Query extra para preencher o documento
                    if (embeddedDocumento.getQuery() != null) {

                        String sqlQuery = embeddedDocumento.getQuery();

                        // Verifica se a query tem parametros
                        if (embeddedDocumento.getQueryParameters() != null) {
                            //Seta os parametros na query
                            List<String> parameterNames = embeddedDocumento.getQueryParameters();
                            for (int i = 0; i < parameterNames.size(); i++) {
                                sqlQuery = sqlQuery.replace("?" + i, resultSet.getString(parameterNames.get(i)));
                            }
                        }

                        // Executa a Query extra para preencher o documento embedded
                        ResultSet resultSetEmbedded = executeQuery(sqlQuery);

                        //Verifica a quantidade de resultados no resultSet
                        if (resultSetEmbedded.last()) {
                            qtdeResultadosQueryEmbeddeds = resultSetEmbedded.getRow();
                            resultSetEmbedded.beforeFirst();
                        }

                        while (resultSetEmbedded.next()) {

                            // Lê o resultSet da consulta com os campos do documento a ser aninhado
                            dbObjectEmbedded = readDBObjectFromResultSet(resultSetEmbedded, embeddedDocumento.getMapColunas(), dbObjectEmbedded);

                            // Se a query tem mais de um documento a ser aninhado, é utilizado a lista abaixo para guardar esses documentos
                            if (qtdeResultadosQueryEmbeddeds > 0) {
                                listDBObjectsQueryEmbeddeds.add(dbObjectEmbedded);
                                //Necessário se não só fica salvo o ultimo ID
                                dbObjectEmbedded = new BasicDBObject();
                            }
                        }

                    } else {
                        // Caso não hava query para executar, preenche o documento aninhado com o primeiro resultSet
                        dbObjectEmbedded = readDBObjectFromResultSet(resultSet, embeddedDocumento.getMapColunas(), dbObjectEmbedded);
                    }

                    // Se a query do documento aninhado retornou mais de um documento a ser aninhado
                    if (!listDBObjectsQueryEmbeddeds.isEmpty()) {
                        // Adiciona a lista de documentos embededds no documento principal
                        dBObject.put(embeddedDocumento.getNome(), listDBObjectsQueryEmbeddeds);

                        //Elimia documentos vazios
                    } else if (!dbObjectEmbedded.toMap().isEmpty()) {
                        // Adiciona o documento embededd no documento principal
                        dBObject.put(embeddedDocumento.getNome(), dbObjectEmbedded);
                    }
                }
            }

            dBObjects.add(dBObject);

        }

        connection.close();
        connection = null;

        return dBObjects;

    }

    private ResultSet executeQuery(String sqlQuery) throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = migratorBase.getSourceConnection();
        }
        PreparedStatement statement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return statement.executeQuery();
    }

    private DBObject readDBObjectFromResultSet(ResultSet resultSet, Map<String, String> mapColunas, DBObject dBObject) throws SQLException {

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
}