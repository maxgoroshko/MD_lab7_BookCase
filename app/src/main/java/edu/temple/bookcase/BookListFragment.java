package edu.temple.bookcase;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
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


public class BookListFragment extends Fragment {


    ListView listView;
    Context c;
    ArrayList<String> listBooks;
    Book book;
    JSONArray bookJSON;


    private OnFragmentInteractionListener fragmentParent;

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
        getBooks();
        return view;
    }




    public void getBooks() {
        new Thread() {
            public void run() {
                String karlUrl = "https://kamorris.com/lab/audlib/booksearch.php";
                try {
                    URL url = new URL(karlUrl);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder sBuilder = new StringBuilder();
                    String tempStr;
                    while ((tempStr = reader.readLine()) != null) {
                        sBuilder.append(tempStr);
                    }
                    Message msg = Message.obtain();
                    msg.obj = sBuilder.toString();
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
            for(int i = 0; i < bookJSON.length(); i++){
                try {
                    JSONObject jsonData = bookJSON.getJSONObject(i);
                    String title = jsonData.getString("title");
                    listBooks.add(title);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, listBooks);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        book = new Book(bookJSON.getJSONObject(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //books = (Book) parent.getItemAtPosition(position);
                    ((OnFragmentInteractionListener) c).onFragmentInteraction(book);
                }
            });
            ((OnFragmentInteractionListener) c).findBook(bookJSON);
            return false;
        }
    });





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

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentParent = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Book objectBook);
        void findBook(JSONArray bookArray);
    }
}
