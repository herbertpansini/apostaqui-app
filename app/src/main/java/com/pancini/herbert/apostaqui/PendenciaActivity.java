package com.pancini.herbert.apostaqui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.pancini.herbert.adapters.PendenciaAdapter;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.entities.UsuarioAposta;
import com.pancini.herbert.persistence.UserDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PendenciaActivity extends AppCompatActivity {
    ArrayList<UsuarioAposta> mUsuarioApostas;
    private PendenciaAdapter mAdapter;

    ListView listView;
    TextView emptyTextView;
    private Rodada mRodada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendencia);

        // get Intents
        this.getIntents();

        // find View By Id
        this.findViewById();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPendencia);
        toolbar.setTitle(this.mRodada.toString());
        setSupportActionBar(toolbar);

        new MyTaskAsync().execute();
    }

    // get Intents
    private void getIntents() {
        Intent intent = getIntent();
        this.mRodada = (Rodada)intent.getSerializableExtra("RODADA");
    }

    // find View By Id
    private void findViewById() {
        listView = (ListView) findViewById(R.id.listView);
        emptyTextView = (TextView) findViewById(R.id.textEmpty);
        listView.setEmptyView(emptyTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.find_confirm_menu, menu);
        MenuItem item = menu.findItem(R.id.action_find);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_confirm:
                AlertDialog.Builder alert = new AlertDialog.Builder(PendenciaActivity.this);
                alert.setTitle("Confirma!");
                alert.setMessage("Você tem certeza que deseja salvar?");
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirm();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;

            default:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirm() {
        JSONArray jsonArray = new JSONArray();
        for(UsuarioAposta usuarioAposta : mUsuarioApostas) {
            if (usuarioAposta.isSelected()) {
                JSONObject jsonObject = new JSONObject();
                try	{
                    jsonObject.put("usuario_id", usuarioAposta.getUsuarioId());
                    jsonObject.put("usuario_id_adm", new UserDao(PendenciaActivity.this).get().getId());
                    jsonObject.put("rodada_id", usuarioAposta.getRodadaId());
                    jsonObject.put("valida", usuarioAposta.isValid());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
        }

        JSONObject jObject = new JSONObject();
        try {
            jObject.put("aposta", jsonArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }

        String _url = getResources().getString(R.string.default_url) + "validate";
        new ApostaTaskAsync().execute(_url, jObject.toString());

    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "pendencia/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PendenciaActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jsonParser.getJSONFromUrl(mUrl + mRodada.getId());
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json != null) {
                mUsuarioApostas = new ArrayList<UsuarioAposta>();
                try {
                    // Getting JSON Array
                    JSONArray jsonArray = json.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        UsuarioAposta obj = new UsuarioAposta();
                        obj.setUsuarioId(jObj.getInt("usuario_id"));
                        obj.setRodadaId(jObj.getInt("rodada_id"));
                        obj.setNome(jObj.getString("nome"));
                        obj.setApostas(jObj.getInt("apostas"));
                        obj.setSelected(false);

                        mUsuarioApostas.add(obj);
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                }

                mAdapter = new PendenciaAdapter(PendenciaActivity.this, mUsuarioApostas);
                listView.setAdapter(mAdapter);
            } else {
                Toast toast = Toast.makeText(PendenciaActivity.this, "Error!", Toast.LENGTH_LONG);
                toast.show();
            }
            progressDialog.dismiss();
        }
    }

    private class ApostaTaskAsync extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PendenciaActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            Integer json = jsonParser.putJSONFromUrlData(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(Integer json) {
            progressDialog.dismiss();
            if (json > 0) {
                try	{
                    new MyTaskAsync().execute();
                } catch(Exception e) {
                    //e.printStackTrace();
                }
            } else {
                Toast.makeText(PendenciaActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}