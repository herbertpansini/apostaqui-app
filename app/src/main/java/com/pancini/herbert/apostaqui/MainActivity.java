package com.pancini.herbert.apostaqui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pancini.herbert.entities.ReportHeader;
import com.pancini.herbert.entities.User;
import com.pancini.herbert.persistence.UserDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textView;
    Toolbar toolbar;
    private int tabIndex;
    private User mUser;
    private static final int ADMINISTRADOR = 1;
    private ReportHeader mReport;

    // get Intents
    private void getIntents() {
        Intent intent = getIntent();
        try {
            this.tabIndex = (int)intent.getSerializableExtra("TAB_INDEX");
        } catch (Exception e) {
            this.tabIndex = 0;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            displaySelectedScreen(id);
            return true;
        }
    };

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.navigation_home :
                fragment = new CampeonatoFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Futebol");
                break;
            case R.id.navigation_history :
                fragment = new HistoricoFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Selecione uma Rodada");
                break;
            case R.id.navigation_dashboard :
                fragment = new MeusJogosFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Minhas Apostas");
                break;
            case R.id.navigation_notifications :
                fragment = new ClassificacaoFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Classificação");
                break;
            case R.id.nav_equipes :
                fragment = new EquipeFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Equipes");
                break;
            case R.id.nav_report :
                fragment = new RelatorioFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Relatório");
                break;
            case R.id.nav_Rodadas :
                fragment = new RodadaFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Rodadas");
                break;
            case R.id.nav_Confirmados :
                fragment = new ConfirmacaoFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Confirmadas");
                break;
            case R.id.nav_Pendente_Confirmacao :
                fragment = new PendenteConfirmacaoFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Não Confirmadas");
                break;
            case R.id.nav_Pendentes :
                fragment = new PendenciaFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Seleciona uma Rodada");
                break;
            case R.id.nav_Validated :
                fragment = new ValidatedFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Validados");
                break;
            case R.id.nav_share :
                fragment = new UsuarioFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Usuários");
                break;
            case R.id.nav_send :
                fragment = new ConfiguracaoFragment();
                ((AppCompatActivity)MainActivity.this).getSupportActionBar().setTitle("Configurações");
                break;
            case R.id.nav_off :
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Confirma!");
                alert.setMessage("Você tem certeza que deseja fazer logoff?");
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOff();
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

        if (fragment != null) {
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }
    }

    private void logOff() {
        new UserDao(MainActivity.this).delete(mUser.getId());
        closeApplication();
    }

    private void closeApplication() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // get Intents
        this.getIntents();

        if (tabIndex == 1) {
            this.displaySelectedScreen(R.id.navigation_dashboard);
        } else {
            this.displaySelectedScreen(R.id.navigation_home);
        }

        //getActivity().setTitle("your title");

        mUser = new UserDao(MainActivity.this).get();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        if (this.mUser != null) {
            TextView tvName = (TextView) headerView.findViewById(R.id.tvName);
            tvName.setText(mUser.getName());

            TextView tvEmail = (TextView) headerView.findViewById(R.id.tvEmail);
            tvEmail.setText(mUser.getEmail());
        }

        new RelatorioTaskAsync().execute();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Confirma!");
        alert.setMessage("Você deseja encerrar a aplicação?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeApplication();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.main, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == R.id.nav_Pendentes) {
            if (mReport.getPodeGerar() > 0) {
                Toast.makeText(MainActivity.this, "Não é mais possível validar apostas.\nRodada já iniciada.", Toast.LENGTH_LONG).show();
            } else {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle(R.string.title_password);
                alertDialogBuilder.setMessage(R.string.message_password);
                final EditText input = new EditText(this);
                alertDialogBuilder.setIcon(R.drawable.key);
                alertDialogBuilder.setView(input);
                alertDialogBuilder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String senha = input.getText().toString();
                        if (senha.equals("130488")) {
                            displaySelectedScreen(id);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.access_denied, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.show();
            }
        } else if (mUser.getRoleId() == ADMINISTRADOR || id == R.id.nav_report || id == R.id.nav_Confirmados || id == R.id.nav_Pendente_Confirmacao || id == R.id.nav_off) {
            displaySelectedScreen(id);
        } else {
            Toast.makeText(MainActivity.this, R.string.access_denied, Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class RelatorioTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "relatorio/1";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
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
}