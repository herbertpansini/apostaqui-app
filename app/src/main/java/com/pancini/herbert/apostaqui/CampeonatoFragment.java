package com.pancini.herbert.apostaqui;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.pancini.herbert.adapters.JogoAdapter;
import com.pancini.herbert.entities.Jogo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CampeonatoFragment extends ListFragment {
    //ArrayList<Campeonato> mTodosCampeonatos;
    ArrayList<Jogo> confrontos;

    //private CampeonatoAdapter mAdapter;
    private JogoAdapter mAdapter;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        new CampeonatoTaskAsync().execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.search_fragment, container, false);
        ListView listView = (ListView) layout.findViewById(android.R.id.list);
        TextView emptyTextView = (TextView) layout.findViewById(android.R.id.empty);
        listView.setEmptyView(emptyTextView);
        return layout;
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh :
                new CampeonatoTaskAsync().execute();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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

    private class CampeonatoTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "confronto/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
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

            // TODO: (Herbert)
            if (confrontos.size() > 0) {
                confrontos.add(0, confrontos.get(0));
            }

            mAdapter = new JogoAdapter(mContext, confrontos);
            setListAdapter(mAdapter);

            progressDialog.dismiss();
        }
    }
}