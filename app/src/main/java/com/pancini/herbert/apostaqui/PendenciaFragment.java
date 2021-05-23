/*package com.pancini.herbert.apostaqui;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.pancini.herbert.adapters.PendenciaAdapter;
import com.pancini.herbert.adapters.UsuarioApostaAdapter;
import com.pancini.herbert.entities.UsuarioAposta;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PendenciaFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    //URL to get JSON Array
    private static final String url = getResources().getString(R.string.default_url) + "pendente/";

    ArrayList<UsuarioAposta> mUsuarioApostas;
    private PendenciaAdapter mAdapter;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        new MyTaskAsync().execute(url);
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {

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
        inflater.inflate(R.menu.find_confirm_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_find);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_confirm:
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
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
                    jsonObject.put("rodada_id", usuarioAposta.getRodadaId());
                    jsonObject.put("valida", usuarioAposta.isValid());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
        }

        JSONObject jObject = new JSONObject();
        try {
            jObject.put("aposta", jsonArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String _url = getResources().getString(R.string.default_url) + "aposta";
        new ApostaTaskAsync().execute(_url, jObject.toString());

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        ArrayList<UsuarioAposta> filteredValues = new ArrayList<UsuarioAposta>(mUsuarioApostas);
        for (UsuarioAposta value : mUsuarioApostas) {
            if (!value.getNome().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }

        mAdapter = new PendenciaAdapter(mContext, filteredValues);
        setListAdapter(mAdapter);

        return false;
    }

    public void resetSearch() {
        mAdapter = new PendenciaAdapter(mContext, mUsuarioApostas);
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
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
                    e.printStackTrace();
                }

                mAdapter = new PendenciaAdapter(mContext, mUsuarioApostas);
                setListAdapter(mAdapter);
            } else {
                Toast toast = Toast.makeText(mContext, "Error!", Toast.LENGTH_LONG);
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
            progressDialog = new ProgressDialog(mContext);
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
                    new MyTaskAsync().execute(url);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}*/

package com.pancini.herbert.apostaqui;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.pancini.herbert.adapters.MatchweekAdapter;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.services.RodadaService;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PendenciaFragment extends ListFragment {
    List<Rodada> mAllMatchweek;
    private ArrayAdapter<Rodada> mAdapter;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        new RodadaTaskAsync().execute();
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        /*String item = (String) listView.getAdapter().getItem(position);
        if (getActivity() instanceof SearchFragment.OnItem1SelectedListener) {
            ((SearchFragment.OnItem1SelectedListener) getActivity()).OnItem1SelectedListener(item);
        }
        getFragmentManager().popBackStack();*/
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

    private class RodadaTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "rodada/1";
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
            RodadaService rodadaService = new RodadaService();
            mAllMatchweek = (ArrayList<Rodada>)rodadaService.getRodada(json);

            ArrayList<Rodada> rodadas = new ArrayList<Rodada>();

            for (Rodada rodada : mAllMatchweek) {
                if (rodada.isFinalizada()) {
                    continue;
                }
                rodadas.add(rodada);
            }

            mAdapter = new MatchweekAdapter(mContext, rodadas);
            setListAdapter(mAdapter);

            progressDialog.dismiss();
        }
    }
}
