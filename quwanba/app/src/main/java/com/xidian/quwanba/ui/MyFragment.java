package com.xidian.quwanba.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.xidian.quwanba.R;
import com.xidian.quwanba.adapter.OrderAdapter;
import com.xidian.quwanba.app.BaseApplication;
import com.xidian.quwanba.bean.FuncBean;
import com.xidian.quwanba.utils.FileUtils;
import com.xidian.quwanba.utils.ServerConfig;
import com.xidian.quwanba.utils.StringUtil;
import com.xidian.quwanba.widget.MyGridView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView text_tip;
    ImageView headImgIv ;

    private MyGridView gview;
    List<FuncBean> mGridList = new ArrayList<FuncBean>();
    OrderAdapter mItem1Adapter;
    BaseApplication myapplication;



    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        gview = view.findViewById(R.id.gview);
        FuncBean item1 = new FuncBean();
        item1.title = "待付款";
        item1.resId = R.mipmap.order1;
        mGridList.add(item1);
        FuncBean item2 = new FuncBean();
        item2.title = "处理中";
        item2.resId = R.mipmap.order2;
        mGridList.add(item2);
        FuncBean item3 = new FuncBean();
        item3.title = "未出行";
        item3.resId = R.mipmap.order3;
        mGridList.add(item3);
        FuncBean item4 = new FuncBean();
        item4.title = "待评价";
        item4.resId = R.mipmap.order4;
        mGridList.add(item4);
        FuncBean item5 = new FuncBean();
        item5.title = "售后";
        item5.resId = R.mipmap.order5;
        mGridList.add(item5);
        mItem1Adapter = new OrderAdapter(getActivity(), mGridList);
        gview.setAdapter(mItem1Adapter);

        LinearLayout headLayout = view.findViewById(R.id.headLayout);
        headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                if(myapplication.userId ==0) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
              }
           }
        );
        text_tip = view.findViewById(R.id.nameTxt);
        myapplication = (BaseApplication) getActivity().getApplication();

        headImgIv = view.findViewById(R.id.headImg);

        RelativeLayout inviteLayout = view.findViewById(R.id.inviteLayout);

        inviteLayout.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              if(myapplication.userId ==0) {
                                                  startActivity(new Intent(getActivity(), LoginActivity.class));
                                              }
                                              else{
                                                  Intent intent=new Intent(getActivity(), InviteActivity.class);
                                                  startActivity(intent);
                                              }

                                          }
                                      }
        );

        RelativeLayout fundLayout = view.findViewById(R.id.fundLayout);

        fundLayout.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                         }
                                     }
        );

        ImageView iv_setting = view.findViewById(R.id.iv_setting);
        iv_setting.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              if(myapplication.userId ==0) {
                                                  startActivity(new Intent(getActivity(), LoginActivity.class));
                                              }
                                              else{
                                                  Intent intent=new Intent(getActivity(), UserInfoActivity.class);
                                                  startActivity(intent);
                                              }

                                          }
                                      }
        );

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                if(myapplication.userId ==0) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                else{
                    Intent intent=new Intent(getActivity(),ErrorActivity.class);
                    startActivity(intent);
                }


            }
        });

        getUserInfo();
        return view;
    }

    public void getUserInfo(){
        String url = ServerConfig.USER_INFO + "?guestUserId=" +myapplication.userId;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {

            }

            @Override
            public void onResponse(Call request, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject  data = jsonObject.getJSONObject("data");
                        if (null != data){
                            String headImgStr = data.getString("headImg");
                            String nickName = data.getString("nickName");
                            text_tip.setText(nickName);
                            if (StringUtil.isNotEmpty(headImgStr)){
                                Bitmap bm = FileUtils.getImage(headImgStr);
                                if (null != bm){
                                    headImgIv.setImageBitmap(bm);
                                }

                            }
                        }
                    }
                    catch(Exception e){

                    }


                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(myapplication.userId != 0 ){
            getUserInfo();
        }
        else{
            if(text_tip !=null){
                text_tip.setText("点击登录");
            }

        }
    }
}