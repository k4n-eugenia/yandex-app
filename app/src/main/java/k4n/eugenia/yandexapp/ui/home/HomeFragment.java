package k4n.eugenia.yandexapp.ui.home;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import k4n.eugenia.yandexapp.App;
import k4n.eugenia.yandexapp.R;
import k4n.eugenia.yandexapp.data.HistoryContentProvider;
import k4n.eugenia.yandexapp.data.models.Languages;
import k4n.eugenia.yandexapp.data.models.Translate;
import k4n.eugenia.yandexapp.data.models.TranslateResult;
import k4n.eugenia.yandexapp.ui.base.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements LanguagesAdapter.LanguageCallback {

    private EditText textToTranslateEdit;
    private ImageView btnSwapLanguages;
    private TextView btnSourceLanguage;
    private TextView btnDestinationLanguage;
    private LinearLayout resultsContainer;

    private Languages supportedLangs;
    private String[] langs;
    private BottomSheetBehavior languagesDialog;
    private RecyclerView recyclerView;

    private Handler handler = new Handler();

    private int currentIndex = -1;
    private LanguagesAdapter languagesAdapter;

    private TranslateResult result; // Последний результат перевода

    public static BaseFragment newInstance() {
        BaseFragment f = new HomeFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleArguments();
        requestLanguages();
    }

    private void requestLanguages() {
        App.getApi().getLanguages(getString(R.string.ui_language)).enqueue(languagesCallback);
    }

    private void translateText(String text) {
        if (text == null) return;

        if (!TextUtils.isEmpty(text.trim())) {
            App.getApi().translate(langsToDirection(langs), text).enqueue(translateCallback);
        } else {
            clearTranslate();
        }
    }

    /**
     * Получаем из списка языков строку с напралением перевода
     * @param langs
     * @return
     */
    private String langsToDirection(String[] langs) {
        if (langs.length > 1)
            return langs[0] + "-" + langs[1];

        if (langs.length > 0)
            return langs[0];

        return "";
    }

    private void initView() {
        textToTranslateEdit = (EditText) getView().findViewById(R.id.text_to_translate);
        textToTranslateEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                translateText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resultsContainer = (LinearLayout) getView().findViewById(R.id.result);

        initLanguageSwitcher();
        initLanguagesBottomBehavior();
    }

    private void initLanguagesBottomBehavior() {
        // Список языков
        recyclerView = (RecyclerView) getView().findViewById(R.id.language_bottomsheet);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        languagesAdapter = new LanguagesAdapter(supportedLangs.getLangs(), this);
        recyclerView.setAdapter(languagesAdapter);



        // BottomSheet
        languagesDialog = BottomSheetBehavior.from(recyclerView);
        languagesDialog.setPeekHeight(0);
        languagesDialog
                .setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_HIDDEN:

                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // no op
                    }
                });
    }

    private void initLanguageSwitcher() {
        btnSourceLanguage = (TextView) getView().findViewById(R.id.btn_source_language);
        btnDestinationLanguage = (TextView) getView().findViewById(R.id.btn_destination_language);

        btnSourceLanguage.setTag(0);
        btnDestinationLanguage.setTag(1);

        btnSourceLanguage.setOnClickListener(showLanguagesDialogListener);
        btnDestinationLanguage.setOnClickListener(showLanguagesDialogListener);

        btnSwapLanguages = (ImageView) getView().findViewById(R.id.btn_swap_languages);
        btnSwapLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = langs[0];
                langs[0] = langs[1];
                langs[1] = tmp;

                changeLanguage(langs);

                translateText(textToTranslateEdit.getText().toString());
            }
        });

        String dirDefault = getString(R.string.default_language_dir);

        changeLanguage(dirDefault);
    }

    private void changeLanguage(String direction) {
        langs = direction.split("-");
        changeLanguage(langs);
    }

    private void changeLanguage(String[] langs) {
        if (langs.length>1) {
            String sourceLanguage = supportedLangs.getLangs().get(langs[0]);
            String destinationLanguage = supportedLangs.getLangs().get(langs[1]);
            btnSourceLanguage.setText(sourceLanguage);
            btnDestinationLanguage.setText(destinationLanguage);
        }
    }

    private void showTranslate(TranslateResult result) {
        clearTranslate();
        if (result.getText().size() > 0 && result.getText() !=null) {
            for (String variant : result.getText()) {
                TextView tv = new TextView(getContext());
                tv.setText(variant);
                tv.setPadding(getResources().getDimensionPixelSize(R.dimen.home_container_padding),
                        0,
                        getResources().getDimensionPixelSize(R.dimen.home_container_padding),
                        getResources().getDimensionPixelSize(R.dimen.home_container_padding));
                resultsContainer.addView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    private void clearTranslate() {
        resultsContainer.removeAllViews();
    }

    private void showLanguagesDialog(int index) {
        currentIndex = index;
        languagesDialog.setState(BottomSheetBehavior.STATE_EXPANDED);
        languagesAdapter.setCurrentLanguage(langs[index]);
        languagesAdapter.notifyDataSetChanged();
    }

    private void hideLanguagesDialog() {
        languagesDialog.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void handleArguments() {
        if (getArguments() != null) {
            // No arguments right now
        }
    }

    @Override
    public boolean allowBackPressed() {
        if (languagesDialog.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            hideLanguagesDialog();
            return false;
        }
        return true;
    }

    //
    // Колбэки UI
    //

    private View.OnClickListener showLanguagesDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showLanguagesDialog((Integer) v.getTag());
        }
    };

    //
    // Колбэки Яндекс API
    //

    private Callback<Languages> languagesCallback = new Callback<Languages>() {

        @Override
        public void onResponse(Call<Languages> call, Response<Languages> response) {
            supportedLangs = response.body();

            if (isAdded() && isVisible() && !isRemoving()) {
                initView();
            }
        }

        @Override
        public void onFailure(Call<Languages> call, Throwable t) {
            t.printStackTrace();
        }
    };

    private Callback<TranslateResult> translateCallback = new Callback<TranslateResult>() {
        @Override
        public void onResponse(Call<TranslateResult> call, Response<TranslateResult> response) {
            result = response.body();
            if (isAdded() && isVisible() && !isRemoving()) {
                // Чтобы не добавлять обрывочные переводы ждём 2 секунды
                // перед добавлением в историю, если не пришёл новый перевод
                handler.removeCallbacks(saveToHistoryRunnable);
                handler.postDelayed(saveToHistoryRunnable, 2000L /* 2 секунды */);
                showTranslate(result);
            }
        }

        @Override
        public void onFailure(Call<TranslateResult> call, Throwable t) {
            t.printStackTrace();
        }
    };

    private void addToHistory(Translate translate) {
        Uri uri = Uri.parse(HistoryContentProvider.URL);
        getActivity().getContentResolver().insert(uri, translate.toContentValues());
    }

    @Override
    public void onLanguageSelected(String code) {
        if (currentIndex >= 0) {
            langs[currentIndex] = code;
            changeLanguage(langs);
            translateText(textToTranslateEdit.getText().toString());
            currentIndex = -1;
        }
        hideLanguagesDialog();
    }

    private Runnable saveToHistoryRunnable = new Runnable() {
        @Override
        public void run() {
            String translate = "";
            if (!result.getText().isEmpty()) {
                translate = result.getText().get(0);
            }
            addToHistory(new Translate(textToTranslateEdit.getText().toString(), translate, result.getLang(), false));
        }
    };
}
