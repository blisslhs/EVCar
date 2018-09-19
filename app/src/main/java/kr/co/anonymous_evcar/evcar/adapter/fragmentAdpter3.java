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
import kr.co.anonymous_evcar.evcar.fragment.view7;
import kr.co.anonymous_evcar.evcar.fragment.view8;
import kr.co.anonymous_evcar.evcar.fragment.view9;

public class fragmentAdpter3 extends FragmentStatePagerAdapter {
    public fragmentAdpter3(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0){
            return view7.getInstance();
        }else if(position == 1){
            return view8.getInstance();
        }else {
            return view9.getInstance();
        }
    }

    @Override
    public int getCount(){return 3;}
}
