package com.example.mpproject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import javax.annotation.Nonnull;

import me.relex.circleindicator.CircleIndicator3;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

public class mainlist_control extends Fragment {

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 5;
    private CircleIndicator3 mIndicater;

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    int option = 0;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.frag_mainlist_control, container, false);

        btn1 = view.findViewById(R.id.vsYesterday);
        btn2 = view.findViewById(R.id.vsLastWeek);
        btn3 = view.findViewById(R.id.vsLastMonth);
        btn4 = view.findViewById(R.id.vsLastYear);

        mPager = (ViewPager2) view.findViewById(R.id.viewpager);
        pagerAdapter = new MyAdapter(getActivity(), num_page);
        mPager.setAdapter(pagerAdapter);
        mPager.setPadding(-50,0,-50,0);

        mIndicater = (CircleIndicator3) view.findViewById(R.id.indicator);
        mIndicater.setViewPager(mPager);
        mIndicater.createIndicators(num_page, 0);

        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(5);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if(positionOffsetPixels == 0){
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                mIndicater.animatePageSelected(position%num_page);
            }
        });

        final float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);
        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);

                if(mPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(mPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = 0;
                btn1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_clicked));
                btn2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                btn3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                btn4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                mPager.setCurrentItem(POSITION_NONE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = 1;
                btn2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_clicked));
                btn1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                btn3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                btn4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                mPager.setCurrentItem(POSITION_NONE);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = 2;
                btn3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_clicked));
                btn2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                btn1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                btn4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                mPager.setCurrentItem(POSITION_NONE);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = 3;
                btn4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_clicked));
                btn2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                btn3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                btn1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_idle));
                mPager.setCurrentItem(POSITION_NONE);
            }
        });

        return view;
    }


    private class MyAdapter extends FragmentStateAdapter{
        public int mCount;
        public MyAdapter(FragmentActivity fa, int count){
            super(fa);
            mCount = count;
        }

        @Nonnull
        @Override
        public Fragment createFragment(int position){
            int index = getRealPosition(position);

            if(index == 0) return new Frag_mainlist(option);
            else if (index == 1) return new Frag_mainlist2(option);
            else if (index == 2) return new Frag_mainlist3(option);
            else if (index == 3) return new Frag_mainlist4(option);
            else return new Frag_mainlist5(option);

        }

        @Override
        public int getItemCount(){
            return 2000;
        }

        public int getRealPosition(int position){return position%mCount;}


    }

}
