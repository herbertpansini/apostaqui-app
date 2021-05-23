package com.pancini.herbert.apostaqui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pancini.herbert.entities.Campeonato;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.entities.User;
import com.pancini.herbert.persistence.UserDao;
import com.pancini.herbert.services.RodadaService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RodadaActivity extends AppCompatActivity {
    ArrayList<Rodada> mTodasRodadas;
    ListView listView;
    TextView emptyTextView;

    private int podeApostar = 0;
    private User mUser = null;
    private Campeonato campeonato = null;
    private Rodada mRodada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rodada);

        mUser = new UserDao(RodadaActivity.this).get();
        campeonato = new Campeonato(1, "Brasileirão Séria A", 1, "Futebol");

        this.findViewById();

        new RodadaTaskAsync().execute();
    }

    private void findViewById() {
        listView = (ListView) findViewById(android.R.id.list);
        emptyTextView = (TextView) findViewById(android.R.id.empty);
        listView.setEmptyView(emptyTextView);
    }

    private class RodadaTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "rodada/1";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RodadaActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jsonParser.getJSONFromUrl(mUrl);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            RodadaService rodadaService = new RodadaService();
            mTodasRodadas = (ArrayList<Rodada>)rodadaService.getRodada(json);

            ArrayList<Rodada> rodadas = new ArrayList<Rodada>();

            for (Rodada rodada : mTodasRodadas) {
                if (rodada.isFinalizada()) {
                    continue;
                }
                rodadas.add(rodada);
            }

            ArrayAdapter<Rodada> arrayAdapter = new ArrayAdapter<Rodada>(RodadaActivity.this, android.R.layout.simple_list_item_1, rodadas);

            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    mRodada = (Rodada) parent.getAdapter().getItem(position);
                    String url_ = getResources().getString(R.string.default_url) + "configuracao/";
                    new PodeApostarTaskAsync().execute(url_ + mRodada.getId());
                }
            });

            progressDialog.dismiss();
        }
    }

    private class PodeApostarTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RodadaActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
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
            podeApostar = 0;
            try {
                // Getting JSON Array
                JSONObject jsonObject = json.getJSONObject("response");
                podeApostar = jsonObject.getInt("pode_apostar");
                if (podeApostar > 0) {
                    //String _url = getResources().getString(R.string.default_url) + "sequence";
                    //new SequenceTaskAsync().execute(_url);

                    try	{
                        Intent intent = new Intent(RodadaActivity.this, ApostaActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.putExtra("USUARIO_ID", mUser.getId());
                        //intent.putExtra("APOSTA_ID", aposta_nextValue);
                        intent.putExtra("CAMPEONATO", campeonato);
                        intent.putExtra("RODADA", mRodada);
                        startActivity(intent);
                    } catch(Exception e) {
                        //e.printStackTrace();
                    }

                } else {
                    Toast toast = Toast.makeText(RodadaActivity.this, "As Apostas para rodada selecionada foram encerradas.", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    private class SequenceTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RodadaActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
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
                    Intent intent = new Intent(RodadaActivity.this, ApostaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("USUARIO_ID", mUser.getId());
                    intent.putExtra("APOSTA_ID", aposta_nextValue);
                    intent.putExtra("CAMPEONATO", campeonato);
                    intent.putExtra("RODADA", mRodada);
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