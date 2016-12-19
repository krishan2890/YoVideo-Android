package com.inspius.yo_video.modules.module_video_detail_jw;

import android.content.Context;
import android.content.Intent;

import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.model.VideoModel;

/**
 * Created by Billy on 12/19/16.
 */

public class MVideoDetailJWUtil {
    public static Intent getIntentVideoDetail(Context mContext, VideoModel videoModel, boolean isAutoPlay) {
        Intent intent = null;
        switch (videoModel.getVideoType()) {
            case YOUTUBE:
                intent = new Intent(mContext, MYoutubeVideoDetailActivity.class);
                break;

            case UPLOAD:
                intent = new Intent(mContext, MJWVideoDetailActivity.class);
                break;

            case MP3:
                intent = new Intent(mContext, MMp3DetailActivity.class);
                break;

            default:
                intent = new Intent(mContext, MWebVideoDetailActivity.class);
                break;
        }

        intent.putExtra(AppConstant.KEY_BUNDLE_AUTO_PLAY, isAutoPlay);
        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);

        return intent;
    }
}
