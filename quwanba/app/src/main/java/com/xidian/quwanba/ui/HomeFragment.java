package com.xidian.quwanba.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xidian.quwanba.MainActivity;
import com.xidian.quwanba.R;
import com.xidian.quwanba.adapter.AcGridAdapter;
import com.xidian.quwanba.adapter.Item1Adapter;
import com.xidian.quwanba.adapter.Item2Adapter;
import com.xidian.quwanba.adapter.Item3Adapter;
import com.xidian.quwanba.bean.FuncBean;
import com.xidian.quwanba.bean.RouteBean;
import com.xidian.quwanba.utils.ServerConfig;
import com.xidian.quwanba.widget.MyGridView;

//import org.json.JSONArray;
//import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int HANDLER_UI = 101;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MyGridView gview1;
    private MyGridView gview2;
    private MyGridView gview3;

    StaggeredGridView grid_view;
    AcGridAdapter mAdapter;
    Item1Adapter mItem1Adapter;
    Item2Adapter mItem2Adapter;
    Item3Adapter mItem3Adapter;

    List<FuncBean> mGridList1 = new ArrayList<FuncBean>();
    List<FuncBean> mGridList2 = new ArrayList<FuncBean>();
    List<FuncBean> mGridList3 = new ArrayList<FuncBean>();

    List<RouteBean> mList = new ArrayList<RouteBean>();
    boolean mIsLoadMore = false;
    int pageNum = 0;
    Gson mGson = new Gson();

    Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UI:
                    mAdapter.setCurList(mList);

                    break;

                default:
                    break;
            }
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        grid_view = view.findViewById(R.id.grid_view);
        View bannerview = LayoutInflater.from(getActivity()).inflate(R.layout.banner_home, null);
        grid_view.addHeaderView(bannerview);
        // 初始化适配器


        gview1 = bannerview.findViewById(R.id.gview1);
        gview2 = bannerview.findViewById(R.id.gview2);
        gview3 = bannerview.findViewById(R.id.gview3);

        FuncBean item1_1 = new FuncBean();
        item1_1.title = "火车票";
        item1_1.resId = R.mipmap.item1_1;
        mGridList1.add(item1_1);
        FuncBean item1_2 = new FuncBean();
        item1_2.title = "亲子游";
        item1_2.resId = R.mipmap.item1_2;
        mGridList1.add(item1_2);
        FuncBean item1_3 = new FuncBean();
        item1_3.title = "私人定制";
        item1_3.resId = R.mipmap.item1_3;
        mGridList1.add(item1_3);
        FuncBean item1_4 = new FuncBean();
        item1_4.title = "签证";
        item1_4.resId = R.mipmap.item1_4;
        mGridList1.add(item1_4);

        FuncBean item2_1 = new FuncBean();
        item2_1.title = "旅游";
        item2_1.resId = R.mipmap.item2_1;
        mGridList2.add(item2_1);
        FuncBean item2_2 = new FuncBean();
        item2_2.title = "酒店";
        item2_2.resId = R.mipmap.item2_2;
        mGridList2.add(item2_2);
        FuncBean item2_3 = new FuncBean();
        item2_3.title = "机票";
        item2_3.resId = R.mipmap.item2_3;
        mGridList2.add(item2_3);
        FuncBean item2_4 = new FuncBean();
        item2_4.title = "景点门票";
        item2_4.resId = R.mipmap.item2_4;
        mGridList2.add(item2_4);
        FuncBean item2_5 = new FuncBean();
        item2_5.title = "当地娱乐";
        item2_5.resId = R.mipmap.item2_5;
        mGridList2.add(item2_5);
        FuncBean item2_6 = new FuncBean();
        item2_6.title = "邮轮";
        item2_6.resId = R.mipmap.item2_6;
        mGridList2.add(item2_6);

        FuncBean item3_1 = new FuncBean();
        item3_1.title = "保险";
        item3_1.resId = R.mipmap.item3_1;
        mGridList3.add(item3_1);
        FuncBean item3_2 = new FuncBean();
        item3_2.title = "攻略";
        item3_2.resId = R.mipmap.item3_2;
        mGridList3.add(item3_2);
        FuncBean item3_3 = new FuncBean();
        item3_3.title = "充值";
        item3_3.resId = R.mipmap.item3_3;
        mGridList3.add(item3_3);
        FuncBean item3_4 = new FuncBean();
        item3_4.title = "加油卡";
        item3_4.resId = R.mipmap.item3_4;
        mGridList3.add(item3_4);

        mItem1Adapter = new Item1Adapter(getActivity(), mGridList1);
        mItem2Adapter = new Item2Adapter(getActivity(), mGridList2);
        mItem3Adapter = new Item3Adapter(getActivity(), mGridList3);

        gview1.setAdapter(mItem1Adapter);
        gview2.setAdapter(mItem2Adapter);
        gview3.setAdapter(mItem3Adapter);


        mAdapter = new AcGridAdapter(getActivity(), mList);
        //listView.setAdapter(mAdapter);

        grid_view.setAdapter(mAdapter);
        grid_view.setOnScrollListener(this);
        grid_view.setOnItemClickListener(this);
       // getRouteList();

        gview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                if(position == 0){
                    Intent intent=new Intent(getActivity(),ShowActivity.class);
                    intent.putExtra("type", 8);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(getActivity(),ErrorActivity.class);
                    startActivity(intent);
                }


            }
        });

        gview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {

                Intent intent=new Intent(getActivity(),ErrorActivity.class);
                startActivity(intent);

            }
        });

        gview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                switch (position){
                    case 0:
                        Intent intent=new Intent(getActivity(),ShowActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1=new Intent(getActivity(),ShowActivity.class);
                        intent1.putExtra("type", 2);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2=new Intent(getActivity(),ShowActivity.class);
                        intent2.putExtra("type", 3);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3=new Intent(getActivity(),ShowActivity.class);
                        intent3.putExtra("type", 4);
                        startActivity(intent3);
                        break;
                    case 4:
                    case 5:
                        Intent intent4=new Intent(getActivity(),ErrorActivity.class);
                        intent4.putExtra("type", 5);
                        startActivity(intent4);
                        break;
                }


            }
        });

        return view;
    }

    private void getRouteList(){

        String url = ServerConfig.GET_ROUTELIST;
        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("publishRoute", "1")
                .add("pageNo", pageNum+"")
                .add("pageSize", "20")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {

            }

            @Override
            public void onResponse(Call request, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    String jsonstring = "";
                    try {
                        JSONObject responseobj = JSONObject.parseObject(json);
                        JSONObject responseobj1 = responseobj.getJSONObject("data");
                        JSONArray responseobj2 = responseobj1.getJSONArray("list");
                        String strJson = responseobj2.toJSONString();

                        List<RouteBean> list = JSONObject.parseArray(strJson, RouteBean.class);
//////                        Type type = new TypeToken<List<RouteBean>>(){}.getType();
//////                        List<RouteBean> list = new Gson().fromJson(strJson,type);
                        mList.addAll(list);
                        Message message = Message.obtain();
                        message.what = HANDLER_UI;
                        handlerUI.sendMessage(message);
                        mIsLoadMore = false;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }



    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        //Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

        // our handling
        if (!mIsLoadMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {

                mIsLoadMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
        pageNum = pageNum + 1;
        getRouteList();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //String strRouteId = mList.get(position).routeId;
        if(position == 0){
            return;
        }
        Intent intent=new Intent(getActivity(), RouteDetailActivity.class);
        intent.putExtra("routeid", mList.get(position-1).getRouteId());
        startActivity(intent);
    }

}