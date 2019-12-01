package edu.temple.bookcase;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;


public class BookListFragment extends Fragment {


    ListView listView;
    Context c;
    ArrayList<Book> listBooks;
    Book book;
    BookAdapter adapter;

    private OnFragmentInteractionListener fragmentParent;
    AudiobookService.MediaControlBinder mediaControlBinder;

    public BookListFragment() {
        // Required empty public constructor
    }


    public static BookListFragment newInstance(String param) {
        BookListFragment bookListFragment = new BookListFragment();
        Bundle args = new Bundle();
        bookListFragment.setArguments(args);
        return bookListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_list,container,false);
        listView = view.findViewById(R.id.bookList);
        listBooks = new ArrayList<>();

        return view;
    }




    public void getBooks(final ArrayList<Book> bookArray) {
        adapter = new BookAdapter(c, bookArray);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book = bookArray.get(position);
                ((OnFragmentInteractionListener) c).onFragmentInteraction(book);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            fragmentParent = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.c = context;
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Book objectBook);
    }
}
