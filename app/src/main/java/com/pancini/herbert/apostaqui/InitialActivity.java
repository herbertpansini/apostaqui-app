package com.pancini.herbert.apostaqui;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pancini.herbert.entities.User;
import com.pancini.herbert.persistence.UserDao;

public class InitialActivity extends AppCompatActivity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        mUser = new UserDao(InitialActivity.this).get();
        new UserLoginTask(mUser).execute();
    }

    public class UserLoginTask extends AsyncTask<String, String, User> {
        private final User _user;

        UserLoginTask(User user) {
            _user = user;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected User doInBackground(String... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                Log.e("InitialActivity", "doInBackground: ", e);
            }
            return _user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Intent intent = new Intent(InitialActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
