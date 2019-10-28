package edu.temple.bookcase;

import android.content.Context;

import android.os.Bundle;


import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class ViewPagerFragment extends Fragment {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;



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
        viewPager = v.findViewById(R.id.myPager);
        pagerAdapter = new BookDetailsPageraAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        return v;
    }


    private class BookDetailsPageraAdapter extends FragmentStatePagerAdapter
    {
        BookDetailsPageraAdapter(FragmentManager myFragment)
        {
            super(myFragment);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    return BookDetailsFragment.newInstance(getResources().getStringArray(R.array.books)[position]);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return getResources().getStringArray(R.array.books).length;
        }
    }
}
