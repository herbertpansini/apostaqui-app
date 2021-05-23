package com.pancini.herbert.apostaqui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import com.pancini.herbert.entities.User;
import com.pancini.herbert.persistence.UserDao;
import com.pancini.herbert.services.UserService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etPhoneNumber, etEmail, etPassword, etConfirmPassword;
    private Button btSignUp;

    private final int APOSTADOR = 2;

    private User user;
    private ArrayList<User> mAllUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // find View By Id
        this.findViewById();

        new MyTaskAsync().execute();
    }

    // find View By Id
    private void findViewById() {
        this.etName = (EditText) findViewById(R.id.name);
        this.etPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        this.etEmail = (EditText) findViewById(R.id.email);
        this.etPassword = (EditText) findViewById(R.id.password);
        this.etConfirmPassword = (EditText) findViewById(R.id.confirmPassword);

        this.btSignUp = (Button) findViewById(R.id.signUp);
        this.btSignUp.setOnClickListener(mSignUpListener);
    }

    private View.OnClickListener mSignUpListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked to validate the input
            register();
        }
    };

    public void login(View v) {
        try {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private String getMessageDigest(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            try {
                md.update(password.getBytes("UTF-8"));
            } catch (java.io.UnsupportedEncodingException e) {
                //e.printStackTrace();
                return "";
            }
            byte byteData[] = md.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
            return "";
        }
    }

    private void initialize() {
        this.user = new User();
        this.user.setName(this.etName.getText().toString().trim());
        this.user.setPhoneNumber(this.etPhoneNumber.getText().toString().trim());
        this.user.setEmail(this.etEmail.getText().toString().trim());

        String pwd = this.etPassword.getText().toString().trim();
        if (!pwd.isEmpty()) {
            this.user.setPassword(this.getMessageDigest(pwd));
        } else {
            this.user.setPassword("");
        }

        String cpwd = this.etConfirmPassword.getText().toString().trim();
        if (!cpwd.isEmpty()) {
            this.user.setConfirmPassword(this.getMessageDigest(cpwd));
        } else {
            this.user.setConfirmPassword("");
        }
    }

    private void register() {
        this.initialize(); // initialize the input to string variables
        if (!this.validate()) {
            Toast.makeText(this, R.string.signup_failed, Toast.LENGTH_SHORT).show();
        } else {
            this.onSignUpSuccess();
        }
    }

    private boolean validate() {
        boolean valid = true;

        if (this.user.getName().length() < 3) {
            this.etName.setError(getResources().getText(R.string.error_incorrect_name));
            valid = false;
        }

        if (this.user.getName().isEmpty()) {
            this.etName.setError(getResources().getText(R.string.error_invalid_name));
            valid = false;
        }

        if (this.user.getPhoneNumber().isEmpty()) {
            this.etPhoneNumber.setError(getResources().getText(R.string.error_invalid_phone_number));
            valid = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(this.user.getEmail()).matches()) {
            this.etEmail.setError(getResources().getText(R.string.error_invalid_email));
            valid = false;
        }

        if (this.user.getPassword().length() < 6) {
            this.etPassword.setError(getResources().getText(R.string.error_less_password));
            valid = false;
        }

        if (this.user.getPassword().isEmpty()) {
            this.etPassword.setError(getResources().getText(R.string.error_invalid_password));
            valid = false;
        }

        if (this.user.getConfirmPassword().isEmpty() || !this.user.getConfirmPassword().equals(this.user.getPassword())) {
            this.etConfirmPassword.setError(getResources().getText(R.string.error_incorrect_confirm_password));
            valid = false;
        }

        // Verify if Phone Number n' Address Mail already exists
        if (userAlreadyExists()) {
            valid = false;
        }

        return valid;
    }

    private boolean userAlreadyExists() {
        boolean valid = false;
        for(User mUser : mAllUsers) {
            if (mUser.getPhoneNumber().equals(this.user.getPhoneNumber().trim())) {
                this.etPhoneNumber.setError(getResources().getText(R.string.error_duplicated_phone_number));
                valid = true;
                break;
            }

            if (mUser.getEmail().equalsIgnoreCase(this.user.getEmail().trim())) {
                this.etEmail.setError(getResources().getText(R.string.error_duplicated_address_mail));
                valid = true;
                break;
            }
        }
        return valid;
    }

    private JSONObject getJSONObject(User user) {
        //Create JSONObject here
        JSONObject jObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try	{
            jsonObject.put("nome", user.getName());
            jsonObject.put("telefone", user.getPhoneNumber());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("senha", user.getPassword());
            jsonObject.put("perfil_id", APOSTADOR);
            jObject.put("usuario", jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return jObject;
    }

    private void onSignUpSuccess() {
        // URL to get JSON Array
        String url = getResources().getString(R.string.default_url) + "usuario";
        String json = this.getJSONObject(this.user).toString();

        new UserRegisterTask().execute(url, json);
    }

    private class UserRegisterTask extends AsyncTask<String, String, Integer> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            Integer json = jsonParser.postJSONFromUrlData(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(Integer json) {
            progressDialog.dismiss();
            if (json > 0) {
                try	{
                    UserDao userDao = new UserDao(RegisterActivity.this);
                    user.setId(json);
                    userDao.insert(user);

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch(Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }
                Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "usuario";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
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
            UserService userService = new UserService();
            mAllUsers = (ArrayList<User>)userService.getUsers(json);
            progressDialog.dismiss();
        }
    }
}
