package com.pancini.herbert.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.entities.Jogo;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JogoAdapter extends ArrayAdapter<Jogo> {
    private final Context context;
    private final ArrayList<Jogo> confrontos;

    private TextView itemHeader;

    private TextView txtTitleTop;
    private TextView txtTitleLeft;
    private ImageView iconLeft;
    private TextView txtTitleVs;
    private ImageView iconRight;
    private TextView txtTitleRight;
    private LinearLayout linearLayout;

    public JogoAdapter(Context context, ArrayList<Jogo> confrontos) {
        super(context, R.layout.jogo_item, confrontos);
        this.confrontos = confrontos;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Jogo jogo = (Jogo)getItem(position);

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = null;
        if (position % 11 == 0) {
            rowView = inflater.inflate(R.layout.jogo_header, parent, false);

            itemHeader = (TextView) rowView.findViewById(R.id.item_header);

            itemHeader.setText(String.format("%02d", jogo.getNumeroRodada()) + "ª Rodada");
        } else {
            rowView = inflater.inflate(R.layout.jogo_item, parent, false);
            linearLayout = (LinearLayout) rowView.findViewById(R.id.jogo_item_linear_layout);

            // set FindViewById
            txtTitleTop = (TextView)rowView.findViewById(R.id.title_top);
            txtTitleLeft = (TextView)rowView.findViewById(R.id.title_left);
            iconLeft = (ImageView)rowView.findViewById(R.id.left_icon);
            txtTitleVs = (TextView)rowView.findViewById(R.id.title_vs);
            iconRight = (ImageView)rowView.findViewById(R.id.right_icon);
            txtTitleRight = (TextView)rowView.findViewById(R.id.title_right);

            // set Values
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MM/yyyy"); // Set your date format
            String formattedDate = sdf.format(jogo.getHorario());
            txtTitleTop.setText(formattedDate);

            txtTitleLeft.setText(jogo.getSiglaEquipeMandante());
            iconLeft.setImageResource(jogo.getEscudoEquipeMandante());
            if (jogo.getPlacarEquipeMandante() != null && jogo.getPlacarEquipeVisitante() != null) {
                txtTitleVs.setText(String.valueOf(jogo.getPlacarEquipeMandante()) + " X " + String.valueOf(jogo.getPlacarEquipeVisitante()));
            } else {
                SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm"); // Set your date format
                String formattedHour = sdfHour.format(jogo.getHorario());
                txtTitleVs.setText(formattedHour);
            }
            iconRight.setImageResource(jogo.getEscudoEquipeVisitante());
            txtTitleRight.setText(jogo.getSiglaEquipeVisitante());
        }
        return rowView;
    }
}