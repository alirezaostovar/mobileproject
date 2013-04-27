/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ut.mobileproject.server;

import java.io.Serializable;

/**
 *
 * @author Barca
 */
public class ResultPack implements Serializable{
    private static final long serialVersionUID = 2;
    Object result = null;
    Object state = null;

    public ResultPack(Object result, Object state) {
        this.result = result;
        this.state = state;
    }

    public Object getresult(){
        return result;
    }

    public Object getstate(){
        return state;
    }

}
