package com.xidian.quwanba.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.xidian.quwanba.R;

import com.xidian.quwanba.app.BaseApplication;
import com.xidian.quwanba.bean.RouteBean;
import com.xidian.quwanba.ui.RouteActivity;
import com.xidian.quwanba.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AcGridAdapter extends BaseAdapter {

    RouteBean pnm = new RouteBean();
    private List<RouteBean> mActLst = new ArrayList<RouteBean>();

    private Context mContext;


    private boolean bNetState = true; // /网络状态

    private boolean mFirst = true;   ///初始化

    public AcGridAdapter(Context context, List<RouteBean> list) {
        if(list != null && list.size() > 0){
            mActLst.addAll(list);
        }

        mContext = context;
    }

    public void setCurList( List<RouteBean> list){

        if(list != null && list.size() > 0){
            mActLst.clear();
            mActLst.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (mActLst != null && mActLst.size() > 0){
            return mActLst.size();
        } else if (mFirst){
            return 0;
        } else {
            return 1;     ///发布ITEM
        }
    }

    @Override
    public Object getItem(int position) {
        if (mActLst != null && mActLst.size() > 0){
            return mActLst.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
     * 控件
     */
    private class ViewHolder {
        // 网络状态条
        LinearLayout netStateBarLayout;

        //活动列表ITEM
        LinearLayout actLayout;

        // 头像
        ImageView headImg;
        // 性别
        ImageView genderImg;
        // 用户名
        TextView nickNameTxt;
        // 认证
        TextView authTxt;
        // 时间
        TextView pubTimeTxt;

        ///
        TextView actTittleTxt;


        // 活动封面图片
        ImageView actCoverImg;

        ///+++活动参加者
        TextView joinTxt;

        ///+++活动花费
        TextView costTxt;


        // 随记内容
        TextView actDescTxt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final AcGridAdapter.ViewHolder viewHolder;

        if (convertView == null || convertView.getTag() == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.act_grid_item,	null);

            viewHolder = new AcGridAdapter.ViewHolder();

            viewHolder.netStateBarLayout = (LinearLayout) convertView.findViewById(R.id.netStateBarLayout); // /网络状态
            ///活动布局
            viewHolder.actLayout = (LinearLayout) convertView.findViewById(R.id.actLayout);
            viewHolder.headImg      = (ImageView) convertView.findViewById(R.id.headImg);
            viewHolder.genderImg    = (ImageView) convertView.findViewById(R.id.genderImg);
            viewHolder.nickNameTxt  = (TextView) convertView.findViewById(R.id.nickNameTxt);
           // viewHolder.authTxt      = (TextView) convertView.findViewById(R.id.authTxt);
            viewHolder.pubTimeTxt   = (TextView) convertView.findViewById(R.id.pubTimeTxt);
            viewHolder.actTittleTxt = (TextView) convertView.findViewById(R.id.actTittleTxt);
            viewHolder.actCoverImg  = (ImageView) convertView.findViewById(R.id.actCoverImg);

            viewHolder.joinTxt      = (TextView) convertView.findViewById(R.id.joinTxt);
            viewHolder.costTxt      = (TextView) convertView.findViewById(R.id.costTxt);

           // viewHolder.actDescTxt = (TextView) convertView.findViewById(R.id.actDescTxt);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AcGridAdapter.ViewHolder) convertView.getTag();
        }

        // /显示网络状态不可用
        viewHolder.netStateBarLayout.setVisibility(View.GONE);
        viewHolder.actLayout.setVisibility(View.VISIBLE);

        if (0 == position && !bNetState) {
            viewHolder.netStateBarLayout.setVisibility(View.VISIBLE);
            viewHolder.netStateBarLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent wifiSettingsIntent = new Intent(Settings.ACTION_SETTINGS);
                    mContext.startActivity(wifiSettingsIntent);
                }

            });
        }

        ///无数据
        if (null == mActLst || 0 == mActLst.size()){
            viewHolder.actLayout.setVisibility(View.GONE);

        }


        ///有数据
        if (mActLst != null && mActLst.size() > 0){
            final RouteBean act = mActLst.get(position);

            viewHolder.actLayout.setTag(act);
            ///点击查看活动详情
//            viewHolder.actLayout.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    RouteBean tmpAct = (RouteBean)v.getTag();
//
//                    Intent intent = new Intent(mContext, RouteActivity.class);
//
//                    intent.putExtra("detail", (Parcelable) tmpAct);
//                    mContext.startActivity(intent);
////                    if (BaseApplication.isNetConnect()) {
////
////                    } else {
////                        Toast.makeText(mContext, "当前网络不可用，请检查网络连接", Toast.LENGTH_SHORT).show();
////                    }
//                }
//            });

            // 头像
            if(act.getUser() == null){
                viewHolder.nickNameTxt.setVisibility(View.GONE);
                viewHolder.headImg.setVisibility(View.GONE);
            }
            else{
                viewHolder.nickNameTxt.setVisibility(View.VISIBLE);
                viewHolder.headImg.setVisibility(View.VISIBLE);
                if (StringUtil.isNotEmpty(act.getUser().getImage())) {
                    Glide.with(mContext)
                            .load(act.getUser().getImage())
                            .centerCrop()
                            //.placeholder(R.drawable.loading_spinner)
                            .into(viewHolder.headImg);
                }

                // 昵称
                viewHolder.nickNameTxt.setText(act.getUser().getBannerName());
            }





//            // 时间
//            String strPubTime = DateTimeUtil.gePublishTime(act.pubTime);
//            viewHolder.pubTimeTxt.setText(strPubTime );

            ///活动标题
            viewHolder.actTittleTxt.setText("");
            if (StringUtil.isNotEmpty(act.title)){
                viewHolder.actTittleTxt.setText(act.title);
            }

//            // 内容
//            viewHolder.actDescTxt.setText("");
//            viewHolder.actDescTxt.setVisibility(View.GONE);
//            if (StringUtil.isNotEmpty(act.desc)) {
//                viewHolder.actDescTxt.setText(act.desc);
//                viewHolder.actDescTxt.setVisibility(View.VISIBLE);
//            }


//            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) viewHolder.actCoverImg.getLayoutParams();
//            linearParams.width = AppConfig.phoneWidth - Dp2Px(mContext,(38+5+13+45));
//            linearParams.height = linearParams.width;
//            viewHolder.actCoverImg.setLayoutParams(linearParams);


            ///点击封面，查看大图
               Glide.with(mContext)
                    .load(act.imagePath)

                    //.placeholder(R.drawable.loading_spinner)
                    .into(viewHolder.actCoverImg);

            ///活动参加者
           // viewHolder.joinTxt.setText("参加 " + act.joinCount);

            ///活动费用
//            if (0 == act.avgCost){
//                viewHolder.costTxt.setText("费用 " + "AA制");
//            } else {
//                viewHolder.costTxt.setText("费用 " + act.avgCost);
//            }

        }

        return convertView;
    }

    public void addItem(RouteBean model) {
        mFirst = false;
        mActLst.add(model);
        notifyDataSetChanged();
    }

//	public void clear() {
//		this.mActLst.clear();
//		notifyDataSetChanged();
//	}

    public void refreshData(List<RouteBean> actLst) {
        mFirst = false;
        this.mActLst = actLst;
        notifyDataSetChanged();
    }



    // /插入网络状态指示条
    public void insertNetStateBar(boolean bConnect) {
        bNetState = bConnect;
        notifyDataSetChanged();
    }

    // /插入网络状态指示条
    public void showGotoPubLayout() {
        notifyDataSetChanged();
    }

    //定义一个函数将dp转换为像素
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    // /插入网络状态指示条
    public void setFirst(boolean bFirst) {
        mFirst = bFirst;
        notifyDataSetChanged();
    }

}