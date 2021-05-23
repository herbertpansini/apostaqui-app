package com.pancini.herbert.apostaqui;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.pancini.herbert.adapters.ClassificacaoAdapter;
import com.pancini.herbert.entities.Classificacao;
import com.pancini.herbert.services.ClassificacaoService;
import org.json.JSONObject;
import java.util.ArrayList;

public class ClassificacaoFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    ArrayList<Classificacao> mTabela;
    private ClassificacaoAdapter mAdapter;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        new MyTaskAsync().execute();
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        /*String item = (String) listView.getAdapter().getItem(position);
        if (getActivity() instanceof SearchFragment.OnItem1SelectedListener) {
            ((SearchFragment.OnItem1SelectedListener) getActivity()).OnItem1SelectedListener(item);
        }
        getFragmentManager().popBackStack();*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.search_fragment, container, false);
        ListView listView = (ListView) layout.findViewById(android.R.id.list);
        TextView emptyTextView = (TextView) layout.findViewById(android.R.id.empty);
        listView.setEmptyView(emptyTextView);
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh :
                new MyTaskAsync().execute();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        ArrayList<Classificacao> filteredValues = new ArrayList<Classificacao>(mTabela);
        for (Classificacao value : mTabela) {
            if (!value.getNome().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }

        mAdapter = new ClassificacaoAdapter(mContext, filteredValues);
        setListAdapter(mAdapter);

        return false;
    }

    public void resetSearch() {
        mAdapter = new ClassificacaoAdapter(mContext, mTabela);
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }

    private class MyTaskAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private String mUrl = getResources().getString(R.string.default_url) + "classificacao/";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jsonParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jsonParser.getJSONFromUrl(mUrl);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            ClassificacaoService classificacaoService = new ClassificacaoService();
            mTabela = (ArrayList<Classificacao>)classificacaoService.getClassificacao(json);

            mAdapter = new ClassificacaoAdapter(mContext, mTabela);
            setListAdapter(mAdapter);

            progressDialog.dismiss();
        }
    }
}