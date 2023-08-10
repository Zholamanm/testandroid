package com.example.starwarsapp1;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.starwarsapp1.db.MyDbManager;
import com.example.starwarsapp1.fragment.DbFragment;
import com.example.starwarsapp1.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    private MyDbManager myDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, MainFragment.newInstance());
        fragmentTransaction.commit();
        myDbManager = new MyDbManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
    }

    public void onClickReload(View view) {
        reloadAppAndDatabase();
    }

    private void reloadAppAndDatabase() {
        // Clear the fragment back stack
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Clear the existing database and reinitialize it
        myDbManager.closeDb();
        myDbManager = new MyDbManager(this);
        myDbManager.openDb();

        // Reload the MainFragment
        MainFragment mainFragment = MainFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, mainFragment);
        fragmentTransaction.commit();
    }

    public void onClickNextPage(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, DbFragment.newInstance());
        fragmentTransaction.addToBackStack(null); // Add this line to enable back navigation
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }
}
