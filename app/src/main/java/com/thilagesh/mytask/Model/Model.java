package com.thilagesh.mytask.Model;

import java.util.ArrayList;

public class Model {

    public enum STATE {
        CLOSED,
        OPENED
    }

    public  String name;
    public int level;
    public  STATE state = STATE.CLOSED;
    public   String designation;
    public   ArrayList<Model> models = new ArrayList<>();

    public Model(String name, int level , String designation) {
        this.name = name;
        this.level = level;
        this.designation = designation;
    }

}
