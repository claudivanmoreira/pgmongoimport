/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pgmongoimport;

/**
 *
 * @author Claudivan Moreira
 */
public class ApplicationException extends Exception {

    public ApplicationException(Throwable causa) {
        super(causa);
    }

}
