package com.pancini.herbert.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.pancini.herbert.entities.UsuarioAposta;
import java.util.ArrayList;
import com.pancini.herbert.apostaqui.R;

public class UsuarioApostaAdapter extends BaseAdapter implements Filterable {
    private ArrayList<UsuarioAposta> usuarioApostaslist; // Original Values
    private ArrayList<UsuarioAposta> mStringFilterList; // Values to be displayed
    private final Context context;
    ValueFilter valueFilter;

    public UsuarioApostaAdapter(Context context, ArrayList<UsuarioAposta> usuarioApostas) {
        this.context = context;
        this.usuarioApostaslist = usuarioApostas;
        this.mStringFilterList = usuarioApostas;
    }

    @Override
    public int getCount() {
        return this.usuarioApostaslist.size();
    }

    @Override
    public Object getItem(int position) {
        return this.usuarioApostaslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tvNome;
        TextView tvApostas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.confirma_apostas_item, parent, false);

            holder = new ViewHolder();
            holder.tvNome = (TextView)convertView.findViewById(R.id.tvNome);
            holder.tvApostas = (TextView)convertView.findViewById(R.id.tvApostas);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UsuarioAposta usuarioAposta = (UsuarioAposta) getItem(position);
        holder.tvNome.setText(usuarioAposta.getNome());
        holder.tvApostas.setText(usuarioAposta.getApostas() + " APS");

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<UsuarioAposta> filterList = new ArrayList<UsuarioAposta>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ( (mStringFilterList.get(i).getNome().toUpperCase() ).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                synchronized(this) {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            usuarioApostaslist = (ArrayList<UsuarioAposta>) results.values;
            notifyDataSetChanged();
        }
    }
}
