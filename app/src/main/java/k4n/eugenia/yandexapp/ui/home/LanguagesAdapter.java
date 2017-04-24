package k4n.eugenia.yandexapp.ui.home;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import k4n.eugenia.yandexapp.R;

/**
 * Created by Eugenia Kan on 16.04.2017.
 */

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.ViewHolder> {

    private HashMap<String, String> languages;
    private List<String> languageCodes;
    private LanguageCallback callback;
    private String currentLanguageCode;

    public LanguagesAdapter(HashMap<String, String> languages, LanguageCallback callback) {
        this.languages = languages;
        this.languageCodes = new ArrayList<>(languages.keySet());
        Collections.sort(this.languageCodes);
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_language, parent, false);

        return new ViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String langCode = languageCodes.get(holder.getAdapterPosition());
        holder.bind(langCode, languages.get(langCode), langCode.equals(currentLanguageCode));
    }

    @Override
    public int getItemCount() {
        return languageCodes.size();
    }

    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguageCode = currentLanguage;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleTextView;
        private String code;
        private LanguageCallback callback;

        public ViewHolder(View itemView, LanguageCallback callback) {
            super(itemView);
            this.callback = callback;
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
        }

        public void bind(String code, String title, boolean current) {
            this.code = code;
            titleTextView.setText(title);
            if (current) {
                titleTextView.setTypeface(null, Typeface.BOLD);
            } else {
                titleTextView.setTypeface(null, Typeface.NORMAL);
            }
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.onLanguageSelected(code);
            }
        }
    }

    public interface LanguageCallback {
        void onLanguageSelected(String code);
    }
}
