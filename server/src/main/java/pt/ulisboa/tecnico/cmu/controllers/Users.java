package pt.ulisboa.tecnico.cmu.controllers;

import org.json.JSONObject;

public class Users extends Controller{

    public JSONObject getUserByUsername(String username){
        return get(username).toJSON();
    }
}
