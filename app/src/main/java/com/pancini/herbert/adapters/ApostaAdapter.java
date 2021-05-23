package com.pancini.herbert.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.pancini.herbert.apostaqui.R;
import com.pancini.herbert.entities.ItemAposta;

public class ApostaAdapter extends BaseAdapter {
    protected static final String TAG = "ItemAposta";
    private final Context context;
    private final ArrayList<ItemAposta> itemsAposta;

    public ApostaAdapter(Context context, ArrayList<ItemAposta> itemsAposta) {
        this.context = context;
        this.itemsAposta = itemsAposta;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.itemsAposta.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return this.itemsAposta.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    static class ViewHolder {
        TextView txtTitleTop;

        TextView txtTitleLeft;
        ImageView iconLeft;

        RadioGroup rgItemAposta;

        ImageView iconRight;
        TextView txtTitleRight;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_aposta, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txtTitleTop = (TextView) convertView.findViewById(R.id.title_top); // Data e Hora do Jogo

            viewHolder.txtTitleLeft = (TextView) convertView.findViewById(R.id.title_left); // Sigla Equipe Mandante
            viewHolder.iconLeft = (ImageView) convertView.findViewById(R.id.left_icon); // Icon  Equipe Mandante

            viewHolder.rgItemAposta = (RadioGroup) convertView.findViewById(R.id.rgItemAposta);

            viewHolder.iconRight = (ImageView) convertView.findViewById(R.id.right_icon); // Icon  Equipe Visitante
            viewHolder.txtTitleRight = (TextView) convertView.findViewById(R.id.title_right); // Sigla Equipe Visitante

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ItemAposta itemAposta = (ItemAposta) getItem(position);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MM/yyyy HH:mm"); // Set your date format
        String formattedDate = sdf.format(itemAposta.getConfronto().getHorario());
        viewHolder.txtTitleTop.setText(formattedDate);

        viewHolder.txtTitleLeft.setText(itemAposta.getConfronto().getSiglaEquipeMandante());
        viewHolder.iconLeft.setImageResource(itemAposta.getConfronto().getEscudoEquipeMandante());

        viewHolder.iconRight.setImageResource(itemAposta.getConfronto().getEscudoEquipeVisitante());
        viewHolder.txtTitleRight.setText(itemAposta.getConfronto().getSiglaEquipeVisitante());

        viewHolder.rgItemAposta.setOnCheckedChangeListener(null);

        switch (itemAposta.getCurrent()) {
            case ItemAposta.ANSWER_ONE_SELECTED :
                viewHolder.rgItemAposta.check(R.id.rbMandante);
                break;
            case ItemAposta.ANSWER_TWO_SELECTED :
                viewHolder.rgItemAposta.check(R.id.rbEmpate);
                break;
            case ItemAposta.ANSWER_THREE_SELECTED :
                viewHolder.rgItemAposta.check(R.id.rbVisitante);
                break;
            default:
                viewHolder.rgItemAposta.clearCheck();
        }

        viewHolder.rgItemAposta.setTag(itemAposta);

        viewHolder.rgItemAposta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ItemAposta item = (ItemAposta)group.getTag();
                switch (checkedId) {
                    case R.id.rbMandante :
                        item.setCurrent(ItemAposta.ANSWER_ONE_SELECTED);
                        Toast.makeText(context, "Você selecionou: vitória do " + item.getConfronto().getNomeEquipeMandante(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbEmpate :
                        item.setCurrent(ItemAposta.ANSWER_TWO_SELECTED);
                        Toast.makeText(context, "Você selecionou: empate entre " + item.getConfronto().getNomeEquipeMandante() + " X " + item.getConfronto().getNomeEquipeVisitante(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbVisitante :
                        item.setCurrent(ItemAposta.ANSWER_THREE_SELECTED);
                        Toast.makeText(context, "Você selecionou: vitória do " + item.getConfronto().getNomeEquipeVisitante(), Toast.LENGTH_SHORT).show();
                        break;
                    default :
                        item.setCurrent(ItemAposta.NONE);
                }
            }
        });

        return convertView;
    }
}