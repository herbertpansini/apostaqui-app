package com.pancini.herbert.apostaqui;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pancini.herbert.entities.Configuracao;
import com.pancini.herbert.services.ConfiguracaoService;
import org.json.JSONObject;

public class ConfiguracaoFragment extends Fragment {
    private Configuracao mConfig = null;
    private Context mContext;

    private EditText hour;
    private Button save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        new ConfiguracaoTaskAsync().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_configuracao, container, false);
        this.hour = (EditText) layout.findViewById(R.id.hour);
        this.save = (Button)layout.findViewById(R.id.btn_salvar);

        this.save.setOnClickListener(mSaveListener);

        return layout;
    }

    private View.OnClickListener mSaveListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked to validate the input
            save();
        }
    };

    private void initialize() {
        this.hour.setText(String.valueOf(this.mConfig.getQuantidadeHoras()));
    }

    private void save() {
        // URL to get JSON Array
        Configuracao config = mConfig;
        config.setQuantidadeHoras(Integer.parseInt(this.hour.getText().toString()));
        String json = new ConfiguracaoService().getJSONObject(config).toString();
        String url = getResources().getString(R.string.default_url) + "configuracao/1";
        new SettingUpTaskAsync().execute(url, json);
    }

    private class ConfiguracaoTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "configuracao";
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
            mConfig = new ConfiguracaoService().getConfiguracao(json);
            initialize();
            progressDialog.dismiss();
        }
    }

    private class SettingUpTaskAsync extends AsyncTask<String, String, Integer> {
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
        protected Integer doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            Integer json = jsonParser.postJSONFromUrlData(args[0], args[1]);
            return json;
        }

        @Override
        protected void onPostExecute(Integer json) {
            if (json > 0) {
                Toast.makeText(mContext, "Success!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }
}
