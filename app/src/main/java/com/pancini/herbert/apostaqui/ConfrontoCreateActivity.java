package com.pancini.herbert.apostaqui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONObject;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pancini.herbert.entities.Confronto;
import com.pancini.herbert.entities.Equipe;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.services.ConfrontoService;
import com.pancini.herbert.services.EquipeService;

public class ConfrontoCreateActivity extends Activity {
    private TextView tvRodada;
    private Spinner spinnerEquipeMandante;
    private Spinner spinnerEquipeVisitante;
    private EditText etHorarioConfronto;

    private Rodada rodada;
    private Equipe equipeMandante;
    private Equipe equipeVisitante;
    private Date horario;

    private ArrayList<Equipe> equipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confronto_create);

        this.getIntents();
        this.findViewById();

        this.tvRodada.setText(rodada.getNumero() + "ª Rodada");

        this.etHorarioConfronto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        new MyTaskAsync().execute();
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        new DatePickerDialog(ConfrontoCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(ConfrontoCreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        date.set(Calendar.SECOND, 0);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // Set your date format
                        String formattedDate = sdf.format(date.getTime());
                        etHorarioConfronto.setText(formattedDate);
                        horario = date.getTime();

                        Log.v("TAG", "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    // Get Intents
    private void getIntents() {
        Intent intent = getIntent();
        this.rodada = (Rodada)intent.getSerializableExtra("RODADA");
    }

    // Find View By Id
    private void findViewById() {
        this.tvRodada = (TextView) findViewById(R.id.tvRodada);
        this.spinnerEquipeMandante = (Spinner) findViewById(R.id.spinnerEquipeMandante);
        this.spinnerEquipeVisitante = (Spinner) findViewById(R.id.spinnerEquipeVisitante);
        this.etHorarioConfronto = (EditText) findViewById(R.id.etHorarioConfronto);
    }

    public void send(View v) {
        Confronto confronto = new Confronto();
        confronto.setRodada(this.rodada.getId());
        confronto.setEquipeMandante(this.equipeMandante.getId());
        confronto.setEquipeVisitante(this.equipeVisitante.getId());
        confronto.setHorario(this.horario);
        ConfrontoService confrontoService = new ConfrontoService();
        JSONObject jsonObject = confrontoService.getJSONObject(confronto);

        String url = getResources().getString(R.string.default_url) + "confronto";
        new MineTaskAsync().execute(url, jsonObject.toString());
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "equipe";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfrontoCreateActivity.this);
            progressDialog.setMessage("Loding Data Screen...");
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
            equipes = (ArrayList<Equipe>)equipeService.getEquipe(json);

            //fill data in spinner
            ArrayAdapter<Equipe> adapter = new ArrayAdapter<Equipe>(ConfrontoCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, equipes);
            spinnerEquipeMandante.setAdapter(adapter); // Equipe Mandante
            spinnerEquipeMandante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    equipeMandante = (Equipe) parent.getSelectedItem();
                    Toast.makeText(ConfrontoCreateActivity.this, "Equipe ID: " + equipeMandante.getId() + ",  Nome da Equipe: " + equipeMandante.getNome(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            spinnerEquipeVisitante.setAdapter(adapter); // Equipe Visitante
            spinnerEquipeVisitante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    equipeVisitante = (Equipe) parent.getSelectedItem();
                    Toast.makeText(ConfrontoCreateActivity.this, "Equipe ID: " + equipeVisitante.getId() + ",  Nome da Equipe: " + equipeVisitante.getNome(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            progressDialog.dismiss();
        }
    }

    private class MineTaskAsync extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfrontoCreateActivity.this);
            progressDialog.setMessage("Saving Data of Fighting...");
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
            progressDialog.dismiss();
            if (json > 0) {
                try	{
                    Intent intent = new Intent(ConfrontoCreateActivity.this, ConfrontoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("RODADA", rodada);
                    startActivity(intent);
                } catch(Exception e) {
                    //e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}