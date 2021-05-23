package com.pancini.herbert.apostaqui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.services.RodadaService;
import org.json.JSONObject;

public class RodadaEditActivity extends AppCompatActivity {
    private Rodada mRodada;

    private EditText year;
    private EditText number;
    private CheckBox discontinued;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rodada_edit);

        // get Intents
        this.getIntents();

        // find View By Id
        this.findViewById();

        this.Initialize();
    }

    // get Intents
    private void getIntents() {
        Intent intent = getIntent();
        this.mRodada = (Rodada) intent.getSerializableExtra("RODADA");
    }

    // find View By Id
    private void findViewById() {
        this.year = (EditText) findViewById(R.id.year);
        this.number = (EditText) findViewById(R.id.number);
        this.discontinued = (CheckBox) findViewById(R.id.discontinued);
    }

    private void Initialize() {
        this.year.setText(String.valueOf(this.mRodada.getAno()));
        this.number.setText(String.valueOf(this.mRodada.getNumero()));
        this.discontinued.setChecked(this.mRodada.isAtiva());
    }

    public void cancel(View v) {
        try {
            Intent intent = new Intent(RodadaEditActivity.this, ConfrontoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("RODADA", mRodada);
            startActivity(intent);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void save(View v) {
        Rodada rodada = this.mRodada;
        if (this.discontinued.isChecked()) {
            rodada.setAtiva(1);
        } else {
            rodada.setAtiva(0);
        }

        RodadaService rodadaService = new RodadaService();
        JSONObject jsonObject = rodadaService.getJSONObject(rodada);

        String url = getResources().getString(R.string.default_url) + "rodada/";
        new RodadaTaskAsync().execute(url + rodada.getId(), jsonObject.toString());
    }

    private class RodadaTaskAsync extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RodadaEditActivity.this);
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
                    Intent intent = new Intent(RodadaEditActivity.this, ConfrontoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("RODADA", mRodada);
                    startActivity(intent);
                } catch(Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }
                Toast.makeText(RodadaEditActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RodadaEditActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
