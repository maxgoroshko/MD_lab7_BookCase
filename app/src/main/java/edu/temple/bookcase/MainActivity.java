package edu.temple.bookcase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.res.Resources;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener {

    ArrayList<String> books = new ArrayList<>();
    boolean oneFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        books.addAll(Arrays.asList(res.getStringArray(R.array.books)));

        oneFragment = (findViewById(R.id.descFragment) == null);

        Fragment bookFragment = getSupportFragmentManager().findFragmentById(R.id.bookFragment);
        if(bookFragment == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.bookFragment,BookListFragment.newInstance(books))
                    .commit();
        }
        else if(bookFragment instanceof BookDetailsFragment) //MB
        {
            getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    public void onFragmentInteraction(int position) {

    }
}