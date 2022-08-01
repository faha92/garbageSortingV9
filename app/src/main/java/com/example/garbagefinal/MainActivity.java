package com.example.garbagefinal;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.garbage);

    }

    UIFragment uiFragment = new UIFragment();
    ListFragment listFragment = new ListFragment();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.garbage:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, uiFragment).commit();
                return true;

            case R.id.list:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                return true;


        }
        return false;
    }
}
