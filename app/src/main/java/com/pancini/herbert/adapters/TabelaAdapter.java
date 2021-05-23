package com.pancini.herbert.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.entities.Classificacao;

import java.util.ArrayList;

public class TabelaAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Classificacao> tablelist; // Original Values
    private ArrayList<Classificacao> mStringFilterList; // Values to be displayed
    private final Context context;
    ValueFilter valueFilter;

    public TabelaAdapter(Context context, ArrayList<Classificacao> tabela) {
        this.context = context;
        this.tablelist = tabela;
        this.mStringFilterList = tabela;
    }

    @Override
    public int getCount() {
        return this.tablelist.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tablelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tvClassificacao;
        TextView tvNome;
        TextView tvPontos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.classificacao_item, parent, false);

            holder = new ViewHolder();
            holder.tvClassificacao = (TextView)convertView.findViewById(R.id.classificacao);
            holder.tvNome = (TextView)convertView.findViewById(R.id.nome_classificado);
            holder.tvPontos = (TextView)convertView.findViewById(R.id.pontos);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Classificacao objTabela = (Classificacao) getItem(position);
        holder.tvClassificacao.setText(String.valueOf(objTabela.getClassificacao()));
        holder.tvNome.setText(objTabela.getNome());
        holder.tvPontos.setText(String.valueOf(objTabela.getPontos()) + " PTS");

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
                ArrayList<Classificacao> filterList = new ArrayList<Classificacao>();
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
            tablelist = (ArrayList<Classificacao>) results.values;
            notifyDataSetChanged();
        }
    }
}