package com.herorickystudios.jiulia_socialmidia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.herorickystudios.jiulia_socialmidia.fragmentos.ExploreFragment;
import com.herorickystudios.jiulia_socialmidia.fragmentos.HomeScroll;
import com.herorickystudios.jiulia_socialmidia.fragmentos.ProfileFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class Home_Show extends AppCompatActivity {

    SmartTabLayout smartTabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_show);


        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewpager);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Pagina inicial", HomeScroll.class)
                .add("Explorar", ExploreFragment.class)
                .add("Seu Perfil", ProfileFragment.class)
                .create()
        );
        viewPager.setAdapter( adapter );
        smartTabLayout.setViewPager( viewPager );
    }
}