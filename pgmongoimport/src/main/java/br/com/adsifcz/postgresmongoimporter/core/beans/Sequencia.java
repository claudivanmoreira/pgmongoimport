/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter.core.beans;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author claudivan
 */
public class Sequencia {

    private Map<String, String> mapColunas;
    private String query;
    private String nome;

    public Map<String, String> getMapColunas() {
        if (mapColunas == null) {
            mapColunas = new HashMap<>();
        }
        return mapColunas;
    }

    public void setMapColunas(Map<String, String> mapColunas) {
        this.mapColunas = mapColunas;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Sequencia{" + "mapColunas=" + mapColunas.toString() + ", query=" + query + '}';
    }
}
