package edu.temple.bookcase;
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
    String bookTitle, title, author, publisher;
    ImageView imageView;
    EditText editText;
    Button button;
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
            bookTitle = getArguments().getString(BOOK_TITLE);
        }
    }


    public void showBook(Book objectBook)
    {
        author = objectBook.getAuthor();
        title = objectBook.getTitle(); publisher = objectBook.getPublished();
        textView.setText(" \"" + title + "\" "); textView.append(", " + author); textView.append(", " + objectBook);
        textView.setTextSize(30);
        String imageURL = objectBook.getCoverURL();
        Picasso.get().load(imageURL).into(imageView);
    }

    ArrayList<String> titleArray;
    ArrayList<String> authorArray;
    ArrayList<String> publishyrArray;
    String searchText;
    JSONObject jsonObject;
    Book books;

    public void findBook(final JSONArray bookArray){
        titleArray = new ArrayList<>(); authorArray = new ArrayList<>(); publishyrArray = new ArrayList<>();

        for(int i = 0; i < bookArray.length(); i++) {
            try {
                jsonObject = bookArray.getJSONObject(i);
                String title = jsonObject.getString("Title");
                titleArray.add(title);
                String author = jsonObject.getString("Author");
                authorArray.add(author);
                String publihser = jsonObject.getString("Published");
                publishyrArray.add(publihser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                searchText = editText.getText().toString();
                Log.d("Title", searchText);
                if(searchText.equals(" ")){
                    textView.setText("No books");
                }
                for (int i = 0; i < bookArray.length(); i++) {
                    try {
                        jsonObject = bookArray.getJSONObject(i);
                        books = new Book(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (searchText.equals(titleArray.get(i))) {
                        showBook(books);
                    } else if (searchText.equals(authorArray.get(i))) {
                        showBook(books);
                    } else if (searchText.equals(publishyrArray.get(i))) {
                        showBook(books);
                    }

                }
            }
        });
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_book_details,container,false);

        textView = view.findViewById(R.id.bTitle);
        imageView = view.findViewById(R.id.bImage);
        button = view.findViewById(R.id.button);
        editText = view.findViewById(R.id.searchBar);
        if(getArguments() != null)
        {
            showBook(pBook);
        }
        return view;
    }


}
