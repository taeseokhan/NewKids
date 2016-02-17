package senori.or.jp.newkids;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import senori.or.jp.newkids.adapter.SectionsPagerAdapter;

public class DayActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.rgb(183, 199, 234));


        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("공지"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("사진첩"), 1);
        tabLayout.addTab(tabLayout.newTab().setText("일정"), 2);
        tabLayout.addTab(tabLayout.newTab().setText("식단표"), 3);


        sectionsPagerAdapter = new

                SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext(), tabLayout);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(sectionsPagerAdapter);
        //  tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
