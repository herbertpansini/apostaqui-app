package com.pancini.herbert.apostaqui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.pancini.herbert.entities.User;
import com.pancini.herbert.persistence.UserDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private User mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mUser = new UserDao(LoginActivity.this).get();
        if (mUser != null)
        {
            new UserLoginTask(mUser.getEmail(), mUser.getPassword()).execute();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email, getMessageDigest(password));
            mAuthTask.execute();
        }
    }

    private String getMessageDigest(String password) {
        if (password.isEmpty()) {
            return "";
        } else {
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
                    String hex = Integer.toHexString(0xff & byteData[i]);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                //e.printStackTrace();
                return "";
            }
        }
    }

    private boolean isEmailValid(String email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public void register(View v) {
        try {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private JSONObject getJSONObject(String email, String password) {
        //Create JSONObject here
        JSONObject jObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try	{
            jsonObject.put("email", email);
            jsonObject.put("senha", password);
            jObject.put("login", jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return jObject;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, String, User> {
        // URL to get JSON Array
        private final String mUrl = getResources().getString(R.string.default_url) + "login";
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected User doInBackground(String... params) {
            JSONObject jsonObject = getJSONObject(mEmail, mPassword);

            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject jObject = jsonParser.postJSONFromUrlDataObject(mUrl, jsonObject.toString());

            User user = null;
            if (jObject != null) {
                try {
                    user = new User();
                    // Getting JSON Array
                    JSONArray jsonArray = jObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Getting JSON Object
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        user.setId(jObj.getInt("usuario_id"));
                        user.setName(jObj.getString("nome"));
                        user.setPhoneNumber(jObj.getString("telefone"));
                        user.setEmail(jObj.getString("email"));
                        user.setPassword(jObj.getString("senha"));
                        user.setRoleId(jObj.getInt("perfil_id"));
                    }
                } catch (JSONException e) {
                    user = null;
                    //e.printStackTrace();
                }
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            mAuthTask = null;
            showProgress(false);

            //if (success) {
            if (user != null) {
                //finish();
                UserDao userDao = new UserDao(LoginActivity.this);
                try {
                    User _user = userDao.get();
                    if (_user == null) {
                        userDao.insert(user);
                    } else {
                        userDao.update(user);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_invalid_login));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

