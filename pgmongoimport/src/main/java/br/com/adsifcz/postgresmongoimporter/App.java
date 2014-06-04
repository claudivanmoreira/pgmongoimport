package br.com.adsifcz.postgresmongoimporter;

/**
 * Hello world!
 *
 */
public class App {

    static final String schemaFile = "src/main/java/br/com/adsifcz/postgresmongoimporter/schema.xml";
    static final PgMigratorBase MIGRATOR_BASE = new PgMigratorBase();

//    public static void main(String[] args) throws SQLException, JDOMException, IOException, JSONException {
//
//
//        SAXBuilder saxb = new SAXBuilder();
//
//        Document document = saxb.build(new File(schemaFile));
//
//        Element rootElement = document.getRootElement();
//
//        List<Element> collections = rootElement.getChildren("collection");
//
//        Element collection = collections.get(0);
//
//        String nomeDaColecao = collection.getAttributeValue("nome");
//
//        System.out.println("Nome da Coleção : " + nomeDaColecao);
//
//        List<Element> documentsSchema = collection.getChildren("documento");
//
//        Element schema = documentsSchema.get(0);
//
////        loadDados(schema);
//
//    }

//    public static JSONArray loadDados(Element schema) throws SQLException, JSONException {
//
//        List<Element> campos = schema.getChildren("campo");
//
//        String query = schema.getAttributeValue("query");
//
//        Connection connection = MIGRATOR_BASE.getSourceConnection();
//
//        Statement statement = connection.createStatement();
//
//        ResultSet resulSet = statement.executeQuery(query);
//
//        JSONArray tuplas = new JSONArray();
//
//        while (resulSet.next()) {
//
//            JSONObject object = new JSONObject();
//
//            for (Element campo : campos) {
//
//                String nomeDoCampo = campo.getAttributeValue("nome");
//
//                String nomeDaColuna = campo.getAttributeValue("coluna");
//
//                if (campo.getAttributeValue("embedded") == null) {
//
//                    String tipoDoCampo = campo.getAttributeValue("tipo");
//
//                    switch (tipoDoCampo) {
//                        case "String":
//                            object.put(nomeDoCampo, resulSet.getString(nomeDaColuna));
//                            break;
//                        case "Long":
//                            object.put(nomeDoCampo, resulSet.getLong(nomeDaColuna));
//                            break;
//                    }
//                } else {
//
//                    List<Element> embeddedcampos = campo.getChildren();
//
//                    JSONObject embeddedJSONObject = new JSONObject();
//
//
//
//                    for (Element embeddedCampo : embeddedcampos) {
//
//                        nomeDoCampo = embeddedCampo.getAttributeValue("nome");
//
//                        nomeDaColuna = embeddedCampo.getAttributeValue("coluna");
//
//                        String tipoDoCampo = embeddedCampo.getAttributeValue("tipo");
//
//                        switch (tipoDoCampo) {
//                            case "String":
//                                embeddedJSONObject.put(nomeDoCampo, resulSet.getString(nomeDaColuna));
//                                break;
//                            case "Long":
//                                embeddedJSONObject.put(nomeDoCampo, resulSet.getLong(nomeDaColuna));
//                                break;
//                        }
//                    }
//
//                    if (campo.getAttributeValue("nome").equals("usuario")) {
//                        object.put("usuario", embeddedJSONObject);
//                    } else {
//                        object.put("contato", embeddedJSONObject);
//                    }
//                }
//            }
//
//            System.out.println(object.toString());
//        }
//        return null;
//    }

//    public static JSONArray getEmbeddedDocuments(Element node) {
//
//        JSONArray jSONArray = new JSONArray();
//
//        List<Element> embeddedcampos = node.getChildren();
//
//        JSONObject embeddedObject = new JSONObject();
//        
//        for (Element campo : embeddedcampos) {
//
//            nomeDoCampo = embeddedCampo.getAttributeValue("nome");
//
//            nomeDaColuna = embeddedCampo.getAttributeValue("coluna");
//
//            String tipoDoCampo = embeddedCampo.getAttributeValue("tipo");
//
//            switch (tipoDoCampo) {
//                case "String":
//                    embeddedJSONObject.put(nomeDoCampo, resulSet.getString(nomeDaColuna));
//                    break;
//                case "Long":
//                    embeddedJSONObject.put(nomeDoCampo, resulSet.getLong(nomeDaColuna));
//                    break;
//            }
//        }
//
//        if (campo.getAttributeValue("nome").equals("usuario")) {
//            object.put("usuario", embeddedJSONObject);
//        } else {
//            object.put("contato", embeddedJSONObject);
//        }
//
//
//    }
}
