package k4n.eugenia.yandexapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import k4n.eugenia.yandexapp.R;
import k4n.eugenia.yandexapp.ui.base.BaseFragment;
import k4n.eugenia.yandexapp.ui.favourites.FavouritesFragment;
import k4n.eugenia.yandexapp.ui.home.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    private WeakReference<BaseFragment> currentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_favourites:
                    changeFragment(FavouritesFragment.newInstance(true));
                    return true;
                case R.id.navigation_history:
                    changeFragment(FavouritesFragment.newInstance(false));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        changeFragment(HomeFragment.newInstance());
    }

    private void changeFragment(BaseFragment f) {
        currentFragment = new WeakReference<>(f);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, f);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        BaseFragment f = currentFragment.get();
        if (f != null && f.allowBackPressed()) {
            super.onBackPressed();
        }
    }
}
