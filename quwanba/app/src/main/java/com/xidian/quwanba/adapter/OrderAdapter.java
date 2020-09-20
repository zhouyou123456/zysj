package com.xidian.quwanba.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xidian.quwanba.R;
import com.xidian.quwanba.bean.FuncBean;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context mContext;
    private List<FuncBean> mList = new ArrayList<>();

    public OrderAdapter(Context context, List<FuncBean> list) {
        this.mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_cover = convertView.findViewById(R.id.iv_cover);
            viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_title.setText(mList.get(position).title);
        Glide.with(mContext)
                .load(mList.get(position).resId)
                .centerCrop()
                .into(viewHolder.iv_cover);

        return convertView;
    }

    class ViewHolder {
        ImageView iv_cover;
        TextView tv_title;
    }
}

