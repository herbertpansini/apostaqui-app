package com.pancini.herbert.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pancini.herbert.entities.Rodada;

public class RodadaService {
    private final String TAG_RODADA = "rodada";
    private final String TAG_RESPONSE = "response";
    private final String TAG_ID = "rodada_id";
    private final String TAG_ANO = "ano";
    private final String TAG_NUMERO = "numero";
    private final String TAG_CAMPEONATO_ID = "campeonato_id";
    private final String TAG_ATIVA = "ativa";
    private final String TAG_FINALIZADA = "finalizada";

    public List<Rodada> getRodada(JSONObject jsonObject) {
        ArrayList<Rodada> rodadas = new ArrayList<Rodada>();
        try {
            // Getting JSON Array
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Rodada rodada = new Rodada();
                rodada.setId(jObj.getInt(TAG_ID));
                rodada.setAno(jObj.getInt(TAG_ANO));
                rodada.setNumero(jObj.getInt(TAG_NUMERO));
                rodada.setCampeonatoId(jObj.getInt(TAG_CAMPEONATO_ID));
                rodada.setAtiva(jObj.getInt(TAG_ATIVA));
                rodada.setFinalizada(jObj.getInt(TAG_FINALIZADA));
                rodadas.add(rodada);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return rodadas;
    }

    public JSONObject getJSONObject(Rodada rodada) {
        //Create JSONObject here
        JSONObject jObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try	{
            //jsonObject.put(TAG_ID, rodada.getId());
            jsonObject.put(TAG_ANO, rodada.getAno());
            jsonObject.put(TAG_NUMERO, rodada.getNumero());
            jsonObject.put(TAG_CAMPEONATO_ID, rodada.getCampeonatoId());
            jsonObject.put(TAG_ATIVA, rodada.isAtiva());
            jObject.put(TAG_RODADA, jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return jObject;
    }
}
