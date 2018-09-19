package kr.co.anonymous_evcar.evcar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kr.co.anonymous_evcar.evcar.fragment.view1;
import kr.co.anonymous_evcar.evcar.fragment.view2;
import kr.co.anonymous_evcar.evcar.fragment.view3;
import kr.co.anonymous_evcar.evcar.fragment.view4;
import kr.co.anonymous_evcar.evcar.fragment.view5;
import kr.co.anonymous_evcar.evcar.fragment.view6;

public class fragmentAdpter2 extends FragmentStatePagerAdapter {
    public fragmentAdpter2(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0){
            return view4.getInstance();
        }else if(position == 1){
            return view5.getInstance();
        }else {
            return view6.getInstance();
        }
    }

    @Override
    public int getCount(){return 3;}
}
