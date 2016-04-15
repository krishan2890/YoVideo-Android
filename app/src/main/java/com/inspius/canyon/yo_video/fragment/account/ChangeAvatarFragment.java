package com.inspius.canyon.yo_video.fragment.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.listener.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Admin on 14/4/2016.
 */
public class ChangeAvatarFragment extends BaseMainFragment {
    public static final String TAG = ChangeAvatarFragment.class.getSimpleName();

    public static ChangeAvatarFragment newInstance() {
        ChangeAvatarFragment fragment = new ChangeAvatarFragment();
        return fragment;
    }
    @Bind(R.id.imageViewAvatar)
    ImageView imvAvatar;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Bitmap bitmap;
    @Override
    public int getLayout() {
        return R.layout.fragment_change_avatar;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.inspius_logo)
                .showImageForEmptyUri(R.drawable.inspius_logo)
                .showImageOnFail(R.drawable.inspius_logo)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();


    }
    @Override
    public void onInitView() {
        ImageLoader.getInstance().displayImage(mAccountDataManager.getCustomerModel().avatar, imvAvatar, options, animateFirstListener);

    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    void uploadImage(){
//        mActivityInterface.showLoading(getString(R.string.msg_updating_loading));
//        Logger.d("rrrrrr",getStringImage(bitmap));
//        mAccountDataManager.callChangeAvatar(getStringImage(bitmap), new AccountDataListener() {
//
//            @Override
//            public void onError(String message) {
//                mActivityInterface.hideLoading();
//                DialogUtil.showMessageBox(getContext(), message);
//            }
//
//            @Override
//            public void onSuccess(CustomerModel results) {
//                mActivityInterface.hideLoading();
//
//                DialogUtil.showMessageBox(getContext(), getString(R.string.msg_update_acc_success), false, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        onBackPressed();
//                    }
//                });
//            }
//        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppConstant.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.PICK_IMAGE_REQUEST && resultCode == AppConstant.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imvAvatar.setImageBitmap(bitmap);
              //  ImageLoader.getInstance().displayImage(bitmap, imvAvatar);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public String getTagText() {
        return TAG;
    }
    @OnClick(R.id.buttonChoose)
    void doClickChoose(){
        showFileChooser();
    }
    @OnClick(R.id.buttonUpload)
    void doUpload(){
        uploadImage();
    }
}
