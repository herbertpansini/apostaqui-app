package com.pancini.herbert.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pancini.herbert.entities.Aposta;
import com.pancini.herbert.entities.ItemAposta;

public class ApostaService {
    private final String TAG_APOSTA = "aposta";
    private final String TAG_RESPONSE = "response";
    private final String TAG_ID = "aposta_id";
    private final String TAG_USUARIO = "usuario_id";
    private final String TAG_RODADA = "rodada_id";
    private final String TAG_VALIDA = "valida";
    private final String TAG_ITENS_APOSTA = "itens_aposta";

    public List<Aposta> getAposta(JSONObject jsonObject) {
        ArrayList<Aposta> apostas = new ArrayList<Aposta>();
        try {
            // Getting JSON Array
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Aposta aposta = new Aposta();
                aposta.setId(jObj.getInt(TAG_ID));
                aposta.setUsuarioId(jObj.getInt(TAG_USUARIO));
                aposta.setRodadaId(jObj.getInt(TAG_RODADA));
                aposta.setValida(jObj.getInt(TAG_VALIDA));
                apostas.add(aposta);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return apostas;
    }

    public JSONObject getJSONObject(Aposta aposta, ArrayList<ItemAposta> itensAposta) {
        //Create JSONObject here
        JSONObject jObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try	{
            jsonObject.put(TAG_ID, aposta.getId());
            jsonObject.put(TAG_USUARIO, aposta.getUsuarioId());
            jsonObject.put(TAG_RODADA, aposta.getRodadaId());
            jsonObject.put(TAG_VALIDA, aposta.getValida());

            JSONArray jsonArray = new JSONArray();
            for (ItemAposta item : itensAposta) {
                jsonArray.put( new ItemApostaService().getJSONObject(item) );
            }
            jsonObject.put(TAG_ITENS_APOSTA, jsonArray);

            jObject.put(TAG_APOSTA, jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return jObject;
    }
}
