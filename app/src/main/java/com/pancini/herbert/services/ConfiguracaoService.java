package com.pancini.herbert.services;

import com.pancini.herbert.entities.Configuracao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfiguracaoService {
    private final String TAG_CONFIGURACAO = "configuracao";
    private final String TAG_RESPONSE = "response";
    private final String TAG_ID = "configuracao_id";
    private final String TAG_QUANTIDADE_HORAS = "quantidade_horas";

    public Configuracao getConfiguracao(JSONObject jsonObject) {
        Configuracao config = new Configuracao();
        try {
            // Getting JSON Object
            JSONObject jObj = jsonObject.getJSONObject(TAG_RESPONSE);
            config.setId(jObj.getInt(TAG_ID));
            config.setQuantidadeHoras(jObj.getInt(TAG_QUANTIDADE_HORAS));
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return config;
    }

    public JSONObject getJSONObject(Configuracao config) {
        //Create JSONObject here
        JSONObject jObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try	{
            //jsonObject.put(TAG_ID, config.getId());
            jsonObject.put(TAG_QUANTIDADE_HORAS, config.getQuantidadeHoras());
            jObject.put(TAG_CONFIGURACAO, jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return jObject;
    }
}