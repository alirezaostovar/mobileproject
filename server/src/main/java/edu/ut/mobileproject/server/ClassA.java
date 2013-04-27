package edu.ut.mobileproject.server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author Barca
 */
public class ClassA implements Serializable{
    private static final long serialVersionUID = 4;

    public int add(int a, int b){
        return  a + b;
    }
}

