package com.pancini.herbert.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pancini.herbert.entities.Confronto;

public class ConfrontoService {
    private final String TAG_CONFRONTO = "confronto";
    private final String TAG_RESPONSE = "response";
    private final String TAG_ID = "confronto_id";
    private final String TAG_RODADA = "rodada_id";
    private final String TAG_EQUIPE_MANDANTE = "equipe_mandante";
    private final String TAG_NOME_EQUIPE_MANDANTE = "nome_equipe_mandante";
    private final String TAG_SIGLA_EQUIPE_MANDANTE = "sigla_equipe_mandante";
    private final String TAG_PLACAR_EQUIPE_MANDANTE = "placar_equipe_mandante";
    private final String TAG_PLACAR_EQUIPE_VISITANTE = "placar_equipe_visitante";
    private final String TAG_EQUIPE_VISITANTE = "equipe_visitante";
    private final String TAG_NOME_EQUIPE_VISITANTE = "nome_equipe_visitante";
    private final String TAG_SIGLA_EQUIPE_VISITANTE = "sigla_equipe_visitante";
    private final String TAG_HORARIO = "horario";

    private Date convertToDate(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date convertedDate = null;
        try {
            convertedDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return convertedDate;
    }

    private String convertToString(Date horario) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Set your date format
        String formattedDate = sdf.format(horario);

        return formattedDate;
    }

    public List<Confronto> getConfronto(JSONObject jsonObject) {
        ArrayList<Confronto> confrontos = new ArrayList<Confronto>();
        try {
            // Getting JSON Array
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Confronto confronto = new Confronto();
                confronto.setId(jObj.getInt(TAG_ID));
                confronto.setRodada(jObj.getInt(TAG_RODADA));
                confronto.setEquipeMandante(jObj.getInt(TAG_EQUIPE_MANDANTE));
                confronto.setNomeEquipeMandante(jObj.getString(TAG_NOME_EQUIPE_MANDANTE));
                confronto.setSiglaEquipeMandante(jObj.getString(TAG_SIGLA_EQUIPE_MANDANTE));
                try	{
                    confronto.setPlacarEquipeMandante(jObj.getInt(TAG_PLACAR_EQUIPE_MANDANTE));
                } catch (Exception e) {

                }
                try	{
                    confronto.setPlacarEquipeVisitante(jObj.getInt(TAG_PLACAR_EQUIPE_VISITANTE));
                } catch (Exception e) {

                }
                confronto.setEquipeVisitante(jObj.getInt(TAG_EQUIPE_VISITANTE));
                confronto.setNomeEquipeVisitante(jObj.getString(TAG_NOME_EQUIPE_VISITANTE));
                confronto.setSiglaEquipeVisitante(jObj.getString(TAG_SIGLA_EQUIPE_VISITANTE));
                confronto.setHorario(this.convertToDate(jObj.getString(TAG_HORARIO)));
                confrontos.add(confronto);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return confrontos;
    }

    public JSONObject getJSONObject(Confronto confronto) {
        //Create JSONObject here
        JSONObject jObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try	{
            //jsonObject.put(TAG_ID, confronto.getId());
            jsonObject.put(TAG_RODADA, confronto.getRodada());
            jsonObject.put(TAG_EQUIPE_MANDANTE, confronto.getEquipeMandante());

            if (confronto.getPlacarEquipeMandante() != null) {
                jsonObject.put(TAG_PLACAR_EQUIPE_MANDANTE, confronto.getPlacarEquipeMandante());
            } else {
                jsonObject.put(TAG_PLACAR_EQUIPE_MANDANTE, JSONObject.NULL);
            }

            if (confronto.getPlacarEquipeVisitante() != null) {
                jsonObject.put(TAG_PLACAR_EQUIPE_VISITANTE, confronto.getPlacarEquipeVisitante());
            } else {
                jsonObject.put(TAG_PLACAR_EQUIPE_VISITANTE, JSONObject.NULL);
            }
            jsonObject.put(TAG_EQUIPE_VISITANTE, confronto.getEquipeVisitante());
            jsonObject.put(TAG_HORARIO, this.convertToString(confronto.getHorario()));
            jObject.put(TAG_CONFRONTO, jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return jObject;
    }
}