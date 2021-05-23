package com.pancini.herbert.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.entities.Model;

import java.util.ArrayList;

public class ModelAdapter extends BaseAdapter {

    private ArrayList<Model> models;
    private final Context context;

    public ModelAdapter(Context context, ArrayList<Model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return this.models.size();
    }

    @Override
    public Object getItem(int position) {
        return this.models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tvDescription;
        TextView tvCounter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.confirma_apostas_item, parent, false);

            holder = new ViewHolder();
            holder.tvDescription = (TextView)convertView.findViewById(R.id.tvNome);
            holder.tvCounter = (TextView)convertView.findViewById(R.id.tvApostas);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Model model = (Model) getItem(position);
        holder.tvDescription.setText(model.getDescription());
        holder.tvCounter.setText(model.getCounter() + " APS");

        return convertView;
    }
}
