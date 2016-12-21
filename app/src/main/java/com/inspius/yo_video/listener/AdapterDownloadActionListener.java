package com.inspius.yo_video.listener;

import com.inspius.yo_video.greendao.DBVideoDownload;

/**
 * Created by Billy on 12/1/15.
 */
public interface AdapterDownloadActionListener extends AdapterActionListener {
    void onItemLongClickListener(int position, DBVideoDownload model);
}
