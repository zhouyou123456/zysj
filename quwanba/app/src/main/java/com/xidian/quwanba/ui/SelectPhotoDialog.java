package com.xidian.quwanba.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import com.xidian.quwanba.R;
import com.xidian.quwanba.utils.AppConfig;

/**
 * 
 * @author yd
 * @version 1.0
 * @date 2014-09-27
 * @Description 先择本地照片/拍照的对话框
 * 
 */

public class SelectPhotoDialog extends Dialog
{	
    //定义回调事件接口
    public interface OnSelectPhotoListener{
    	public void takePhoto();       ///拍照
    	public void selectLocalImg();  ///先择本地照片
    }
    
    private OnSelectPhotoListener mSelectPhotoListener;
    private boolean mShowSelectImgBtn;
    private Button takePhotoBtn, selectLocalImgBtn, cancelBtn;
    
    public SelectPhotoDialog(Context context, boolean showSelectImgBtn, OnSelectPhotoListener selectPhotoListener) {
    	super(context, R.style.round_dialog);             ///圆角窗体   	
        requestWindowFeature(Window.FEATURE_NO_TITLE);    ///无标题栏
        setCanceledOnTouchOutside(true);                  ///点击窗口外部消失
        
        Window window = this.getWindow();  
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置  
        

        mShowSelectImgBtn = showSelectImgBtn;
        ///回调函数
        mSelectPhotoListener = selectPhotoListener;       
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.select_photo_dialog);
        
        if (AppConfig.phoneWidth > 500) {
        	LayoutParams layoutParams = getWindow().getAttributes();
    		layoutParams.width = (int) (AppConfig.phoneWidth*0.99);
    		getWindow().setAttributes(layoutParams);
		}
		
        initViews();
    }
    
    private View.OnClickListener clickListener = new View.OnClickListener() 
    {
        @Override
        public void onClick(View v) 
        {
        	switch (v.getId())
        	{
        	case R.id.takePhotoBtn:
        		mSelectPhotoListener.takePhoto();
            	SelectPhotoDialog.this.dismiss();
        		break;
        		
        	case R.id.selectLocalImgBtn:
        		mSelectPhotoListener.selectLocalImg();
            	SelectPhotoDialog.this.dismiss();
        		break;        		
        		
        	case R.id.cancelBtn:
            	SelectPhotoDialog.this.dismiss();
            	break;
        	default:
        		break;
        	}                   
        }
    };
    
    
	// 初始化控件
	private void initViews() 
	{      
        ///拍照
		takePhotoBtn = (Button) findViewById(R.id.takePhotoBtn);
		takePhotoBtn.setOnClickListener(clickListener);
				
		///选择本地照片
		selectLocalImgBtn = (Button) findViewById(R.id.selectLocalImgBtn);
		selectLocalImgBtn.setOnClickListener(clickListener);
		
		if (!mShowSelectImgBtn){
			selectLocalImgBtn.setVisibility(View.GONE);			
		}
        
        ///取消
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(clickListener);
        
 	}
}
