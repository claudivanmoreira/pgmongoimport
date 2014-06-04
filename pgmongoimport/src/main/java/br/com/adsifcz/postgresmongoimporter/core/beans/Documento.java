/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.adsifcz.postgresmongoimporter.core.beans;

import java.util.List;
import java.util.Map;

/**
 *
 * @author claudivan
 */
public class Documento {

    private String nome;
    private String query;
    private String nomePk;
    private String tipo;
    private List<String> queryParameters;
    private Map<String, String> mapColunas;
    private List<Documento> embeddedDocumentos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getNomePk() {
        return nomePk;
    }

    public void setNomePk(String nomePk) {
        this.nomePk = nomePk;
    }

    public Map<String, String> getMapColunas() {
        return mapColunas;
    }

    public void setMapColunas(Map<String, String> mapColunas) {
        this.mapColunas = mapColunas;
    }

    public List<Documento> getEmbeddedDocumentos() {
        return embeddedDocumentos;
    }

    public void setEmbeddedDocumentos(List<Documento> embeddedDocumentos) {
        this.embeddedDocumentos = embeddedDocumentos;
    }

    public List<String> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(List<String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (tipo != null) {
            this.tipo = tipo;
        }
        this.tipo = "embedded";
    }

    @Override
    public String toString() {
        return "Documento{" + "nome=" + nome + ", query=" + query + ", nomePk=" + nomePk + ", pgColunas=" + mapColunas + ", embeddedDocumentos=" + embeddedDocumentos + '}';
    }
}
