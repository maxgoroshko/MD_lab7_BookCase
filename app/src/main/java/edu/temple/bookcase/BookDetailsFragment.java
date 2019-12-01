package edu.temple.bookcase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookDetailsFragment extends Fragment {


    TextView textView;
    String bookSelected, title, author, publisher;
    ImageView imageView;
    Book pBook;


    public static final String BOOK_TITLE = "book title";

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    public static BookDetailsFragment newInstance(Book bookTitle) {
        BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BOOK_TITLE,bookTitle);
        bookDetailsFragment.setArguments(args);
        return bookDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pBook = getArguments().getParcelable(BOOK_TITLE);
        }
    }


    public void showBook(Book objectBook)
    {
        author = objectBook.getAuthor();
        title = objectBook.getTitle(); publisher = objectBook.getPublished();
        textView.setText(" \"" + title + "\" "); textView.append(", " + author); textView.append(", " + publisher);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        String imageURL = objectBook.getCoverURL();
        Picasso.get().load(imageURL).into(imageView);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_book_details,container,false);

        textView = view.findViewById(R.id.bTitle);
        imageView = view.findViewById(R.id.bImage);
        if(getArguments() != null)
        {
            showBook(pBook);
        }
        return view;
    }

}
