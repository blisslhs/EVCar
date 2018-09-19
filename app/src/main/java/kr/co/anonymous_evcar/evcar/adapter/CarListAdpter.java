package kr.co.anonymous_evcar.evcar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.anonymous_evcar.evcar.R;
import kr.co.anonymous_evcar.evcar.data.CarsData;

public class CarListAdpter extends BaseAdapter{
    ArrayList<CarsData> carsData;
    public CarListAdpter(ArrayList<CarsData>carsData){
        this.carsData = carsData;
    }

    @Override
    public int getCount() {
        return carsData.size();
    }

    @Override
    public Object getItem(int position) {
        return carsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carslistitem, parent, false);
            holder.carslistitem_category = convertView.findViewById(R.id.carslistitem_category);
            holder.carslistitem_image = convertView.findViewById(R.id.carslistitem_image);
            holder.carslistitem_mycarname = convertView.findViewById(R.id.carslistitem_mycarname);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }
        CarsData item = (CarsData)getItem(position);

        holder.carslistitem_image.setBackgroundResource(item.getImage());
        holder.carslistitem_mycarname.setText(item.getMycarname());
        if(item.getMakingcategory() == 1){
            holder.carslistitem_category.setText("기아");
        }else if (item.getMakingcategory() == 2){
            holder.carslistitem_category.setText("삼성");
        }else if (item.getMakingcategory() == 3){
            holder.carslistitem_category.setText("한국GM");
        }else if (item.getMakingcategory() == 4){
            holder.carslistitem_category.setText("BMW");
        }else if (item.getMakingcategory() == 5){
            holder.carslistitem_category.setText("닛산");
        }else if (item.getMakingcategory() == 6){
            holder.carslistitem_category.setText("현대");
        } else if (item.getMakingcategory() == 7) {
            holder.carslistitem_category.setText("혼다");
        }else if (item.getMakingcategory() == 8) {
            holder.carslistitem_category.setText("토요타");
        }else if (item.getMakingcategory() == 9) {
            holder.carslistitem_category.setText("링컨");
        }else if (item.getMakingcategory() == 10){
            holder.carslistitem_category.setText("포드");
        }else if (item.getMakingcategory() == 11){
            holder.carslistitem_category.setText("벤츠");
        }else if (item.getMakingcategory() == 12){
            holder.carslistitem_category.setText("포르쉐");
        }else if (item.getMakingcategory() == 13){
            holder.carslistitem_category.setText("테슬라");
        }else if (item.getMakingcategory() == 14){
            holder.carslistitem_category.setText("렉서스");
        }

        return convertView;
    }

    public  class  Holder{
        ImageView carslistitem_image;
        TextView carslistitem_category;
        TextView carslistitem_mycarname;
    }
}
