/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author claudivan
 */
public class PgMigratorBase {

    public Connection getSourceConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost/RefeitorioBD", "postgres", "123456");
    }

    public DB getTargetConnection() {
        DB connection = null;
        try {

            ServerAddress address = new ServerAddress("localhost", 27017);
            MongoClient mongoCliente = new MongoClient(address);
            connection = mongoCliente.getDB("refeitoriobd");

        } catch (UnknownHostException ex) {
            Logger.getLogger(PgMigratorBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return connection;
    }
}
