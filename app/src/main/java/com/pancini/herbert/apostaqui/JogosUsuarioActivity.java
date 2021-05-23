package com.pancini.herbert.apostaqui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.ListView;
import com.pancini.herbert.adapters.MeusJogosAdapter;
import com.pancini.herbert.entities.Aposta;
import com.pancini.herbert.entities.Jogos;
import com.pancini.herbert.services.JogosService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class JogosUsuarioActivity extends AppCompatActivity {
    //URL to get JSON Array


    private MeusJogosAdapter mAdapter;
    ArrayList<Aposta> mTodasApostas;
    private int rodadaId;
    private int usuarioId;
    private String nomeUsuario;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogos_usuario);

        // get Intents
        this.getIntents();

        // find View By Id
        this.findViewById();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarJogosUsuario);
        toolbar.setTitle(this.nomeUsuario);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new MineTaskAsync().execute();

        new MyTaskAsync().execute();
    }

    private void getIntents() {
        Intent intent = getIntent();
        this.rodadaId = (int)intent.getSerializableExtra("RODADA_ID");
        this.usuarioId = (int)intent.getSerializableExtra("USUARIO_ID");
        this.nomeUsuario = (String)intent.getSerializableExtra("USUARIO_NOME");
    }

    // find View By Id
    private void findViewById() {
        this.lv = (ListView)findViewById(R.id.listViewJogosUsuario);
    }

    private class MineTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "tabela/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(JogosUsuarioActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jsonParser.getJSONFromUrl(mUrl + rodadaId + "/" + usuarioId);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            mTodasApostas = new ArrayList<Aposta>();
            try {
                // Getting JSON Array
                JSONArray jsonArray = json.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    Aposta aposta = new Aposta();
                    aposta.setId(jObj.getInt("aposta_id"));
                    aposta.setUsuarioId(jObj.getInt("usuario_id"));
                    aposta.setPontos(jObj.getInt("pontos"));
                    aposta.setValida(jObj.getInt("valida"));
                    mTodasApostas.add(aposta);
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "jogos/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(JogosUsuarioActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jsonParser.getJSONFromUrl(mUrl + rodadaId + "/" + usuarioId);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            JogosService jogosService = new JogosService(mTodasApostas);
            ArrayList<Jogos> jogos = (ArrayList<Jogos>)jogosService.getJogos(json);

            mAdapter = new MeusJogosAdapter(getBaseContext(), jogos);
            lv.setAdapter(mAdapter);

            progressDialog.dismiss();
        }
    }
}
