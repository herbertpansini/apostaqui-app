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
import com.pancini.herbert.adapters.ModelAdapter;
import com.pancini.herbert.entities.Model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ValidatedFragment extends ListFragment {

    ArrayList<Model> mModels;
    private ModelAdapter mAdapter;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        new ValidatedTaskAsync().execute();
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

    private class ValidatedTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "validate";

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
            if (json != null) {
                mModels = new ArrayList<Model>();
                try {
                    // Getting JSON Array
                    JSONArray jsonArray = json.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        Model obj = new Model();
                        obj.setId(jObj.getInt("usuario_id_adm"));
                        obj.setDescription(jObj.getString("nome"));
                        obj.setCounter(jObj.getInt("quantidade"));
                        mModels.add(obj);
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                }

                mAdapter = new ModelAdapter(mContext, mModels);
                setListAdapter(mAdapter);

                progressDialog.dismiss();
            }
        }
    }
}