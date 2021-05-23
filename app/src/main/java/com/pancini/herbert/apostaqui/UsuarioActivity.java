package com.pancini.herbert.apostaqui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.pancini.herbert.entities.Role;
import com.pancini.herbert.entities.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class UsuarioActivity extends Activity {
    private User mUser;
    private ArrayList<Role> mRoles;
    private Role mRole;

    private EditText name;
    private EditText phoneNumber;
    private EditText email;
    private Spinner role;

    private Button bet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        // get Intents
        this.getIntents();

        // find View by Id
        this.findViewById();

        // set User
        //this.SetUser(mUser);

        //
        new ProfileTaskAsync().execute();
    }

    // get Intents
    private void getIntents() {
        Intent intent = getIntent();
        this.mUser = (User)intent.getSerializableExtra("USUARIO");
    }

    // find View By Id
    private void findViewById() {
        this.name = (EditText) findViewById(R.id.name);
        this.phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        this.email = (EditText) findViewById(R.id.email);
        this.role = (Spinner) findViewById(R.id.role);
        this.bet = (Button) findViewById(R.id.bet);
        this.bet.setOnClickListener(mBetListener);
    }

    private View.OnClickListener mBetListener = new View.OnClickListener() {
        public void onClick(View v) {
        // do something when the button is clicked to validate the input
        betsOfUser();
        }
    };

    private void betsOfUser() {
        try {
            Intent intent = new Intent(UsuarioActivity.this, ApostasDoUsuarioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("USUARIO", mUser);
            startActivity(intent);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private void setUser(User user) {
        this.name.setText(user.getName());
        this.phoneNumber.setText(user.getPhoneNumber());
        this.email.setText(user.getEmail());
        this.role.setSelection(((ArrayAdapter<Role>)this.role.getAdapter()).getPosition(this.getProfile(user.getRoleId())));
    }

    private Role getProfile(int roleId) {
        for (Role profile : this.mRoles) {
            if (profile.getId() == roleId) {
                return profile;
            }
        }
        return null;
    }

    public void cancel(View v) {
        try {
            Intent intent = new Intent(UsuarioActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra("USUARIO", mUser);
            startActivity(intent);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void save(View v) {
        User user = this.mUser;
        user.setRoleId(this.mRole.getId());

        //Create JSONObject here
        JSONObject jObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try	{
            jsonObject.put("nome", user.getName());
            jsonObject.put("telefone", user.getPhoneNumber());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("senha", user.getPassword());
            jsonObject.put("perfil_id", user.getRoleId());
            jObject.put("usuario", jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }

        String url = getResources().getString(R.string.default_url) + "usuario/";
        new UserTaskAsync().execute(url + this.mUser.getId(), jObject.toString());
    }

    private class ProfileTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "perfil/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UsuarioActivity.this);
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
            mRoles = new ArrayList<Role>();
            try {
                // Getting JSON Array
                JSONArray jsonArray = json.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    Role profile = new Role();
                    profile.setId(jObj.getInt("perfil_id"));
                    profile.setName(jObj.getString("descricao"));
                    mRoles.add(profile);
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }

            //fill data in spinner
            ArrayAdapter<Role> adapter = new ArrayAdapter<Role>(UsuarioActivity.this, android.R.layout.simple_spinner_dropdown_item, mRoles);
            role.setAdapter(adapter);
            role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mRole = (Role) parent.getSelectedItem();
                    Toast.makeText(UsuarioActivity.this, "Role ID: " + mRole.getId() + ",  Role Name: " + mRole.getName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            setUser(mUser); // set User

            progressDialog.dismiss();
        }
    }

    private class UserTaskAsync extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UsuarioActivity.this);
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
                    Intent intent = new Intent(UsuarioActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch(Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }
                Toast.makeText(UsuarioActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UsuarioActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
