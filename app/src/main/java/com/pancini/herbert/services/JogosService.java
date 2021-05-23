package com.pancini.herbert.services;

import com.pancini.herbert.entities.Aposta;
import com.pancini.herbert.entities.Jogos;
import com.pancini.herbert.entities.Rodada;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JogosService {
    private final String TAG_RESPONSE = "response";
    private final String TAG_APOSTA = "aposta_id";
    private final String TAG_RODADA = "numero";
    private final String TAG_NOME_EQUIPE_MANDANTE = "nome_equipe_mandante";
    private final String TAG_SIGLA_EQUIPE_MANDANTE = "sigla_equipe_mandante";
    private final String TAG_OPCAO = "opcao";
    private final String TAG_NOME_EQUIPE_VISITANTE = "nome_equipe_visitante";
    private final String TAG_SIGLA_EQUIPE_VISITANTE = "sigla_equipe_visitante";
    private final String TAG_JOGOU = "jogou";
    private final String TAG_ACERTOU = "acertou";

    private ArrayList<Aposta> apostas;

    public JogosService() {

    }

    public JogosService(ArrayList<Aposta> apostas) {
        this.apostas = apostas;
    }

    private int getPontos(int apostaId) {
        int ponto = 0;
        for (Aposta aposta : this.apostas) {
            if (aposta.getId() == apostaId) {
                ponto = aposta.getPontos();
                break;
            }
        }
        return ponto;
    }

    private int getValidacao(int apostaId) {
        int valida = 0;
        for (Aposta aposta : this.apostas) {
            if (aposta.getId() == apostaId) {
                valida = aposta.getValida();
                break;
            }
        }
        return valida;
    }

    public List<Jogos> getJogos(JSONObject jsonObject) {
        ArrayList<Jogos> collection = new ArrayList<Jogos>();
        try {
            int apostaId = 0;
            int numeroRodada = 0;
            // Getting JSON Array
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Jogos obj = new Jogos();
                obj.setApostaId(jObj.getInt(TAG_APOSTA));
                obj.setNumeroRodada(jObj.getInt(TAG_RODADA));
                obj.setNomeEquipeMandante(jObj.getString(TAG_NOME_EQUIPE_MANDANTE));
                obj.setSiglaEquipeMandante(jObj.getString(TAG_SIGLA_EQUIPE_MANDANTE));
                obj.setOpcao(jObj.getInt(TAG_OPCAO));
                obj.setSiglaEquipeVisitante(jObj.getString(TAG_SIGLA_EQUIPE_VISITANTE));
                obj.setNomeEquipeVisitante(jObj.getString(TAG_NOME_EQUIPE_VISITANTE));
                obj.setJogou(jObj.getInt(TAG_JOGOU));
                obj.setAcertou(jObj.getInt(TAG_ACERTOU));

                if (obj.getNumeroRodada() != numeroRodada) {
                    numeroRodada = obj.getNumeroRodada();
                    Rodada rodada = new Rodada();
                    rodada.setNumero(obj.getNumeroRodada());

                    Jogos jogo = new Jogos();
                    jogo.setDescricaoRodada(rodada.toString());
                    jogo.setHeader(true);
                    collection.add(jogo);
                }

                if (obj.getApostaId() != apostaId) {
                    apostaId = obj.getApostaId();
                    Jogos jogo = new Jogos();
                    jogo.setApostaId(apostaId);
                    jogo.setPontos(this.getPontos(apostaId));
                    jogo.setValida(this.getValidacao(apostaId));
                    jogo.setTitle(true);

                    collection.add(jogo);
                }
                collection.add(obj);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return collection;
    }
}