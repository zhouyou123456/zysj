package com.xidian.quwanba.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.linkin.adsdk.AdSdk;
import com.xidian.quwanba.MainActivity;
import com.xidian.quwanba.R;
import com.xidian.quwanba.app.BaseApplication;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntertainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntertainFragment extends Fragment {

    BaseApplication myapplication;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EntertainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment entertain.
     */
    // TODO: Rename and change types and number of parameters
    public static EntertainFragment newInstance(String param1, String param2) {
        EntertainFragment fragment = new EntertainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myapplication = (BaseApplication) getActivity().getApplication();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entertain, container, false);



        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(myapplication.userId ==0){
//            Intent intent=new Intent(getActivity(), LoginActivity.class);
//            startActivity(intent);
//        }
//        else{
//            Intent intent=new Intent(getActivity(), OtcActivity.class);
//            startActivity(intent);
//        }
    }

}