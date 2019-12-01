package edu.temple.bookcase;

import android.content.Context;

import android.os.Bundle;


import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ViewPagerFragment extends Fragment {

    ViewPager viewPager;
    BookDetailsPageraAdapter pagerAdapter;
    BookDetailsFragment bookFragment;
    Book book;
    ArrayList<Book> books;
    ArrayList<BookDetailsFragment> bookFragments;




    public ViewPagerFragment() {
        // Required empty public constructor
    }


    public static ViewPagerFragment newInstance() {
        return new ViewPagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_pager,container,false);
        pagerAdapter = new BookDetailsPageraAdapter(getFragmentManager());
        viewPager = v.findViewById(R.id.myPager);
        books = new ArrayList<>();
        bookFragments = new ArrayList<>();
        viewPager.setAdapter(pagerAdapter);

        return v;
    }

    public void addPager(final ArrayList<Book> bookArray){
        for(int i = 0; i < bookArray.size(); i++) {
            book = bookArray.get(i);
            bookFragment = BookDetailsFragment.newInstance(book);
            bookFragments.add(bookFragment);
        }
        pagerAdapter.addBooks(bookFragments);
    }


    private class BookDetailsPageraAdapter extends FragmentStatePagerAdapter {

        ArrayList<BookDetailsFragment> pagerFragments;

        public BookDetailsPageraAdapter(FragmentManager fm) {
            super(fm);
            pagerFragments = new ArrayList<>();
        }

        public void addBooks(ArrayList<BookDetailsFragment> books) {
            pagerFragments.clear();
            pagerFragments.addAll(books);
            notifyDataSetChanged();
        }

        public void add(BookDetailsFragment fragment) {
            pagerFragments.add(fragment);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int i) {
            return pagerFragments.get(i);
        }

        @Override
        public int getCount() {
            return pagerFragments.size();
        }
    }
}
