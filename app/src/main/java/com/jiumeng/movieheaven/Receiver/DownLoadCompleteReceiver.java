package com.jiumeng.movieheaven.Receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jiumeng.movieheaven.utils.PrefUtils;

/**
 * 手机迅雷下载完成监听
 * 下载完成后 自动启动安装界面
 * Created by Administrator on 2016/8/9 0009.
 */
public class DownLoadCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long downloadID = PrefUtils.getLong("downloadID");

        if (downloadID == myDwonloadID) {

            String serviceString = Context.DOWNLOAD_SERVICE;
            DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
            Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);

            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }

}
