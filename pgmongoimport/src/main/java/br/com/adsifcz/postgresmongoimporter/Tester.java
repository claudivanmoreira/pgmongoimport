/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter;

import br.com.adsifcz.postgresmongoimporter.core.beans.Documento;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author claudivan
 */
public class Tester {

    static final String schemaFile = "src/main/java/br/com/adsifcz/postgresmongoimporter/schema.xml";
    static Element rootElement;
    static PgMigratorBase migratorBase = new PgMigratorBase();

    public static void main(String[] args) throws Exception {

        SAXBuilder saxb = new SAXBuilder();
        Document doc = saxb.build(new File(schemaFile));

        rootElement = doc.getRootElement();

        //Chega nas tags <documento> de pessoa
        List<Element> nodesDocumentos = rootElement.getChildren();

//
//        Connection pgConnection = migratorBase.getSourceConnection();
//
//        PreparedStatement statement = pgConnection.prepareStatement(schema.getQuery());
//
//        ResultSet resultSet = statement.executeQuery();
//


//        while (resultSet.next()) {
//
//            DBObject dBObject = new BasicDBObject();
//            Map<String, String> mapColunas = schema.getPgColunas();
//            for (String nomeDaColuna : mapColunas.keySet()) {
//                switch (mapColunas.get(nomeDaColuna)) {
//                    case "Long":
//                        dBObject.put(nomeDaColuna, resultSet.getLong(nomeDaColuna));
//                        break;
//                    case "String":
//                        dBObject.put(nomeDaColuna, resultSet.getString(nomeDaColuna));
//                        break;
//                    case "Double":
//                        dBObject.put(nomeDaColuna, resultSet.getDouble(nomeDaColuna));
//                        break;
//                    case "Integer":
//                        dBObject.put(nomeDaColuna, resultSet.getInt(nomeDaColuna));
//                        break;
//                }
//            }
//
//            List<Documento> embeddedDocumentos = schema.getEmbeddedDocumentos();
//
//            for (Documento documento : embeddedDocumentos) {
//
//                DBObject embeddedObject = new BasicDBObject();
//
//                Map<String, String> colunas = documento.getPgColunas();
//
//                for (String nomeDaColuna : colunas.keySet()) {
//                    switch (colunas.get(nomeDaColuna)) {
//                        case "Long":
//                            embeddedObject.put(nomeDaColuna, resultSet.getLong(nomeDaColuna));
//                            break;
//                        case "String":
//                            embeddedObject.put(nomeDaColuna, resultSet.getString(nomeDaColuna));
//                            break;
//                        case "Double":
//                            dBObject.put(nomeDaColuna, resultSet.getDouble(nomeDaColuna));
//                            break;
//                        case "Integer":
//                            dBObject.put(nomeDaColuna, resultSet.getInt(nomeDaColuna));
//                            break;
//                    }
//                }
//
//                dBObject.put(documento.getNome(), embeddedObject);
//            }


        Set<String> nomesDasColecoes = new HashSet<>();

        SchemaParser parser = new SchemaParser();

        MongoImport mongoImport = new MongoImport();

        System.out.println("----------------------------------------");
        System.out.println(" Iniciando Importação");
        System.out.println("----------------------------------------");

        for (Element documentoSchema : nodesDocumentos) {

            Documento schema = parser.getSchemaDoDocumento(documentoSchema);

            String nomeDaColecao = documentoSchema.getAttributeValue("nome");
            
            nomesDasColecoes.add(nomeDaColecao);

//            List<DBObject> dBObjects = new ArrayList<>();
//
//            PgReader pgReader = new PgReader();
//            dBObjects = pgReader.readerPGTable(schema);
//
//            mongoImport.writeToMongoDB(nomeDaColecao, dBObjects);
        }
        
        System.out.println("----------------------------------------");
        System.out.println(" Criando Sequencias");
        System.out.println("----------------------------------------");

        mongoImport.createSequences(nomesDasColecoes);
        
        System.out.println("----------------------------------------");
        System.out.println(" Importação Concluída");
        System.out.println("----------------------------------------");


    }
//}
//private static void mongoImport(List<DBObject> array, String colecao) throws UnknownHostException {
//        MongoClient mongo = new MongoClient("localhost", 27017);
//        DB db = mongo.getDB("RefeitorioBD");
//        db.getCollection(colecao).insert(array);
//    }
}