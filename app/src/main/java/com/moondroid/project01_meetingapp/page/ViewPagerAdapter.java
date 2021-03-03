package com.moondroid.project01_meetingapp.page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.moondroid.project01_meetingapp.page_tab1_info.InformationFragment;
import com.moondroid.project01_meetingapp.page_tab2_communication.CommunicationFragment;
import com.moondroid.project01_meetingapp.page_tab3_gallery.GalleryFragment;
import com.moondroid.project01_meetingapp.page_tab4_chatting.ChattingFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;
    private String[] tabTitles;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragments = new Fragment[]{new InformationFragment(), new CommunicationFragment(), new GalleryFragment(), new ChattingFragment()};
        tabTitles = new String[]{"정보", "게시판", "사진첩", "채팅"};
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
