package k4n.eugenia.yandexapp.ui.base;

import android.support.v4.app.Fragment;


/**
 * Created by Eugenia Kan on 18.04.2017.
 */

public class BaseFragment extends Fragment {

    /**
     * Можно ли обработать нажатие кнопки назад
     * или фрагмент обработает его сам
     * @return
     */
    public boolean allowBackPressed() {
        return true;
    }
}
