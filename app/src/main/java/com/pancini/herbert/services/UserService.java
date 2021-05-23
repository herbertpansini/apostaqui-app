package com.pancini.herbert.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.pancini.herbert.entities.User;

public class UserService {
    private final String TAG_USER = "usuario";
    private final String TAG_RESPONSE = "response";
    private final String TAG_ID = "usuario_id";
    private final String TAG_NOME = "nome";
    private final String TAG_TELEFONE = "telefone";
    private final String TAG_EMAIL = "email";
    private final String TAG_SENHA = "senha";
    private final String TAG_ROLE_ID = "perfil_id";

    public User getUser(JSONObject jsonObject) {
        User user = new User();
        try {
            // Getting JSON Object
            user.setId(jsonObject.getInt(TAG_ID));
            user.setName(jsonObject.getString(TAG_NOME));
            user.setPhoneNumber(jsonObject.getString(TAG_TELEFONE));
            user.setEmail(jsonObject.getString(TAG_EMAIL));
            user.setPassword(jsonObject.getString(TAG_SENHA));
            user.setRoleId(jsonObject.getInt(TAG_ROLE_ID));
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return user;
    }

    public List<User> getUsers(JSONObject jsonObject) {
        ArrayList<User> users = new ArrayList<User>();
        try {
            // Getting JSON Array
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                User user = new User();
                user.setId(jObj.getInt(TAG_ID));
                user.setName(jObj.getString(TAG_NOME));
                user.setPhoneNumber(jObj.getString(TAG_TELEFONE));
                user.setEmail(jObj.getString(TAG_EMAIL));
                user.setPassword(jObj.getString(TAG_SENHA));
                user.setRoleId(jObj.getInt(TAG_ROLE_ID));
                users.add(user);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return users;
    }
}