package com.pancini.herbert.services;

import com.pancini.herbert.entities.Classificacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ClassificacaoService {
    private final String TAG_RESPONSE = "response";
    private final String TAG_USUARIO = "usuario_id";
    private final String TAG_RODADA = "rodada_id";
    private final String TAG_NOME = "nome";
    private final String TAG_APOSTAS = "apostas";
    private final String TAG_PONTOS = "pontos";

    public List<Classificacao> getClassificacao(JSONObject jsonObject) {
        ArrayList<Classificacao> classificacao = new ArrayList<Classificacao>();
        try {
            // Getting JSON Array
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Classificacao obj = new Classificacao();
                obj.setClassificacao(i+1);
                obj.setRodadaId(jObj.getInt(TAG_RODADA));
                obj.setUsuarioId(jObj.getInt(TAG_USUARIO));
                obj.setNome(jObj.getString(TAG_NOME));
                obj.setApostas(jObj.getInt(TAG_APOSTAS));
                obj.setPontos(jObj.getInt(TAG_PONTOS));
                classificacao.add(obj);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return classificacao;
    }
}