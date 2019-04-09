package com.cahjaya.lian.greenhousecabai;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.ViewPager;

/**
 * Created by Lian CahJaya on 28/11/2017.
 */

public class SecondFragment extends Fragment {
    private TabHelper adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentActivity myContext;
    View myView;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.secondlayout, container, false);
        viewPager = (ViewPager) myView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) myView.findViewById(R.id.tabLayout);
        adapter = new TabHelper (myContext.getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Jam");
        //adapter.addFragment(new Tab2Fragment(), "Harian");
        //adapter.addFragment(new Tab3Fragment(), "Mingguan");
        //adapter.addFragment(new Tab4Fragment(), "Bulanan");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return myView;
    }


}
