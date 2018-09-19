package kr.co.anonymous_evcar.evcar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kr.co.anonymous_evcar.evcar.R;
import kr.co.anonymous_evcar.evcar.bus.BusProvider;
import kr.co.anonymous_evcar.evcar.event.MoveViewPager;

public class view9 extends Fragment {
    private static view9 curr = null;
    public static view9 getInstance(){
        if(curr == null){
            curr = new view9();
        }

        return  curr;
    }
    private Unbinder unbinder;


    Bus bus = BusProvider.getInstance().getBus();

    Button basic_btn_1;
    Button basic_btn_2;
    Button basic_btn_3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view9frag, container, false);
        bus.register(this);

        unbinder =  ButterKnife.bind(this,view);

        basic_btn_1 = view.findViewById(R.id.basic_btn_1);
        basic_btn_2 = view.findViewById(R.id.basic_btn_2);
        basic_btn_3 = view.findViewById(R.id.basic_btn_3);

        basic_btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveViewPager moveViewPager = new MoveViewPager(0);
                bus.post(moveViewPager);
            }
        });


        basic_btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveViewPager moveViewPager = new MoveViewPager(1);
                bus.post(moveViewPager);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        bus.unregister(this);
    }

}
