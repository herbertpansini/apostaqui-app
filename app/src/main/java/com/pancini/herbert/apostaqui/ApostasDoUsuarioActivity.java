package com.pancini.herbert.apostaqui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pancini.herbert.entities.Aposta;
import com.pancini.herbert.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApostasDoUsuarioActivity extends AppCompatActivity {
    ArrayList<Aposta> mTodasApostas;
    private User mUser;
    private int mApostaId;

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apostas_do_usuario);

        // Get Intents
        this.getIntents();

        // find View By Id
        this.findViewById();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarApostasDoUsuario);
        toolbar.setTitle(mUser.getName());
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        new BetTaskAsync().execute();
    }

    // Get Intents
    private void getIntents() {
        Intent intent = getIntent();
        this.mUser = (User)intent.getSerializableExtra("USUARIO");
    }

    // find View By Id
    private void findViewById() {
        lv = (ListView) findViewById(R.id.listViewApostas);
    }

    private ArrayList<String> getArrayString(ArrayList<Aposta> apostas) {
        ArrayList<String> arrStr = new ArrayList<String>();
        for (Aposta aposta : apostas) {
            arrStr.add(String.valueOf(aposta.getId()));
        }
        return arrStr;
    }

    private void delete() {
        String _url = getResources().getString(R.string.default_url) + "aposta/";
        new ApostaTaskAsync().execute(_url + mApostaId);
    }

    private class BetTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "classificacao/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ApostasDoUsuarioActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jsonParser.getJSONFromUrl(mUrl + mUser.getId());
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

            final ArrayAdapter adapter = new ArrayAdapter<String>(ApostasDoUsuarioActivity.this, android.R.layout.simple_list_item_1, getArrayString(mTodasApostas));
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                    AlertDialog.Builder adb=new AlertDialog.Builder(ApostasDoUsuarioActivity.this);
                    adb.setTitle("Excluir?");
                    adb.setMessage("Você tem certeza que deseja excluir?");
                    final int positionToRemove = position;
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mApostaId = Integer.parseInt(lv.getAdapter().getItem(positionToRemove).toString());
                            delete();
                            Object obj = lv.getAdapter().getItem(positionToRemove);
                            adapter.remove(obj);
                            adapter.notifyDataSetChanged();
                        }});
                    adb.show();
                }
            });

            progressDialog.dismiss();
        }
    }

    private class ItemApostaTaskAsync extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ApostasDoUsuarioActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            Integer json = jsonParser.deleteJSONFromUrl(args[0]);
            return json;
        }

        @Override
        protected void onPostExecute(Integer json) {
            if (json > 0) {
                Toast.makeText(ApostasDoUsuarioActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ApostasDoUsuarioActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
    }

    private class ApostaTaskAsync extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ApostasDoUsuarioActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            Integer json = jsonParser.deleteJSONFromUrl(args[0]);
            return json;
        }

        @Override
        protected void onPostExecute(Integer json) {
            if (json > 0) {
                String url_ = getResources().getString(R.string.default_url) + "jogo/";
                new ItemApostaTaskAsync().execute(url_ + mApostaId);
            } else {
                Toast.makeText(ApostasDoUsuarioActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
    }
}
