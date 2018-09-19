package kr.co.anonymous_evcar.evcar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kr.co.anonymous_evcar.evcar.fragment.view1;
import kr.co.anonymous_evcar.evcar.fragment.view2;
import kr.co.anonymous_evcar.evcar.fragment.view3;

public class fragmentAdpter extends FragmentStatePagerAdapter {
    public fragmentAdpter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0){
            return view1.getInstance();
        }else if(position == 1){
            return view2.getInstance();
        }else {
            return view3.getInstance();
        }
    }

    @Override
    public int getCount(){return 3;}
}
