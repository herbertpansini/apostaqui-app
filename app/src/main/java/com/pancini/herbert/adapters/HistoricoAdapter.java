package com.pancini.herbert.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pancini.herbert.apostaqui.ClassificacaoActivity;
import com.pancini.herbert.apostaqui.JogosUsuarioActivity;
import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.entities.Classificacao;
import com.pancini.herbert.entities.Rodada;

public class HistoricoAdapter extends ArrayAdapter<Rodada> {
    private final Context context;
    private final ArrayList<Rodada> rodadas;

    private TextView numero_rodada;

    public HistoricoAdapter(Context context, ArrayList<Rodada> rodadas) {
        super(context, R.layout.rodada_item, rodadas);
        this.context = context;
        this.rodadas = rodadas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 2. Get rowView from inflater
        View rowView = null;
        rowView = inflater.inflate(R.layout.rodada_item, parent, false);

        this.numero_rodada = (TextView)rowView.findViewById(R.id.numero_rodada);

        final Rodada objRodada = (Rodada) rodadas.get(position);
        this.numero_rodada.setText(objRodada.toString());

        // Set OnClickListener
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, ClassificacaoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("RODADA", objRodada);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.e("onClick Item Rodada", e.getMessage());
                }
            }
        });
        return rowView;
    }
}