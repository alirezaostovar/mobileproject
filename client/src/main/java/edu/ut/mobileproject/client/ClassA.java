package edu.ut.mobileproject.client;


import java.io.Serializable;

/**
 *
 * @author Barca
 */
public class ClassA implements Serializable {
    private static final long serialVersionUID = 4;

    public int add(int a, int b){
        return  a + b;
    }
}

