package com.pancini.herbert.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pancini.herbert.entities.ItemAposta;

public class ItemApostaService {
    private final String TAG_ITEM_APOSTA = "item_aposta";
    private final String TAG_RESPONSE = "response";
    private final String TAG_ID = "item_aposta_id";
    private final String TAG_APOSTA = "aposta_id";
    private final String TAG_CONFRONTO = "confronto_id";
    private final String TAG_CURRENT = "opcao";

    public List<ItemAposta> getItemAposta(JSONObject jsonObject) {
        ArrayList<ItemAposta> itensAposta = new ArrayList<ItemAposta>();
        try {
            // Getting JSON Array
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                ItemAposta itemAposta = new ItemAposta();
                itemAposta.setId(jObj.getInt(TAG_ID));
                itemAposta.setApostaId(jObj.getInt(TAG_APOSTA));
                itemAposta.setConfrontoId(jObj.getInt(TAG_CONFRONTO));
                itemAposta.setCurrent(jObj.getInt(TAG_CURRENT));
                itensAposta.add(itemAposta);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return itensAposta;
    }

    public JSONObject getJSONObject(ItemAposta itemAposta) {
        JSONObject jsonObject = new JSONObject();
        try	{
            jsonObject.put(TAG_APOSTA, itemAposta.getApostaId());
            jsonObject.put(TAG_CONFRONTO, itemAposta.getConfrontoId());
            jsonObject.put(TAG_CURRENT, itemAposta.getCurrent());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return jsonObject;
    }
}
