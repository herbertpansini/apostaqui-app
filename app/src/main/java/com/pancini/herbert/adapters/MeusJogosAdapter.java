package com.pancini.herbert.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.entities.Jogos;
import java.util.ArrayList;

public class MeusJogosAdapter extends ArrayAdapter<Jogos> {
    private final Context context;
    private final ArrayList<Jogos> jogos;

    private TextView tvNumeroAposta;
    private ImageView iconChecked;

    private TextView txtTitleLeft;
    private ImageView iconLeft;

    private RadioButton rbOpcaoMandante;
    private RadioButton rbOpcaoEmpate;
    private RadioButton rbOpcaoVisitante;

    private ImageView iconRight;
    private TextView txtTitleRight;

    private LinearLayout linearLayout;

    public MeusJogosAdapter(Context context, ArrayList<Jogos> jogos) {
        super(context, R.layout.jogos_item, jogos);
        this.context = context;
        this.jogos = jogos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Jogos jogos = this.jogos.get(position);
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = null;
        if (jogos.isHeader()) {
            rowView = inflater.inflate(R.layout.jogo_header, parent, false);

            TextView itemHeader = (TextView) rowView.findViewById(R.id.item_header);

            itemHeader.setText(jogos.getDescricaoRodada());
        } else if (jogos.isTitle()) {
            rowView = inflater.inflate(R.layout.jogos_header, parent, false);

            tvNumeroAposta = (TextView)rowView.findViewById(R.id.tvNumeroAposta);
            iconChecked = (ImageView)rowView.findViewById(R.id.icon_checked);

            tvNumeroAposta.setText( "APOSTA " + String.valueOf(this.jogos.get(position).getApostaId()) + " - PONTOS: " + String.valueOf(this.jogos.get(position).getPontos()) );

            if (this.jogos.get(position).getValida() > 0) {
                iconChecked.setImageResource(R.drawable.checked);
            } else {
                iconChecked.setImageResource(R.drawable.unchecked);
            }
        } else {
            rowView = inflater.inflate(R.layout.jogos_item, parent, false);
            linearLayout = (LinearLayout) rowView.findViewById(R.id.aposta_item_linear_layout);
            if (jogos.getJogou() == -1) {
                linearLayout.setBackgroundColor(Color.WHITE);
            } else {
                if (jogos.getAcertou() > 0) {
                    linearLayout.setBackgroundColor(Color.GREEN);
                } else {
                    linearLayout.setBackgroundColor(Color.RED);
                }
            }

            txtTitleLeft = (TextView) rowView.findViewById(R.id.mandante_title_left); // Sigla Equipe Mandante
            iconLeft = (ImageView) rowView.findViewById(R.id.sigla_left_icon); // Icon  Equipe Mandante

            rbOpcaoMandante = (RadioButton)rowView.findViewById(R.id.rbOpcaoMandante);
            rbOpcaoEmpate = (RadioButton)rowView.findViewById(R.id.rbOpcaoEmpate);
            rbOpcaoVisitante = (RadioButton)rowView.findViewById(R.id.rbOpcaoVisitante);

            iconRight = (ImageView) rowView.findViewById(R.id.sigla_right_icon); // Icon  Equipe Visitante
            txtTitleRight = (TextView) rowView.findViewById(R.id.visitante_title_right); // Sigla Equipe Visitante

            txtTitleLeft.setText(jogos.getSiglaEquipeMandante());
            iconLeft.setImageResource(jogos.getEscudoEquipeMandante());

            iconRight.setImageResource(jogos.getEscudoEquipeVisitante());
            txtTitleRight.setText(jogos.getSiglaEquipeVisitante());

            switch (jogos.getOpcao()) {
                case 0 :
                    rbOpcaoMandante.setChecked(true);
                    break;
                case 1 :
                    rbOpcaoEmpate.setChecked(true);
                    break;
                case 2 :
                    rbOpcaoVisitante.setChecked(true);
                    break;
            }
        }
        return rowView;
    }
}

