package edu.temple.bookcase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;


public class MainActivity extends AppCompatActivity implements BookListFragment.OnFragmentInteractionListener, BookDetailsFragment.BookDetailsInterface {

    boolean oneFragment;
    BookDetailsFragment bookDetailsFragment;
    ViewPagerFragment viewPagerFragment;
    BookListFragment listFragment;
    EditText searchText;
    Button button;
    JSONArray bookJSON;
    String findBook;
    ArrayList<Book> listBooks;
    boolean connected;
    AudiobookService.MediaControlBinder mediaControlBinder;
    String savedSearch;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchText = findViewById(R.id.searchText);
        searchText.clearFocus();
        button = findViewById(R.id.searchButton);
        listBooks = new ArrayList<>();

        oneFragment = findViewById(R.id.descFragment) == null;
        bookDetailsFragment = new BookDetailsFragment();
        listFragment = new BookListFragment();
        viewPagerFragment = new ViewPagerFragment();

        bindService(new Intent(this, AudiobookService.class), serviceConnection, BIND_AUTO_CREATE);
        if (!oneFragment) {
            newFragment(listFragment, R.id.bookFragment);
            newFragment(bookDetailsFragment, R.id.descFragment);
        } else {
            newFragment(bookDetailsFragment,R.id.myPager);
            newFragment(viewPagerFragment, R.id.myPager);
        }
        sharedPreferences = getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findBook = searchText.getText().toString();
                editor.putString("SEARCH", findBook);
                editor.apply();
                getBook(findBook);
            }
        });

        savedSearch = sharedPreferences.getString("SEARCH", "");
        getBook(savedSearch);
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
    public void playBook(int id) {
        mediaControlBinder.play(id);
    }

    @Override
    public void playBookFile(File file) {
        mediaControlBinder.play(file);
    }

    @Override
    public void pauseBook() {
        mediaControlBinder.pause();
    }

    @Override
    public void stopBook() {
        mediaControlBinder.stop();
    }

    @Override
    public void seekBook(int position) {
        mediaControlBinder.seekTo(position);
    }

    @Override
    public void setProgress(Handler progressHandler) {
        mediaControlBinder.setProgressHandler(progressHandler);
    }

    @Override
    public void playBookPosition(int id, int position) {
        mediaControlBinder.play(id, position);
    }

    @Override
    public void playBookFilePosition(File file, int position) {
        mediaControlBinder.play(file, position);
    }


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaControlBinder = ((AudiobookService.MediaControlBinder) service);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
            mediaControlBinder = null;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connected) {
            unbindService(serviceConnection);
            connected = false;
        }
    }

    @Override
    public void onFragmentInteraction(Book objectBook) {

        bookDetailsFragment.showBook(objectBook);
    }
}