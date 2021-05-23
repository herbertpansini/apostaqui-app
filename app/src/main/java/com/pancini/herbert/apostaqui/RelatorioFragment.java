package com.pancini.herbert.apostaqui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.pancini.herbert.adapters.ModelAdapter;
import com.pancini.herbert.entities.Model;
import com.pancini.herbert.entities.Relatorio;
import com.pancini.herbert.entities.ReportHeader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RelatorioFragment extends Fragment {
    private ModelAdapter mAdapter;
    private Context mContext;
    private static final String NOME_PASTA_APP = "com.pancini.herbert";
    private  static final String GERADOS = "MeusArquivos";
    private ReportHeader mReport;
    private ArrayList<Relatorio> mTodosRelatorios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        new RelatorioTaskAsync().execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.report_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        Button btnReport = (Button) view.findViewById(R.id.btn_report);
        btnReport.setOnClickListener(mReportListener);

    }

    private View.OnClickListener mReportListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked to validate the input
            report();
        }
    };

    public void report() {
        if (mReport.getPodeGerar() > 0) {
            new ReportTaskAsync().execute();
        } else {
            Toast.makeText(mContext, "Relatório só pode ser gerado após início da rodada.\nTente novamente mais tarde.", Toast.LENGTH_LONG).show();
        }
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }

    private void generatePdf() {
        String NOME_ARQUIVO = mReport.getNumeroRodada() + "_Rodada_Brasileirao_Serie_A" + ".pdf";
        String PASTA_SD = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(PASTA_SD + File.separator + NOME_PASTA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubDir = new File(pdfDir.getPath() + File.separator + GERADOS);

        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        String NOME_COMPLETO = Environment.getExternalStorageDirectory() + File.separator + NOME_PASTA_APP +
                File.separator + GERADOS + File.separator + NOME_ARQUIVO;

        File outputFile = new File(NOME_COMPLETO);
        if (outputFile.exists()) {
            outputFile.delete();
        }

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(NOME_COMPLETO));

            document.open();
            document.addAuthor("Herbert Pancini");
            document.addCreator("Apostaquí");
            document.addSubject("");
            document.addCreationDate();
            document.addTitle(mReport.getNumeroRodada() + "ª Rodada do Brasileirão Série A");

            XMLWorkerHelper xmlWorker = XMLWorkerHelper.getInstance();

            double valorAposta;
            final double porcetagem = 10;
            if (mReport.getDiaSemana() >= 3 && mReport.getDiaSemana() <= 5) {
                valorAposta = 10;
            } else {
                valorAposta = 5;
            }

            DecimalFormat df2 = new DecimalFormat("#,##0.00");

            double arrecadacao = mReport.getApostas() * valorAposta;
            double premiacao = mReport.getApostas() * (valorAposta - (valorAposta * porcetagem / 100));

            String htmlToPdf = "<html>" +
                                   "<head>" +
                                   "</head>" +
                                   "<body>" +
                                       "<h1>" +
                                            mReport.getNumeroRodada() + "ª Rodada do Brasileirão Série A" +
                                       "</h1>" +
                                       "<h2>" +
                                            "Apostas: " + mReport.getApostas() +
                                       "</h2>" +
                                       "<h2>" +
                                            "Arrecadação: R$ " + df2.format(arrecadacao) +
                                       "</h2>" +
                                       "<h2>" +
                                            "Prêmiação: R$ " + df2.format(premiacao) +
                                       "</h2>" +
                                       "<table border=\"1\">" +
                                            this.getReport() +
                                       "</table>" +
                                   "</body>" +
                               "</html>";
            try {
                xmlWorker.parseXHtml(pdfWriter, document, new StringReader(htmlToPdf));
                document.close();
                Toast.makeText(mContext, "PDF gerado com sucesso!", Toast.LENGTH_SHORT).show();
                showPdf(NOME_COMPLETO);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showPdf(String path) {
        Toast.makeText(mContext, "Lendo o arquivo", Toast.LENGTH_SHORT).show();
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "Você não tem um app para abrir esse tipo de dados", Toast.LENGTH_LONG).show();
        }
    }

    private String getReport() {
        StringBuilder sb = new StringBuilder();
        String nome = "", nomeDisplay = "";
        for (Relatorio rel : mTodosRelatorios) {
            if (!nome.equals(rel.getNome())) {
                nome = rel.getNome();
                nomeDisplay = rel.getNome();
            } else {
                nomeDisplay = "";
            }

            sb.append( "<tr>" +
                    "<td>" +
                    nomeDisplay +
                    "</td>" +
                    "<td>" +
                    rel.getSiglaEquipeMandante() +
                    "</td>" +
                    "<td>" +
                    rel.getMandante() +
                    "</td>" +
                    "<td>" +
                    rel.getEmpate() +
                    "</td>" +
                    "<td>" +
                    rel.getVisitante() +
                    "</td>" +
                    "<td>" +
                    rel.getSiglaEquipeVisitante() +
                    "</td>" +
                    "</tr>"
            );
        }
        return sb.toString();
    }

    private class RelatorioTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "relatorio/1";
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
            mReport = new ReportHeader();
            try {
                // Getting JSON Array
                JSONArray jsonArray = json.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    mReport.setPodeGerar(jObj.getInt("pode_gerar"));
                    mReport.setDiaSemana(jObj.getInt("dia_semana"));
                    mReport.setNumeroRodada(jObj.getInt("numero_rodada"));
                    mReport.setApostas(jObj.getInt("apostas"));
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    private class ReportTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "relatorio/";
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
            mTodosRelatorios = new ArrayList<Relatorio>();
            try {
                // Getting JSON Array
                JSONArray jsonArray = json.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    Relatorio relatorio = new Relatorio();
                    relatorio.setNome(jObj.getString("usuario_aposta"));
                    relatorio.setSiglaEquipeMandante(jObj.getString("sigla_equipe_mandante"));
                    relatorio.setMandante(jObj.getString("MANDANTE"));
                    relatorio.setEmpate(jObj.getString("EMPATE"));
                    relatorio.setVisitante(jObj.getString("VISITANTE"));
                    relatorio.setSiglaEquipeVisitante(jObj.getString("sigla_equipe_visitante"));
                    mTodosRelatorios.add(relatorio);
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            generatePdf();

            progressDialog.dismiss();
        }
    }
}