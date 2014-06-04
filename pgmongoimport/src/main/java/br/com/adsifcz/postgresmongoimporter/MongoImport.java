/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author claudivan
 */
public class MongoImport {

    private PgMigratorBase migratorBase;
    private DB refeitorioConnection;

    public MongoImport() {
        migratorBase = new PgMigratorBase();
    }

    public void writeToMongoDB(String collection, List<DBObject> dBObjects) {

        System.out.println(collection);

        if (refeitorioConnection == null) {
            refeitorioConnection = migratorBase.getTargetConnection();
        }

        refeitorioConnection.getCollection(collection).insert(dBObjects);

    }

    public void createSequences(Set<String> colecoes) {

        List<DBObject> sequencias = new ArrayList<>();

        for (String colecao : colecoes) {
            Long ultimoId = findLastId(colecao);
            DBObject object = new BasicDBObject();
            object.put("_id", colecao);
            object.put("sequence_value", colecao);
        }



    }

    private Long findLastId(String collection) {

        DB refeitorio = migratorBase.getTargetConnection();

        BasicDBObject all = new BasicDBObject();

        BasicDBObject queryAll = new BasicDBObject();
        queryAll.put("_id", 1);

        BasicDBObject querySorter = new BasicDBObject();
        querySorter.put("_id", -1);

        DBCursor cursor = refeitorio.getCollection(collection).find(all, queryAll).sort(querySorter).limit(1);
        return (Long) cursor.next().get("_id");

    }
}
