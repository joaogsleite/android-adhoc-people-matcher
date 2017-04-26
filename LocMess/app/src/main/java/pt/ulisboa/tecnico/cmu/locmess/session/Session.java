package pt.ulisboa.tecnico.cmu.locmess.session;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Session {

    public static final String APP_NAME = "pt.ulisboa.tecnico.cmu.locmess";
    public static final String BASE_URL = "http://cmu.n1z.pt:8080";

    private static Session instance = null;

    private RequestQueue queue;
    private SharedPreferences prefs;

    private Session(Context context, SharedPreferences prefs) {
        this.queue = Volley.newRequestQueue(context);
        this.prefs = prefs;
    }
    public static Session getInstance(){ return instance; }
    public static Session getInstance(Activity act) {
        if(instance==null) {
            Context context = act.getApplicationContext();
            SharedPreferences prefs = act.getSharedPreferences(APP_NAME, context.MODE_PRIVATE);
            instance = new Session(context, prefs);
            Log.d("Session", "Init");
        }
        return instance;
    }

    // --------------------------------------------

    public void request(StringRequest req){
        this.queue.add(req);
    }

    public SharedPreferences.Editor editor(){
        return prefs.edit();
    }

    // --------------------------------------------

    public boolean isLoggedIn(){
        try{
            return prefs.getBoolean("login", false);
        }
        catch (Exception e){
            return false;
        }
    }

    public String token(){
        if(isLoggedIn())
            return prefs.getString("token","...");
        else
            return null;
    }

    public void token(String newToken){
        if(newToken != null){
            SharedPreferences.Editor editor = editor();
            editor.clear();
            editor.putBoolean("login", true);
            editor.putString("token", newToken);
            editor.apply();
        }
        else logout();
    }

    // --------------------------------------------

    public void logout(){
        SharedPreferences.Editor editor = editor();
        editor.clear();
        editor.putBoolean("login",false);
        editor.putString("token",null);
        editor.apply();
    }
}