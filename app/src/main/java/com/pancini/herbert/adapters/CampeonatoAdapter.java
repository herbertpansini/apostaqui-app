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
import com.pancini.herbert.apostaqui.JogoActivity;
import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.entities.Campeonato;

public class CampeonatoAdapter extends ArrayAdapter<Campeonato> {
	private final Context context;
	private final ArrayList<Campeonato> campeonatos;

	private TextView tvNome;
	
	public CampeonatoAdapter(Context context, ArrayList<Campeonato> campeonatos) {
		super(context, R.layout.campeonato_item, campeonatos);
		this.context = context;
		this.campeonatos = campeonatos;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 2. Get rowView from inflater
		View rowView = null;
		rowView = inflater.inflate(R.layout.campeonato_item, parent, false);

		tvNome = (TextView) rowView.findViewById(R.id.nome_campeonato);

		// 3. Create Object campeonato
		final Campeonato campeonato = (Campeonato) this.campeonatos.get(position);

		tvNome.setText(campeonato.getNome());

		// Set OnClickListener
		rowView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try	{
					Intent intent = new Intent(context, JogoActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("CAMPEONATO", campeonato);
					context.startActivity(intent);
				} catch (Exception e) {
					Log.e("onClick Item Campeonato", e.getMessage());
				}
			}
		});
		return rowView;
	}
}
