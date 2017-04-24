package k4n.eugenia.yandexapp.ui.favourites;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import k4n.eugenia.yandexapp.R;
import k4n.eugenia.yandexapp.data.models.Translate;

/**
 * Created by Eugenia Kan on 16.04.2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Translate> translates;
    private HistoryCallback callback;

    public HistoryAdapter(List<Translate> translates, HistoryCallback callback) {
        this.translates = translates;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_history, parent, false);

        return new ViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Translate translate = translates.get(holder.getAdapterPosition());
        holder.bind(translate);
    }

    @Override
    public int getItemCount() {
        return translates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleTextView;
        private TextView directionTextView;
        private Translate translate;
        private HistoryCallback callback;

        public ViewHolder(View itemView, HistoryCallback callback) {
            super(itemView);
            this.callback = callback;
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            directionTextView = (TextView) itemView.findViewById(R.id.direction);
        }

        public void bind(Translate translate) {
            this.translate = translate;
            titleTextView.setText(translate.getText());
            directionTextView.setText(translate.getDirection());
            if (translate.isFavourite()) {
                titleTextView.setTypeface(null, Typeface.BOLD);
            } else {
                titleTextView.setTypeface(null, Typeface.NORMAL);
            }
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.onHistoryClick(translate);
            }
        }
    }

    public interface HistoryCallback {
        void onHistoryClick(Translate translate);
    }
}
