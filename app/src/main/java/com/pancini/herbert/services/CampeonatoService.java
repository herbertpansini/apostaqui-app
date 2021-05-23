package com.pancini.herbert.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pancini.herbert.entities.Campeonato;

public class CampeonatoService {
	private final String TAG_CAMPEONATO = "campeonato";
    private final String TAG_RESPONSE = "response";
    private final String TAG_ID = "campeonato_id";
    private final String TAG_NOME = "nome";
    private final String TAG_ESPORTE_ID = "esporte_id";
    private final String TAG_NOME_ESPORTE = "nome_esporte";

    public List<Campeonato> getCampeonato(JSONObject jsonObject) {
        ArrayList<Campeonato> campeonatos = new ArrayList<Campeonato>();
        try {
            // Getting JSON Array
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Campeonato campeonato = new Campeonato();
                campeonato.setId(jObj.getInt(TAG_ID));
                campeonato.setNome(jObj.getString(TAG_NOME));
                campeonato.setEsporteId(jObj.getInt(TAG_ESPORTE_ID));
                campeonato.setNomeEsporte(jObj.getString(TAG_NOME_ESPORTE));
                campeonatos.add(campeonato);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return campeonatos;
    }
    
    public JSONObject getJSONObject(Campeonato campeonato) {
    	//Create JSONObject here
    	JSONObject jObject = new JSONObject();
	    JSONObject jsonObject = new JSONObject();	    
    	try	{
		    jsonObject.put(TAG_NOME, campeonato.getNome());
		    jsonObject.put(TAG_ESPORTE_ID, campeonato.getEsporteId());
		    jObject.put(TAG_CAMPEONATO, jsonObject);
    	} catch (JSONException e) {
    		// TODO Auto-generated catch block
    	    //e.printStackTrace();
    	}	    
	    return jObject;
    }
}
