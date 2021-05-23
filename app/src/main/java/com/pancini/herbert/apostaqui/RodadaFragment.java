package com.pancini.herbert.apostaqui;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.pancini.herbert.adapters.RodadaAdapter;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.services.RodadaService;
import org.json.JSONObject;
import java.util.ArrayList;

public class RodadaFragment extends ListFragment {
    final String CAMPEONATO = "1";

    ArrayList<Rodada> mTodasRodadas;
    private RodadaAdapter mAdapter;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        new RodadaTaskAsync().execute();
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

    private class RodadaTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "rodada/";
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
            JSONObject json = jsonParser.getJSONFromUrl(mUrl + CAMPEONATO);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            RodadaService rodadaService = new RodadaService();
            mTodasRodadas = (ArrayList<Rodada>)rodadaService.getRodada(json);

            mAdapter = new RodadaAdapter(mContext, mTodasRodadas);
            setListAdapter(mAdapter);

            progressDialog.dismiss();
        }
    }
}