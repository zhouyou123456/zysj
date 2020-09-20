package com.xidian.quwanba.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.xidian.quwanba.R;
import com.xidian.quwanba.app.BaseApplication;
import com.xidian.quwanba.utils.AppConfig;
import com.xidian.quwanba.utils.FileUtils;
import com.xidian.quwanba.utils.ServerConfig;
import com.xidian.quwanba.utils.StringUtil;
import com.xidian.quwanba.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity extends Activity {

    BaseApplication myapplication;
    CircleImageView headImg;
    LinearLayout backLayout ;
    TextView nickNameView,phoneView,addressView;
    RelativeLayout idcardLayout;
    private Drawable headDrawable;
    /** 本地上传 */
    private final int REQUEST_CODE_UPLOADING = 1001;
    /** 拍照上传 */
    private final int REQUEST_CODE_PHOTOGRAPH = 1002;
    /** 缩放完成 */
    private final int REQUEST_CODE_ZOOMED = 1003;
    private static final int REQUEST_CODE_GENDER = 1005;

    public final static int PARA_MY_PHOTO = 1006;

    private static final int CHOOSE_BIG_PICTURE = 1007;

    private int picDegree; // /照片的角度
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static Uri imageUri;
    private File file;

    private static final int SUCC = 1;
    private static final int ERR = 0;
    private static final int IMG_SUCC = 2;
    JSONObject  data;

    Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCC:
                    Toast.makeText(UserInfoActivity.this,"成功", Toast.LENGTH_SHORT).show();
                    break;
                case ERR:
                    Toast.makeText(UserInfoActivity.this,"失败", Toast.LENGTH_SHORT).show();
                    headImg.setImageResource(R.mipmap.user_default_head_img);
                    break;
                case IMG_SUCC:
                    String nickName = null;
                    String userMobile = null;
                    String userAddress = null;
                    try {
                        if(null != headDrawable){
                            headImg.setImageDrawable(headDrawable);
                        }
                        nickName = data.getString("nickName");
                        if(StringUtil.isNotEmpty(nickName)){
                            nickNameView.setText(nickName);
                        }
                        userMobile = data.getString("userMobile");
                        phoneView.setText(userMobile);
                        userAddress = data.getString("userAddress");
                        if(StringUtil.isNotEmpty(userAddress)){
                            addressView.setText(userAddress);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                default:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        myapplication = (BaseApplication) getApplication();

        backLayout = findViewById(R.id.layout_back);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headImg =  findViewById(R.id.head_view);
        nickNameView =  findViewById(R.id.nickName_view);
        phoneView =  findViewById(R.id.phone_view);
        addressView =  findViewById(R.id.address_view);

        nickNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this,NickNameActivity.class);
                intent.putExtra("nickName",nickNameView.getText().toString());
                startActivity(intent);
            }
        });

        idcardLayout = findViewById(R.id.idcardLayout);
        idcardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this,IDCardActivity.class);
                startActivity(intent);
            }
        });

        Button exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {

                                               myapplication.userId = 0;
                                               myapplication.nickName = "";

                                               SharedPreferences shared = getSharedPreferences("quwanba", Context.MODE_PRIVATE);
                                               SharedPreferences.Editor editor = shared.edit();
                                               editor.putInt("userId",0);
                                               editor.putString("nickName","");
                                               editor.commit();
                                               finish();
                                           }
                                       }
        );

        RelativeLayout headLayout = findViewById(R.id.headLayout);
        headLayout.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                          headLayoutClick();
                                       }
                                   }
        );
        getUserInfo();
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
                Message message = Message.obtain();
                message.what = ERR;
                handlerUI.sendMessage(message);
            }

            @Override
            public void onResponse(Call request, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        data = jsonObject.getJSONObject("data");
                        if (null != data){
                            String headImgStr = data.getString("headImg");
                            if (StringUtil.isNotEmpty(headImgStr)){
                                Bitmap bm = FileUtils.getImage(headImgStr);
                                if (null != bm){
//                                    headImg.setImageBitmap(bm);
                                    headDrawable = new BitmapDrawable(bm);
                                }
                            }
                            Message message = Message.obtain();
                            message.what = IMG_SUCC;
                            handlerUI.sendMessage(message);
                        }
                    }
                    catch(Exception e){
                         e.getMessage();
                    }
                }
            }
        });
    }


    // 在sd卡中创建一保存图片（原图和缩略图共用的）文件夹
    private File createFileIfNeed() throws IOException {
        String fileA = AppConfig.headImgSavePath;
        File fileJA = new File(fileA);
        if (!fileJA.exists()) {
            fileJA.mkdirs();
        }
        File file = new File(fileA);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    void headLayoutClick() {
        //headDrawable = headImg.getDrawable();
        SelectPhotoDialog selPhotoDlg = new SelectPhotoDialog(UserInfoActivity.this, true,
                new SelectPhotoDialog.OnSelectPhotoListener() {

                    @Override
                    public void takePhoto() {  // 拍照
                        //"点击了照相";
                        //  6.0之后动态申请权限 摄像头调取权限,SD卡写入权限
                        //判断是否拥有权限，true则动态申请
                        if (ContextCompat.checkSelfPermission(UserInfoActivity.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(UserInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(UserInfoActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_PHOTOGRAPH);
                        } else {
                            try {
                                //有权限,去打开摄像头
                                takeCamera(REQUEST_CODE_PHOTOGRAPH);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void selectLocalImg() {
                        //"点击了相册";
                        //  6.0之后动态申请权限 SD卡写入权限
                        if (ContextCompat.checkSelfPermission(UserInfoActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(UserInfoActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_UPLOADING);
                        } else{
                            choosePhoto(REQUEST_CODE_UPLOADING);
                        }
                    }
                });

        selPhotoDlg.show();
    }


    /**
     * 申请权限回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PHOTOGRAPH) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    takeCamera(REQUEST_CODE_PHOTOGRAPH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this,"拒绝了你的请求",Toast.LENGTH_SHORT).show();
                //"权限拒绝");

            }
        }
        if (requestCode == REQUEST_CODE_UPLOADING) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto(REQUEST_CODE_UPLOADING);
            } else {
                //"权限拒绝");
                Toast.makeText(this,"拒绝了你的请求",Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_EXTERNAL_STORAGE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    takeCamera(REQUEST_CODE_PHOTOGRAPH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //"权限拒绝");
                Toast.makeText(this,"拒绝了你的请求",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void takeCamera(int a) throws IOException {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(UserInfoActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(UserInfoActivity.this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }else{
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                // 获取文件
                file = createFileIfNeed("HeadImage.jpg");
                //拍照后原图回存入此路径下

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    imageUri = Uri.fromFile(file);
                } else {
                    /**
                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                     * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                     */
                    imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.xidian.quwanba.provider", file);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, a);
                startActivityForResult(intent, a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 在sd卡中创建一保存图片（原图和缩略图共用的）文件夹
    private File createFileIfNeed(String fileName) throws IOException {
//        String fileA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/qwb";
        String fileA = Environment.getExternalStorageDirectory()+ File.separator ;
        File fileJA = new File(fileA);
        if (!fileJA.exists()) {
            fileJA.mkdirs();
        }
        File file = new File(fileA, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }


    /**
     * 打开相册
     */
    private void choosePhoto(int b) {
        //这是打开系统默认的相册(就是你系统怎么分类,就怎么显示,首先展示分类列表)
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, b);
    }


    private File convertBitmapToFile(Bitmap bitmap) {
        try {
            // create a file to write bitmap data
            file = new File(UserInfoActivity.this.getCacheDir(), "portrait");
            file.createNewFile();

            // convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            // write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {

        }
        return file;
    }

    private void uploadingHeadImg(Drawable curDrawable){
        String url = ServerConfig.UOLOAD_USERINDFO;
        OkHttpClient client = new OkHttpClient();

        BitmapDrawable bd = (BitmapDrawable) curDrawable;
        Bitmap bm= bd.getBitmap();

        File file = convertBitmapToFile(bm);

        RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("guestUserId",myapplication.userId+"")  //其他信息
        .addFormDataPart("headImg", file.getName(),
                RequestBody.create(MediaType.parse("image/jpg"), file))//文件
        .build();


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                Message message = Message.obtain();
                message.what = ERR;
                handlerUI.sendMessage(message);
            }

            @Override
            public void onResponse(Call request, Response response) throws IOException {

                if (response.isSuccessful()) {
                    Message message = Message.obtain();
                    message.what = SUCC;
                    handlerUI.sendMessage(message);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED && resultCode == RESULT_OK) {
            Uri uri = null;
            switch (requestCode) {
                // 若是本地上传直接缩放
                case REQUEST_CODE_UPLOADING:
                    try {
                        startBigPhotoZoom(data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                // 若是拍照上传则建立拍照文件缩放
                case REQUEST_CODE_PHOTOGRAPH:
                    if (hasSdcard()) {
//                        File tempFile = new File(AppConfig.headImgSavePath);
//                        picDegree = readPictureDegree(AppConfig.headImgSavePath); // /照片角度
                        try {
                            startBigPhotoZoom(imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "无法存储照片！", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
                    }
                    break;
                // 缩放完成则直接设置
                case REQUEST_CODE_ZOOMED:
                    if (data != null) {
                        ///=========================
                        headImg.setImageDrawable(getImageToView(data));
                    }

                    // 修改头像
                    if ( headImg.getDrawable() != null) {
                        // 异步上传头像
                        uploadingHeadImg(headImg.getDrawable());
                    }
                    break;

                case CHOOSE_BIG_PICTURE:

                    if(imageUri != null){
                        Bitmap bitmap = decodeUriAsBitmap(imageUri);//decode bitmap
                        headImg.setImageBitmap(bitmap);
                    }

                    // 修改头像
                    if (headImg.getDrawable() != null) {
                        // 异步上传头像
                        uploadingHeadImg(headImg.getDrawable());
                    }

                    break;
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    private boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startBigPhotoZoom(Uri uri) throws IOException {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // 使图片处于可裁剪状态
        intent.putExtra("crop", "true");
        // 裁剪框的比例（根据需要显示的图片比例进行设置）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 让裁剪框支持缩放
        intent.putExtra("scale", true);
        // 裁剪后图片的大小（注意和上面的裁剪比例保持一致）
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        try {
            File file = createFileIfNeed("HeadImage.jpg");
            // 传递原图路径
            imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.xidian.quwanba.provider", file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 设置裁剪区域的形状，默认为矩形，也可设置为原形
            intent.putExtra("circleCrop", false);
            // 设置图片的输出格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // return-data=true传递的为缩略图，小米手机默认传递大图，所以会导致onActivityResult调用失败
            intent.putExtra("return-data", false);
            // 是否需要人脸识别
            intent.putExtra("noFaceDetection", true);
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            if (resInfoList.size() > 0) {
                startActivityForResult(intent, CHOOSE_BIG_PICTURE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /**
     * 保存裁剪之后的图片数据
     *
     */
    @SuppressWarnings("deprecation")
    private Drawable getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");

            Bitmap newbitmap = rotaingImageView(picDegree, photo);
            // iv.setImageBitmap(newbitmap);

            return new BitmapDrawable(newbitmap);
        } else {
            return null;
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }



    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            options.inSampleSize = 1;
            if (options.outWidth > 2000 || options.outHeight > 2000) {
                options.inSampleSize = 2;
            }
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }
        return null;
    }


    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;

    }

    @Override
    public void onResume() {
        super.onResume();
        if(myapplication.userId != 0 ){
            getUserInfo();
        }
    }

}