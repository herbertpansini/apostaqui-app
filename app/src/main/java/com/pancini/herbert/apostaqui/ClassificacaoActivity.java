package com.pancini.herbert.apostaqui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.pancini.herbert.adapters.TabelaAdapter;
import com.pancini.herbert.entities.Classificacao;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.services.ClassificacaoService;
import org.json.JSONObject;
import java.util.ArrayList;

public class ClassificacaoActivity extends AppCompatActivity {
    ListView listView;
    TextView emptyTextView;
    private Rodada mRodada;

    ArrayList<Classificacao> mTabela;
    private TabelaAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classificacao);

        // get Intents
        this.getIntents();

        // find View By Id
        this.findViewById();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTabela);
        toolbar.setTitle(this.mRodada.toString());
        setSupportActionBar(toolbar);

        // execute async task
        new MyTaskAsync().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.find_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
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

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "tabela/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ClassificacaoActivity.this);
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
            ClassificacaoService classificacaoService = new ClassificacaoService();
            mTabela = (ArrayList<Classificacao>)classificacaoService.getClassificacao(json);

            mAdapter = new TabelaAdapter(ClassificacaoActivity.this, mTabela);
            listView.setAdapter(mAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    Classificacao classificacao = (Classificacao) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(ClassificacaoActivity.this, JogosUsuarioActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("RODADA_ID", classificacao.getRodadaId());
                    intent.putExtra("USUARIO_ID", classificacao.getUsuarioId());
                    intent.putExtra("USUARIO_NOME", classificacao.getNome());
                    startActivity(intent);
                }
            });
            progressDialog.dismiss();
        }
    }
}