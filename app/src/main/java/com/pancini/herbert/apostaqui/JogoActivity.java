package com.pancini.herbert.apostaqui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.pancini.herbert.adapters.JogoAdapter;
import com.pancini.herbert.entities.Campeonato;
import com.pancini.herbert.entities.Jogo;
import com.pancini.herbert.entities.User;
import com.pancini.herbert.persistence.UserDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JogoActivity extends AppCompatActivity {
    private ListView lv;
    JogoAdapter adapter;
    TextView tvHeader;
    TextView tvTitle;

    private Campeonato campeonato;

    private User mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        mUser = new UserDao(JogoActivity.this).get();

        // get Intents
        this.getIntents();

        // find View By Id
        this.findViewById();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarJogo);
        toolbar.setTitle(this.campeonato.getNome());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabJogo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getResources().getString(R.string.default_url) + "sequence";
                new MineTaskAsync().execute(url);

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        // load data
        new MyTaskAsync().execute();
    }

    // Get Intents
    private void getIntents() {
        Intent intent = getIntent();
        this.campeonato = (Campeonato)intent.getSerializableExtra("CAMPEONATO");
    }

    // find View By Id
    private void findViewById() {
        /*this.tvTitle = (TextView)findViewById(R.id.tvTitle);
        this.tvTitle.setText(this.campeonato.getNome());
        this.tvHeader = (TextView)findViewById(R.id.item_header);*/
        this.lv = (ListView) findViewById(R.id.listViewJogos);
    }

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

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "confronto/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(JogoActivity.this);
            progressDialog.setMessage("Getting Data of Teams...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            // Initialize Object JSONParser
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jsonParser.getJSONFromUrl(mUrl + campeonato.getId());
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            ArrayList<Jogo> confrontos = new ArrayList<Jogo>();
            try {
                // Getting JSON Array
                JSONArray jsonArray = json.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    Jogo jogo = new Jogo();
                    jogo.setId(jObj.getInt("confronto_id"));
                    jogo.setRodada(jObj.getInt("rodada_id"));
                    jogo.setNumeroRodada(jObj.getInt("numero_rodada"));
                    jogo.setEquipeMandante(jObj.getInt("equipe_mandante"));
                    jogo.setNomeEquipeMandante(jObj.getString("nome_equipe_mandante"));
                    jogo.setSiglaEquipeMandante(jObj.getString("sigla_equipe_mandante"));
                    try	{
                        jogo.setPlacarEquipeMandante(jObj.getInt("placar_equipe_mandante"));
                    } catch (Exception e) {

                    }
                    try	{
                        jogo.setPlacarEquipeVisitante(jObj.getInt("placar_equipe_visitante"));
                    } catch (Exception e) {

                    }
                    jogo.setEquipeVisitante(jObj.getInt("equipe_visitante"));
                    jogo.setNomeEquipeVisitante(jObj.getString("nome_equipe_visitante"));
                    jogo.setSiglaEquipeVisitante(jObj.getString("sigla_equipe_visitante"));
                    jogo.setHorario(convertToDate(jObj.getString("horario")));
                    confrontos.add(jogo);
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }

            adapter = new JogoAdapter(getBaseContext(), confrontos);
            lv.setAdapter(adapter);

            if (confrontos != null && confrontos.size() > 0) {
                //tvHeader.setText(confrontos.get(0).getNumeroRodada() + "ª Rodada");
            }
            progressDialog.dismiss();
        }
    }

    private class MineTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(JogoActivity.this);
            progressDialog.setMessage("Carregando... Por favor, aguarde.");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jsonParser.getJSONFromUrl(args[0]);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            JSONParser jsonParser = new JSONParser();
            int aposta_nextValue = jsonParser.getResponse(json);

            if (aposta_nextValue > 0) {
                try	{
                    Intent intent = new Intent(JogoActivity.this, ApostaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("USUARIO_ID", mUser.getId());
                    intent.putExtra("APOSTA_ID", aposta_nextValue);
                    intent.putExtra("CAMPEONATO", campeonato);
                    startActivity(intent);
                } catch(Exception e) {
                    //e.printStackTrace();
                }
            } else {

            }
            progressDialog.dismiss();
        }
    }
}
