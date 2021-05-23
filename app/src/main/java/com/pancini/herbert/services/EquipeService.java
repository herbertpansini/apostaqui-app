package com.pancini.herbert.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.pancini.herbert.entities.Equipe;

public class EquipeService {
    private final String TAG_EQUIPE = "equipe";
    private final String TAG_RESPONSE = "response";
    private final String TAG_ID = "equipe_id";
    private final String TAG_NOME = "nome";
    private final String TAG_SIGLA = "sigla";

    public List<Equipe> getEquipe(JSONObject jsonObject) {
        ArrayList<Equipe> equipes = new ArrayList<Equipe>();
        try {
            // Getting JSON Array
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Equipe equipe = new Equipe();
                equipe.setId(jObj.getInt(TAG_ID));
                equipe.setNome(jObj.getString(TAG_NOME));
                equipe.setSigla(jObj.getString(TAG_SIGLA));
                equipes.add(equipe);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return equipes;
    }

    public JSONObject getJSONObject(Equipe equipe) {
        //Create JSONObject here
        JSONObject jObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try	{
            //jsonObject.put(TAG_ID, equipe.getId());
            jsonObject.put(TAG_NOME, equipe.getNome());
            jsonObject.put(TAG_SIGLA, equipe.getSigla());
            jObject.put(TAG_EQUIPE, jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return jObject;
    }
}