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
    BookDetailsFragment bookDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        books.addAll(Arrays.asList(res.getStringArray(R.array.books)));

        oneFragment = (findViewById(R.id.descFragment) == null);

        Fragment bookFragment = getSupportFragmentManager().findFragmentById(R.id.bookFragment);
        if(bookFragment == null && oneFragment)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.bookFragment, new ViewPagerFragment())
                    .commit();
        }
        else if(bookFragment instanceof BookDetailsFragment && oneFragment) //MB
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.bookFragment, new ViewPagerFragment())
                    .commit();
        }
        else
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.bookFragment, BookListFragment.newInstance(books))
                    .commit();
        }

    }

    @Override
    public void onFragmentInteraction(int position) {

        String bookName = books.get(position);

        bookDetailsFragment = new BookDetailsFragment();

        Bundle detailsBook = new Bundle();

        detailsBook.putString(BookDetailsFragment.BOOK_TITLE,bookName);

        bookDetailsFragment.setArguments(detailsBook);

        if(!oneFragment)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.descFragment,bookDetailsFragment)
                    .commit();
        }

    }
}