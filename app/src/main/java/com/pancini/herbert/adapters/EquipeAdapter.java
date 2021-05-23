package com.pancini.herbert.adapters;

import java.util.List;
import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.entities.Equipe;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EquipeAdapter extends BaseAdapter {
    private final Context context;
    private final List<Equipe> equipes;

    public EquipeAdapter(Context context, List<Equipe> equipes) {
        this.context = context;
        this.equipes = equipes;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.equipes.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return this.equipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Equipe equipe = (Equipe)getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.equipe_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txtNome = (TextView)convertView.findViewById(R.id.nome_equipe);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtNome.setText(equipe.getNome());

        return convertView;
    }

    static class ViewHolder {
        TextView txtNome;
    }
}
