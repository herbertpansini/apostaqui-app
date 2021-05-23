package com.pancini.herbert.apostaqui;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.pancini.herbert.adapters.EquipeAdapter;
import com.pancini.herbert.entities.Equipe;
import com.pancini.herbert.services.EquipeService;
import org.json.JSONObject;
import java.util.List;

public class EquipeFragment extends ListFragment {


    List<Equipe> mTodasEquipes;
    private EquipeAdapter mAdapter;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        new EquipeTaskAsync().execute();
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

    private class EquipeTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "equipe";
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
            EquipeService equipeService = new EquipeService();
            mTodasEquipes = equipeService.getEquipe(json);

            mAdapter = new EquipeAdapter(mContext, mTodasEquipes);
            setListAdapter(mAdapter);

            progressDialog.dismiss();
        }
    }
}