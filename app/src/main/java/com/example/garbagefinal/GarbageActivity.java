package com.example.garbagefinal;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;



public class GarbageActivity extends AppCompatActivity  {

    /*
     The Main Activity sets up the fragment manager and
     the two fragments ListFragment and UIFragment.
     */

    private FragmentManager fm;

    Fragment fragmentUI, fragmentList;
    private final static ItemsViewModel DB = new ItemsViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB.initialize(this);

        fm = getSupportFragmentManager();
        //setUpFragments();


        //To handle landscape mode - no navigation needed
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fm;
            Fragment fragmentUI, fragmentList;
            fm = getSupportFragmentManager();
            fragmentUI = fm.findFragmentById(R.id.container_ui_landscape);
            fragmentList = fm.findFragmentById(R.id.container_list);
            if ((fragmentUI == null) && (fragmentList == null)) {
                fragmentUI = new UIFragment();
                fragmentList = new ListFragment();
                fm.beginTransaction()
                        .add(R.id.container_ui_landscape, fragmentUI)
                        .add(R.id.container_list, fragmentList)
                        .commit();
            }
        }

    }


}