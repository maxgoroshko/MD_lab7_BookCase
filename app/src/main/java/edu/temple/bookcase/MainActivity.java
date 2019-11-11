package edu.temple.bookcase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener {

    boolean oneFragment;
    BookDetailsFragment bookDetailsFragment;
    ViewPagerFragment viewPagerFragment;
    BookListFragment listFragment;
    EditText searchText;
    Button button;
    JSONArray bookJSON;
    String findBook;
    ArrayList<Book> listBooks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchText = findViewById(R.id.searchText);
        button = findViewById(R.id.searchButton);
        listBooks = new ArrayList<>();

        oneFragment = findViewById(R.id.descFragment) == null;
        bookDetailsFragment = new BookDetailsFragment();
        listFragment = new BookListFragment();
        viewPagerFragment = new ViewPagerFragment();

        if (!oneFragment) {
            newFragment(listFragment, R.id.bookFragment);
            newFragment(bookDetailsFragment, R.id.descFragment);
        } else {
            newFragment(viewPagerFragment, R.id.myPager);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findBook = searchText.getText().toString();
                getBook(findBook);
                Log.d("Test10110010101001010101101001", findBook);
            }
        });
    }

    public void newFragment(Fragment fragment, int ID) {
        getSupportFragmentManager().
                beginTransaction().
                replace(ID, fragment).
                addToBackStack(null).
                commit();
    }


    public void getBook(final String search) {
        new Thread() {
            public void run() {
                try {
                    String urlString = "https://kamorris.com/lab/audlib/booksearch.php?search=" + search;
                    URL url = new URL(urlString);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder builder = new StringBuilder();
                    String tmpString;
                    while ((tmpString = reader.readLine()) != null) {
                        builder.append(tmpString);
                    }
                    Message msg = Message.obtain();
                    msg.obj = builder.toString();
                    urlHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler urlHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                bookJSON = new JSONArray((String) msg.obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listBooks.clear();
            for(int i = 0; i < bookJSON.length(); i++){
                try {
                    listBooks.add(new Book(bookJSON.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(oneFragment) {
                viewPagerFragment.addPager(listBooks);
            } else {
                listFragment.getBooks(listBooks);
            }
            return false;
        }
    });




    @Override
    public void onFragmentInteraction(Book objectBook) {

        bookDetailsFragment.showBook(objectBook);
    }
}