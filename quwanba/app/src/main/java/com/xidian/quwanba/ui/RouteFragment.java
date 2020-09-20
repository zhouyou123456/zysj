package com.xidian.quwanba.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etsy.android.grid.StaggeredGridView;
import com.xidian.quwanba.R;
import com.xidian.quwanba.adapter.AcGridAdapter;
import com.xidian.quwanba.bean.RouteBean;
import com.xidian.quwanba.utils.ServerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteFragment extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    StaggeredGridView grid_view;
    AcGridAdapter mAdapter;
    List<RouteBean> mList = new ArrayList<RouteBean>();
    boolean mIsLoadMore = false;
    int pageNum = 0;
    private static final int HANDLER_UI = 101;

    Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UI:
                    mAdapter.setCurList(mList);
                    //mAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };

    public RouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RouteFragment newInstance(String param1, String param2) {
        RouteFragment fragment = new RouteFragment();
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
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        grid_view = view.findViewById(R.id.grid_view);
        mAdapter = new AcGridAdapter(getActivity(), mList);

        grid_view.setAdapter(mAdapter);
        grid_view.setOnScrollListener(this);
        grid_view.setOnItemClickListener(this);

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