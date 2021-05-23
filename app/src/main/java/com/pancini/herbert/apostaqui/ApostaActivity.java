package com.pancini.herbert.apostaqui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.pancini.herbert.adapters.ApostaAdapter;
import com.pancini.herbert.entities.Aposta;
import com.pancini.herbert.entities.Campeonato;
import com.pancini.herbert.entities.Confronto;
import com.pancini.herbert.entities.ItemAposta;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.services.ApostaService;
import com.pancini.herbert.services.ConfrontoService;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class ApostaActivity extends AppCompatActivity {
    private Rodada mRodada;
    private Campeonato campeonato;
    private int usuarioId;
    //private int apostaId;
    private ArrayList<Confronto> mConfrontos;

    private final int tabIndex = 1;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aposta);

        // get Intents
        this.getIntents();

        // find View By Id
        this.findViewById();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAposta);
        toolbar.setTitle(mRodada.toString());
        setSupportActionBar(toolbar);

        // load data
        new MyTaskAsync().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            // do something when the button is clicked to validate the input
            AlertDialog.Builder alert = new AlertDialog.Builder(ApostaActivity.this);
            alert.setTitle("Confirma!");
            alert.setMessage("Você tem certeza que deseja salvar?");
            alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    save();
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        boolean flag = true;
        int count = this.lv.getCount();
        //ItemApostaService itemApostaService = new ItemApostaService();
        ArrayList<ItemAposta> itensAposta = new ArrayList<ItemAposta>();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < count; i++) {
            ItemAposta itemAposta = (ItemAposta) this.lv.getItemAtPosition(i);
            if (itemAposta.getCurrent() == -1) {
                flag = false;
                Toast.makeText(getBaseContext(), "Você não selecionou o jogo: " + itemAposta.getConfronto().getNomeEquipeMandante() + " X " + itemAposta.getConfronto().getNomeEquipeVisitante(), Toast.LENGTH_LONG).show();
                break;
            }
            //jsonArray.put( itemApostaService.getJSONObject(itemAposta) );
            itensAposta.add(itemAposta);
        }

        if (flag) {
            Aposta aposta = new Aposta();
            //aposta.setId(this.apostaId);
            aposta.setRodadaId(mConfrontos.get(0).getRodada());
            aposta.setUsuarioId(this.usuarioId);
            JSONObject jsonObject = new ApostaService().getJSONObject(aposta, itensAposta);
            String _url = getResources().getString(R.string.default_url) + "aposta";
            new ApostaTaskAsync().execute(_url, jsonObject.toString());

            /*String url_ = getResources().getString(R.string.default_url) + "jogo";

            //JSONObject jObject = new JSONObject();
            try {
                //jObject.put("itens_aposta", jsonArray);
                jsonObject.put("itens_aposta", jsonArray);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new ItemApostaTaskAsync().execute(url_, jObject.toString());*/
        }
    }

    // get Intents
    private void getIntents() {
        Intent intent = getIntent();
        this.mRodada = (Rodada)intent.getSerializableExtra("RODADA");
        this.campeonato = (Campeonato)intent.getSerializableExtra("CAMPEONATO");
        //this.apostaId = (int)intent.getSerializableExtra("APOSTA_ID");
        this.usuarioId = (int)intent.getSerializableExtra("USUARIO_ID");
    }

    // find View By Id
    private void findViewById() {
        this.lv = (ListView)findViewById(R.id.listViewAposta);
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "confronto/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ApostaActivity.this);
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
            ConfrontoService confrontoService = new ConfrontoService();
            mConfrontos = (ArrayList<Confronto>)confrontoService.getConfronto(json);
            ArrayList<ItemAposta> itemsAposta = new ArrayList<ItemAposta>();
            if (mConfrontos != null) {
                for (Confronto confronto : mConfrontos) {
                    ItemAposta itemAposta = new ItemAposta();
                    //itemAposta.setApostaId(apostaId);
                    itemAposta.setConfrontoId(confronto.getId());
                    itemAposta.setConfronto(confronto);
                    itemsAposta.add(itemAposta);
                }
            }
            lv.setAdapter(new ApostaAdapter(getBaseContext(), itemsAposta));

            progressDialog.dismiss();
        }
    }

    private class ApostaTaskAsync extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ApostaActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            Integer json = jsonParser.postJSONFromUrlData(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(Integer json) {
            progressDialog.dismiss();
            if (json > 0) {
                try {
                    Intent intent = new Intent(ApostaActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("TAB_INDEX", tabIndex);
                    startActivity(intent);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                Toast.makeText(getBaseContext(), "Success!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ItemApostaTaskAsync extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ApostaActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            Integer json = jsonParser.postJSONFromUrlData(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(Integer json) {
            progressDialog.dismiss();
            if (json > 0) {
                try {
                    Intent intent = new Intent(ApostaActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("TAB_INDEX", tabIndex);
                    startActivity(intent);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                //Toast.makeText(getBaseContext(), "Success!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
