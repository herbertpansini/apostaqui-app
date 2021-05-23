package com.pancini.herbert.apostaqui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pancini.herbert.adapters.ConfrontoAdapter;
import com.pancini.herbert.entities.Confronto;
import com.pancini.herbert.entities.Rodada;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ConfrontoActivity extends AppCompatActivity {
    private TextView tvHeader;

    ConfrontoAdapter adapter;
    private ListView list;

    private Rodada rodada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confronto);

        this.getIntents();
        this.findViewById();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarConfronto);
        toolbar.setTitle(rodada.toString());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabConfronto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try	{
                    Intent intent = new Intent(ConfrontoActivity.this, ConfrontoCreateActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("RODADA", rodada);
                    startActivity(intent);
                } catch(Exception e) {
                    //e.printStackTrace();
                }
            }
        });

        new MyTaskAsync().execute();
    }

    // Get Intents
    private void getIntents() {
        Intent intent = getIntent();
        this.rodada = (Rodada)intent.getSerializableExtra("RODADA");
    }

    private void findViewById() {
        this.list = (ListView) findViewById(R.id.listViewConfrontos);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            try	{
                Intent intent = new Intent(ConfrontoActivity.this, RodadaEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("RODADA", this.rodada);
                startActivity(intent);
                return true;
            } catch(Exception e) {
                //
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "confronto/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfrontoActivity.this);
            progressDialog.setMessage("Getting Data of Fighting...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jsonParser.getJSONFromUrl(mUrl + rodada.getId());
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            ArrayList<Confronto> confrontos = new ArrayList<Confronto>();
            try {
                // Getting JSON Array
                JSONArray jsonArray = json.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    Confronto confronto = new Confronto();
                    confronto.setId(jObj.getInt("confronto_id"));
                    confronto.setRodada(jObj.getInt("rodada_id"));
                    confronto.setNumeroRodada(jObj.getInt("numero_rodada"));
                    confronto.setEquipeMandante(jObj.getInt("equipe_mandante"));
                    confronto.setNomeEquipeMandante(jObj.getString("nome_equipe_mandante"));
                    confronto.setSiglaEquipeMandante(jObj.getString("sigla_equipe_mandante"));
                    try	{
                        confronto.setPlacarEquipeMandante(jObj.getInt("placar_equipe_mandante"));
                    } catch (Exception e) {

                    }
                    try	{
                        confronto.setPlacarEquipeVisitante(jObj.getInt("placar_equipe_visitante"));
                    } catch (Exception e) {

                    }
                    confronto.setEquipeVisitante(jObj.getInt("equipe_visitante"));
                    confronto.setNomeEquipeVisitante(jObj.getString("nome_equipe_visitante"));
                    confronto.setSiglaEquipeVisitante(jObj.getString("sigla_equipe_visitante"));
                    confronto.setHorario(convertToDate(jObj.getString("horario")));
                    confrontos.add(confronto);
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }

            adapter = new ConfrontoAdapter(getBaseContext(), confrontos);
            list.setAdapter(adapter);

            //list.setAdapter(new ConfrontoAdapter(getBaseContext(), confrontos));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    Confronto confronto = (Confronto) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(ConfrontoActivity.this, ConfrontoEditActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("CONFRONTO", confronto);
                    startActivity(intent);
                }
            });

            progressDialog.dismiss();
        }
    }
}