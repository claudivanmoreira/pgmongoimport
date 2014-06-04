/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter;

import br.com.adsifcz.postgresmongoimporter.core.beans.Documento;
import br.com.adsifcz.postgresmongoimporter.core.DBSchema;
import br.com.adsifcz.postgresmongoimporter.core.Unmarhaller;
import com.mongodb.DBObject;
import java.io.IOException;
import java.util.List;
import org.jdom.JDOMException;

/**
 *
 * @author claudivan
 */
public class Testador {

    static Unmarhaller unmarhaller = Unmarhaller.createUnmarhaller();
    static DBSchema dBSchema = new DBSchema();
    static MongoImport mongoImport = new MongoImport();

    public static void main(String[] args) throws IOException, JDOMException {

        List<Documento> documentos = (List<Documento>) unmarhaller.unmarshal(Documento.class);

        for (int i = 0; i < documentos.size(); i++) {
//
            Documento documento = documentos.get(i);
//
            List<DBObject> jsonsMongoDB = dBSchema.getDBObjectsFromDocumento(documento);
//
            mongoImport.writeToMongoDB(documento.getNome(), jsonsMongoDB);
//
//
        }

    }
}
