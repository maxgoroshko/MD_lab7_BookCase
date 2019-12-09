package edu.temple.bookcase;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BookDetailsFragment extends Fragment {

    private BookDetailsInterface mListener;
    Context c;
    TextView textView;
    String bookSelected, title, author, publisher;
    int currentTime;
    ImageView imageView;
    Book pBook;
    ImageButton playButton, stopButton, pauseButton; Button downloadButton, deleteButton;
    public static final String BOOK_TITLE = "book title";
    SeekBar seekBar; TextView progressText; File file;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void showBook(final Book objectBook)
    {
        author = objectBook.getAuthor();
        title = objectBook.getTitle(); publisher = objectBook.getPublished();
        textView.setText(" \"" + title + "\" "); textView.append(", " + author); textView.append(", " + publisher);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        String imageURL = objectBook.getCoverURL();
        Picasso.get().load(imageURL).into(imageView);

        seekBar.setMax(objectBook.getDuration());

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTime = preferences.getInt("SAVED_PROGRESS" + objectBook.getId(), 0);
                if(currentTime <= 10){
                    currentTime = 0;
                } else {
                    currentTime = currentTime - 10;
                }
                Log.d("Current Time", String.valueOf(currentTime));
                if(file != null){
                    Toast.makeText(getActivity(), "Playing Audio Book", Toast.LENGTH_SHORT).show();

                    ((BookDetailsInterface) c).playBookFilePosition(file, currentTime);
                }else {
                    Toast.makeText(getActivity(), "Streaming Audio Book", Toast.LENGTH_SHORT).show();

                    ((BookDetailsInterface) c).playBookPosition(objectBook.getId(), currentTime);
                }
                ((BookDetailsInterface) c).setProgress(progressHandler);
            }

        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("SAVED_PROGRESS" + objectBook.getId(), seekBar.getProgress());
                editor.apply();
                ((BookDetailsInterface) c).pauseBook();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("SAVED_PROGRESS" + objectBook.getId(), 0);
                editor.apply();
                seekBar.setProgress(0);
                progressText.setText("0s");
                ((BookDetailsInterface) c).stopBook();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file == null) {
                    Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_LONG).show();
                    int bookId = objectBook.getId();
                    downloadBook(bookId);
                } else {
                    Toast.makeText(getActivity(), "Already Downloaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file != null) {
                    file.delete();
                    file = null;
                    Toast.makeText(getActivity(), "File Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "File does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    progressText.setText("" + progress + "s");
                    ((BookDetailsInterface) c).seekBook(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    Handler progressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            updateSeekbar(msg.what);
            return false;
        }
    });

    public void updateSeekbar(int currentTime){
        seekBar.setProgress(currentTime);
        Log.d("Progress", ":" + seekBar.getProgress());
        progressText.setText("" + currentTime + "s");
    }

    public void downloadBook(final int search) {
        new Thread() {
            public void run() {
                try {
                    String urlString = "https://kamorris.com/lab/audlib/download.php?id=" + search;
                    URL url = new URL(urlString);
                    InputStream inputStream = url.openStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int num;
                    while((num = inputStream.read(buffer)) > 0){
                        byteArrayOutputStream.write(buffer, 0, num);
                    }
                    file = new File(getActivity().getFilesDir(), String.valueOf(search));
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_book_details,container,false);

        textView = view.findViewById(R.id.bTitle);
        imageView = view.findViewById(R.id.bImage);
        playButton = view.findViewById(R.id.playButton);
        stopButton = view.findViewById(R.id.stopButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        downloadButton = view.findViewById(R.id.downloadButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        seekBar = view.findViewById(R.id.seekBar);
        progressText = view.findViewById(R.id.progressText);
        preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();


        if(getArguments() != null)
        {
            showBook(pBook);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookListFragment.OnFragmentInteractionListener) {
            mListener = (BookDetailsInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.c = context;
    }

    public interface BookDetailsInterface{
        void playBook(int id);
        void playBookFile(File file);
        void pauseBook();
        void stopBook();
        void seekBook(int position);
        void setProgress(Handler progressHandler);
        void playBookPosition(int id, int position);
        void playBookFilePosition(File file, int position);
    }


}
