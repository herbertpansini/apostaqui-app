package com.pancini.herbert.apostaqui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.pancini.herbert.entities.Confronto;
import com.pancini.herbert.entities.Equipe;
import com.pancini.herbert.entities.Rodada;
import com.pancini.herbert.services.ConfrontoService;
import com.pancini.herbert.services.EquipeService;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConfrontoEditActivity extends Activity {
    //private TextView tvRodada;
    private Spinner spinnerEquipeMandante;
    private EditText etPlacarEquipeMandante;
    private EditText etPlacarEquipeVisitante;
    private Spinner spinnerEquipeVisitante;
    private EditText etHorarioConfronto;

    private Confronto confronto;
    private Equipe equipeMandante;
    private Equipe equipeVisitante;
    private Date horario;

    private ArrayList<Equipe> equipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confronto_edit);

        this.getIntents();

        this.findViewById();

        this.etHorarioConfronto.setOnClickListener(new View.OnClickListener() {
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
        new DatePickerDialog(ConfrontoEditActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(ConfrontoEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        this.confronto = (Confronto)intent.getSerializableExtra("CONFRONTO");
    }

    // Find View By Id
    private void findViewById() {
        //this.tvRodada = (TextView) findViewById(R.id.tvRodada);
        this.spinnerEquipeMandante = (Spinner) findViewById(R.id.spinnerEquipeMandante);
        this.etPlacarEquipeMandante = (EditText) findViewById(R.id.etPlacarEquipeMandante);
        this.etPlacarEquipeVisitante = (EditText) findViewById(R.id.etPlacarEquipeVisitante);
        this.spinnerEquipeVisitante = (Spinner) findViewById(R.id.spinnerEquipeVisitante);
        this.etHorarioConfronto = (EditText) findViewById(R.id.etHorarioConfronto);
    }

    private Equipe getEquipeMandante(int equipeMandanteId) {
        for (Equipe equipeMandante : this.equipes) {
            if (equipeMandante.getId() == equipeMandanteId) {
                return equipeMandante;
            }
        }
        return null;
    }

    private Equipe getEquipeVisitante(int equipeVisitanteId) {
        for (Equipe equipeVisitante : this.equipes) {
            if (equipeVisitante.getId() == equipeVisitanteId) {
                return equipeVisitante;
            }
        }
        return null;
    }

    private void setConfronto(Confronto confronto) {
        this.spinnerEquipeMandante.setSelection(((ArrayAdapter<Equipe>)this.spinnerEquipeMandante.getAdapter()).getPosition(this.getEquipeMandante(confronto.getEquipeMandante())));
        if (confronto.getPlacarEquipeMandante() != null) {
            this.etPlacarEquipeMandante.setText(confronto.getPlacarEquipeMandante().toString());
        }

        this.spinnerEquipeVisitante.setSelection(((ArrayAdapter<Equipe>)this.spinnerEquipeVisitante.getAdapter()).getPosition(this.getEquipeVisitante(confronto.getEquipeVisitante())));
        if (confronto.getPlacarEquipeVisitante() != null) {
            this.etPlacarEquipeVisitante.setText(confronto.getPlacarEquipeVisitante().toString());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // Set your date format
        String formattedDate = sdf.format(confronto.getHorario());
        this.horario = confronto.getHorario();
        this.etHorarioConfronto.setText(formattedDate);
    }

    //TODO: (Herbert)
    // Fazer verificação nos campos de placar tornando-os obrigatório
    public void send(View v) {
        Confronto confronto = new Confronto();
        confronto.setRodada(this.confronto.getRodada());
        confronto.setEquipeMandante(this.equipeMandante.getId());

        try {
            confronto.setPlacarEquipeMandante(Integer.valueOf(this.etPlacarEquipeMandante.getText().toString()));
        } catch(Exception e) {

        }

        try {
            confronto.setPlacarEquipeVisitante(Integer.valueOf(this.etPlacarEquipeVisitante.getText().toString()));
        } catch(Exception e) {

        }

        confronto.setEquipeVisitante(this.equipeVisitante.getId());
        confronto.setHorario(this.horario);
        ConfrontoService confrontoService = new ConfrontoService();
        JSONObject jsonObject = confrontoService.getJSONObject(confronto);

        String url = getResources().getString(R.string.default_url) + "confronto/";
        new MineTaskAsync().execute(url + this.confronto.getId(), jsonObject.toString());
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "equipe";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfrontoEditActivity.this);
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
            progressDialog.dismiss();

            EquipeService equipeService = new EquipeService();
            equipes = (ArrayList<Equipe>)equipeService.getEquipe(json);

            //fill data in spinner
            ArrayAdapter<Equipe> adapter = new ArrayAdapter<Equipe>(ConfrontoEditActivity.this, android.R.layout.simple_spinner_dropdown_item, equipes);
            spinnerEquipeMandante.setAdapter(adapter); // Equipe Mandante
            spinnerEquipeMandante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    equipeMandante = (Equipe) parent.getSelectedItem();
                    Toast.makeText(ConfrontoEditActivity.this, "Equipe ID: " + equipeMandante.getId() + ",  Nome da Equipe: " + equipeMandante.getNome(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ConfrontoEditActivity.this, "Equipe ID: " + equipeVisitante.getId() + ",  Nome da Equipe: " + equipeVisitante.getNome(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            setConfronto(confronto);
        }
    }

    private class MineTaskAsync extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfrontoEditActivity.this);
            progressDialog.setMessage("Saving Data of Fighting...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            Integer json = jsonParser.putJSONFromUrlData(args[0], args[1]);
            return json;
        }

        @Override
        protected void onPostExecute(Integer json) {
            progressDialog.dismiss();
            if (json > 0) {
                try	{
                    Intent intent = new Intent(ConfrontoEditActivity.this, ConfrontoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Rodada rodada = new Rodada();
                    rodada.setId(confronto.getRodada());
                    intent.putExtra("RODADA", rodada);
                    startActivity(intent);
                } catch(Exception e) {

                }
            } else {
                Toast toast = Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}