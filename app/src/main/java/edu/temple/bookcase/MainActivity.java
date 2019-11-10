package edu.temple.bookcase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Bundle;

import org.json.JSONArray;


public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener {

    boolean oneFragment;
    BookDetailsFragment bookDetailsFragment;
    ViewPagerFragment viewPagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oneFragment = findViewById(R.id.descFragment) == null;
        bookDetailsFragment = new BookDetailsFragment();
        BookListFragment listFragment = new BookListFragment();
        viewPagerFragment = new ViewPagerFragment();

        if (!oneFragment) {
            newFragment(listFragment, R.id.bookFragment);
            newFragment(bookDetailsFragment, R.id.descFragment);
        } else {
            newFragment(viewPagerFragment, R.id.myPager);
        }
    }

    public void newFragment(Fragment fragment, int ID) {
        getSupportFragmentManager().
                beginTransaction().
                replace(ID, fragment).
                addToBackStack(null).
                commit();
    }


    @Override
    public void onFragmentInteraction(Book objectBook) {

        bookDetailsFragment.showBook(objectBook);
    }

}