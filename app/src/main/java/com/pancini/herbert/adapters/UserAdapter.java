package com.pancini.herbert.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.apostaqui.UsuarioActivity;
import com.pancini.herbert.entities.User;

public class UserAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final ArrayList<User> users;

    private TextView tvNomeUsuario;

    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.user_item, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 2. Get rowView from inflater
        View rowView = null;
        rowView = inflater.inflate(R.layout.user_item, parent, false);

        tvNomeUsuario = (TextView)rowView.findViewById(R.id.nome_usuario);

        final User user = (User)users.get(position);
        tvNomeUsuario.setText(user.getName());

        // Set OnClickListener
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, UsuarioActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("USUARIO", user);
                    context.startActivity(intent);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        });

        return rowView;
    }
}