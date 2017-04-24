package k4n.eugenia.yandexapp.ui.favourites;


import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import k4n.eugenia.yandexapp.R;
import k4n.eugenia.yandexapp.data.HistoryContentProvider;
import k4n.eugenia.yandexapp.data.HistorySQLiteHelper;
import k4n.eugenia.yandexapp.data.models.Translate;
import k4n.eugenia.yandexapp.ui.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends BaseFragment implements HistoryAdapter.HistoryCallback {

    private static final String ARG_SHOW_FAVOURITES_ONLY = "favouritesOnly";

    private RecyclerView recyclerView;
    private List<Translate> history = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private boolean favouritesOnly = false;

    public static BaseFragment newInstance(boolean favouritesOnly) {
        BaseFragment f = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SHOW_FAVOURITES_ONLY, favouritesOnly);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favouritesOnly = getArguments().getBoolean(ARG_SHOW_FAVOURITES_ONLY);
        initView();
        readHistory(favouritesOnly);
    }

    private void initView() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdapter = new HistoryAdapter(history, this);
        recyclerView.setAdapter(historyAdapter);
    }

    /**
     * Читаем всю историю или только избранное
     * @param favouritesOnly
     */
    private void readHistory(boolean favouritesOnly) {
        Uri uri = Uri.parse(HistoryContentProvider.URL);
        Cursor cursor;
        if (favouritesOnly) {
            cursor = getActivity().getContentResolver().query(uri, null,
                    HistorySQLiteHelper.History.COLUMN_IS_FAVOURITE + " = ?",
                    new String[]{"1"}, null);
        } else {
            cursor = getActivity().getContentResolver().query(uri, null,
                    null,
                    null, null);
        }

        if (cursor.moveToFirst()) {
            history.clear();
            history.add(new Translate(cursor));
            while (cursor.moveToNext()) {
                history.add(new Translate(cursor));
            }
        }
        historyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHistoryClick(final Translate translate) {
        //TODO: Открыть переводчик с этим словом
        String menuText;
        if (translate.isFavourite()) {
            menuText = getString(R.string.favourites_remove_from_favourites);
        } else {
            menuText = getString(R.string.favourites_add_to_favourites);
        }
        final String[] menuItems ={menuText, getString(R.string.favourites_delete), getString(R.string.favourites_open)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.favourites_select_action)); // заголовок для диалога

        builder.setItems(menuItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        updateHistory(translate, !translate.isFavourite());
                        readHistory(favouritesOnly);
                        break;
                    case 1:
                        deleteFromHistory(translate);
                        readHistory(favouritesOnly);
                        break;
                    case 2:
                        Toast.makeText(getContext(), getString(R.string.favourites_not_supported), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.show();
    }

    private void updateHistory(Translate translate, boolean addToFavourites) {
        if (addToFavourites) {
            translate.setFavourite(true);
        } else {
            translate.setFavourite(false);
        }

        Uri uri = Uri.parse(HistoryContentProvider.URL);
        getActivity().getContentResolver().update(uri, translate.toContentValues(),
                HistorySQLiteHelper.History._ID + " = ?",
                new String[]{String.valueOf(translate.getId())});
    }

    private void deleteFromHistory(Translate translate) {
        Uri uri = Uri.parse(HistoryContentProvider.URL);
        getActivity().getContentResolver().delete(uri,
                HistorySQLiteHelper.History._ID + " = ?",
                new String[]{String.valueOf(translate.getId())});
    }

}
