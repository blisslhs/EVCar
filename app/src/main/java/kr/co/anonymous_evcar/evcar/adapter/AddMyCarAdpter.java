package kr.co.anonymous_evcar.evcar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.anonymous_evcar.evcar.R;
import kr.co.anonymous_evcar.evcar.data.MakingCategory;

public class AddMyCarAdpter  extends BaseAdapter {
    ArrayList<MakingCategory> items;
    public AddMyCarAdpter(ArrayList<MakingCategory> items){
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.addmycar_item, parent, false);
            holder.item_img = convertView.findViewById(R.id.item_img);
            holder.item_txt = convertView.findViewById(R.id.item_txt);
            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }
        MakingCategory item = (MakingCategory)getItem(position);
        holder.item_img.setBackgroundResource(item.getMakingiamge());
        holder.item_txt.setText(item.getMakingname());
        return convertView;
    }

    public class Holder{
        ImageView item_img;
        TextView item_txt;
    }
}
