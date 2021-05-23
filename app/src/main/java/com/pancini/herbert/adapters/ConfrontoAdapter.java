package com.pancini.herbert.adapters;

import java.text.SimpleDateFormat;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.entities.Confronto;

public class ConfrontoAdapter extends BaseAdapter {
    private final Context context;
    private final List<Confronto> confrontos;

    public ConfrontoAdapter(Context context, List<Confronto> confrontos) {
        this.context = context;
        this.confrontos = confrontos;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.confrontos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return this.confrontos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Confronto confronto = (Confronto)getItem(position);
        ConfrontoAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.confronto_item, parent, false);

            viewHolder = new ConfrontoAdapter.ViewHolder();
            viewHolder.txtTitleTop = (TextView)convertView.findViewById(R.id.title_top);

            viewHolder.txtTitleLeft = (TextView)convertView.findViewById(R.id.title_left);
            viewHolder.iconLeft = (ImageView)convertView.findViewById(R.id.left_icon);

            viewHolder.txtTitleVs = (TextView)convertView.findViewById(R.id.title_vs);
            viewHolder.iconRight = (ImageView)convertView.findViewById(R.id.right_icon);

            viewHolder.txtTitleRight = (TextView)convertView.findViewById(R.id.title_right);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ConfrontoAdapter.ViewHolder) convertView.getTag();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MM/yyyy"); // Set your date format
        String formattedDate = sdf.format(confronto.getHorario());
        viewHolder.txtTitleTop.setText(formattedDate);

        viewHolder.txtTitleLeft.setText(confronto.getSiglaEquipeMandante());
        viewHolder.iconLeft.setImageResource(confronto.getEscudoEquipeMandante());
        if (confronto.getPlacarEquipeMandante() != null && confronto.getPlacarEquipeVisitante() != null) {
            viewHolder.txtTitleVs.setText(String.valueOf(confronto.getPlacarEquipeMandante()) + " X " + String.valueOf(confronto.getPlacarEquipeVisitante()));
        } else {
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm"); // Set your date format
            String formattedHour = sdfHour.format(confronto.getHorario());
            viewHolder.txtTitleVs.setText(formattedHour);
        }
        viewHolder.iconRight.setImageResource(confronto.getEscudoEquipeVisitante());
        viewHolder.txtTitleRight.setText(confronto.getSiglaEquipeVisitante());
        return convertView;
    }

    static class ViewHolder {
        TextView txtTitleTop;
        TextView txtTitleLeft;
        ImageView iconLeft;
        TextView txtTitleVs;
        ImageView iconRight;
        TextView txtTitleRight;
    }
}