package com.irfankhoirul.apps.webservicelibrarycomparison.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irfankhoirul.apps.webservicelibrarycomparison.MainActivity;
import com.irfankhoirul.apps.webservicelibrarycomparison.R;
import com.irfankhoirul.apps.webservicelibrarycomparison.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BaseTabLayoutFragment extends Fragment {
    protected static String LIBRARY_VOLLEY = "volley";
    protected static String LIBRARY_OKHTTP = "okhttp";
    protected static String LIBRARY_RETROFIT = "retrofit";

    protected View v;
    protected ViewPagerAdapter adapter;
    protected List<String> tabTitles;
    protected List<Fragment> tabFragments;
    protected MainActivity activity;

    private String library;

    protected TabLayout tabLayout;
    @BindView(R.id.viewPager)
    protected ViewPager viewPager;

    public static BaseTabLayoutFragment newInstance(String param) {
        BaseTabLayoutFragment fragment = new BaseTabLayoutFragment();
        Bundle args = new Bundle();
        args.putString("param", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            library = getArguments().getString("param");
            if (library.equalsIgnoreCase(LIBRARY_VOLLEY)) {
                setTitleAndFragment("volley");
            } else if (library.equalsIgnoreCase(LIBRARY_OKHTTP)) {
                setTitleAndFragment("okhttp");
            } else if (library.equalsIgnoreCase(LIBRARY_RETROFIT)) {
                setTitleAndFragment("retrofit");
            }
        }

        adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), tabTitles, tabFragments);
    }

    protected void setTitleAndFragment(final String library) {
        tabTitles = new ArrayList<String>() {{
            add(getString(R.string.tab_data_title));
            add(getString(R.string.tab_benchmark_title));
        }};
        tabFragments = new ArrayList<Fragment>() {{
            add(DataFragment.newInstance(library));
            add(BenchmarkFragment.newInstance(library));
        }};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_base_tab_layout, container, false);
        ButterKnife.bind(this, v);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(7);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }
}
