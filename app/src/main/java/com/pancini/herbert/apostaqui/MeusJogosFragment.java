package com.pancini.herbert.apostaqui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.pancini.herbert.adapters.MeusJogosAdapter;
import com.pancini.herbert.entities.Aposta;
import com.pancini.herbert.entities.Campeonato;
import com.pancini.herbert.entities.Jogos;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.entities.User;
import com.pancini.herbert.services.JogosService;
import com.pancini.herbert.persistence.UserDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MeusJogosFragment extends ListFragment implements MenuItem.OnActionExpandListener {
    ArrayList<Aposta> mTodasApostas;
    private MeusJogosAdapter mAdapter;
    private Context mContext;

    private int podeApostar = 0;

    private User mUser = null;

    private Campeonato campeonato = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        mUser = new UserDao(mContext).get();

        campeonato = new Campeonato(1, "Brasileirão Séria A", 1, "Futebol");

        String url_ = getResources().getString(R.string.default_url) + "configuracao/";
        new PodeApostarTaskAsync().execute(url_);

        new MineTaskAsync().execute();

        new MyTaskAsync().execute();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add :
                /*if (podeApostar > 0) {
                    String url_ = getResources().getString(R.string.default_url) + "sequence";
                    new SequenceTaskAsync().execute(url_);
                } else {
                    Toast toast = Toast.makeText(mContext, "As Apostas foram encerradas.", Toast.LENGTH_LONG);
                    toast.show();
                }*/

                try	{
                    Intent intent = new Intent(mContext, RodadaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } catch(Exception e) {
                    //e.printStackTrace();
                }

                break;
            case R.id.action_refresh :
                new MineTaskAsync().execute();
                new MyTaskAsync().execute();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }

    private class MineTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "classificacao/";
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
            progressDialog.dismiss();
        }
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "jogos/";
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
            JSONObject json = jsonParser.getJSONFromUrl(mUrl + mUser.getId());
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            JogosService jogosService = new JogosService(mTodasApostas);
            ArrayList<Jogos> jogos = (ArrayList<Jogos>)jogosService.getJogos(json);

            mAdapter = new MeusJogosAdapter(mContext, jogos);
            setListAdapter(mAdapter);

            progressDialog.dismiss();
        }
    }

    private class PodeApostarTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
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
            JSONObject json = jsonParser.getJSONFromUrl(args[0]);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            JSONParser jsonParser = new JSONParser();
            int aposta_nextValue = jsonParser.getResponse(json);

            if (aposta_nextValue > 0) {
                try	{
                    Intent intent = new Intent(mContext, ApostaActivity.class);
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