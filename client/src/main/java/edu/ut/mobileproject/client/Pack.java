/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ut.mobileproject.client;

import java.io.Serializable;

/**
 *
 * @author Barca
 */
public class Pack implements Serializable{
    private static final long serialVersionUID = 1;
    String functionName = null;
    Class stateType = null;
    Object state = null;
    Object[] FuncArgValues = null;
    Class[] FuncDTypes = null;

    public Pack(String functionName, Class stateType, Object state, Object[] FuncArgValues, Class[] FuncDTypes) {
        this.functionName = functionName;
        this.stateType = stateType;
        this.state = state;
        this.FuncArgValues = FuncArgValues;
        this.FuncDTypes = FuncDTypes;
    }

    public String getfunctionName(){
        return functionName;
    }
    
    public Class getstateType(){
        return stateType;
    }

    public Object[] getFuncArgValues(){
        return FuncArgValues;
    }

    public Class[] getFuncDTypes(){
        return FuncDTypes;
    }

    public Object getstate(){
        return state;
    }
    
}
