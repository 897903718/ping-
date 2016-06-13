package com.arialyy.frame.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arialyy.frame.cache.CacheUtil;
import com.arialyy.frame.http.inf.IRequest;
import com.arialyy.frame.http.inf.IResponse;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.arialyy.frame.http.inf.IDownloadListener;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.FileUtils;
import com.arialyy.frame.util.MyLog;
import com.arialyy.frame.util.SharedPreferData;
import com.arialyy.frame.util.StreamUtil;
import com.arialyy.frame.util.show.FL;
import com.arialyy.frame.util.show.L;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * Created by AriaLyy on 2015/4/9.
 * 网络请求工具类
 */
public class HttpUtil implements IRequest<Map<String, String>> {
    private static final String TAG = "HttpUtil";
    private RequestQueue mRequestQueue;
    private Request<String> request;
    private CacheUtil mCacheUtil;
    private DownloadSetting mDownloadSetting;
    private Context mContext;
    public HttpUtil(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mDownloadSetting = new DownloadSetting();
        mCacheUtil = new CacheUtil(context, true);
        this.mContext=context;
    }

    public HttpUtil(Context context, CacheUtil cacheUtil) {
        mRequestQueue = Volley.newRequestQueue(context);
        mCacheUtil = cacheUtil;
        mDownloadSetting = new DownloadSetting();
        this.mContext=context;

    }

    /**
     * 获取下载设置
     *
     * @return
     */
    public DownloadSetting getDownloadSetting() {
        return mDownloadSetting;
    }

    @Override
    public void post(final String url, final Map<String, String> param, final IResponse response) {
        if (response == null) {
            throw new NullPointerException("没有设置IResponse回调监听");
        }
        request = new StringRequest(Request.Method.POST, url, new Listener<String>() {
            @Override
            public void onResponse(String s) {
                L.i(TAG, "服务器返回数据【" + url + "】:" + s);

                response.onResponse(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.e(TAG, "请求服务器失败【" + url + "】:" + FL.getMapString(param), volleyError);
                response.onError(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                L.i(TAG, "客户端请求参数【" + url + "】:" + FL.getMapString(param));
//                if(url.contains("getShareData")){
//                    MyLog.i("post不带缓存客户端请求参数【" + url +"】:",FL.getMapString(param));
//                }
                return param;
            }
        };
        mRequestQueue.add(request);
    }

    /**
     * 带缓存的Post
     *
     * @param url
     * @param response
     */
    public void postFromCanche(final String url, final Map<String, String> param, final IResponse response) {
        if (response == null) {
            throw new NullPointerException("没有设置IResponse回调监听");
        }
        if (mCacheUtil == null) {
            throw new NullPointerException("没有初始化缓存工具");
        }
        request = new StringRequest(Request.Method.POST, url, new Listener<String>() {
            @Override
            public void onResponse(String s) {
                L.i(TAG, "服务器返回数据【" + url + "】:" + s);
                SharedPreferData.writeStringdata(mContext,url,s);
                //2015-7-24更改缓存写入
//                mCacheUtil.putStringCache(url, s);
//                mCacheUtil.flush();
                response.onResponse(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //2015-7-24更改缓存读取
//                String data = mCacheUtil.getStringCacche(url);
                String data =SharedPreferData.readString(mContext,url);
                L.e(TAG, "请求服务器失败__从缓存中获取数据【" + url + "】:" + FL.getMapString(param), volleyError);
                L.i(TAG, "缓存数据[" + data + "]");
                if (TextUtils.isEmpty(data)) {
                    response.onError(volleyError);
                } else {
                    response.onResponse(data);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                L.i(TAG, "客户端请求参数【" + url + "】:" + FL.getMapString(param));
//                if(url.contains("getShareData")){
//                    MyLog.i("post带缓存客户端请求参数【" + url +"】:",FL.getMapString(param));
//                }

                return param;
            }
        };
        mRequestQueue.add(request);
    }

    /**
     * get 请求
     *
     * @param url      请求连接
     * @param response 回调接口
     */
    @Override
    public void get(String url, final Map<String, String> params, final IResponse response) {
        if (response == null) {
            throw new NullPointerException("没有设置IResponse回调监听");
        }
        if(params != null && params.size() != 0){
            Set set = params.entrySet();
            int i = 0;
            for (Object aSet : set) {
                i++;
                Map.Entry entry = (Map.Entry) aSet;
                url += entry.getKey() + "=" + entry.getValue() + (i < params.size() ? "&" : "");
            }
        }
        final String url1 = url;
        request = new StringRequest(url1, new Listener<String>() {
            @Override
            public void onResponse(String s) {
                L.i(TAG, "服务器返回数据【" + url1 + "】:" + s);
                response.onResponse(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.e(TAG, "请求服务器失败【" + url1 + "】", volleyError);
                response.onError(volleyError);
            }
        });
        mRequestQueue.add(request);
    }

    /**
     * 带缓存的get方法
     */
    public void getFormCache(String url, final Map<String, String> params, final IResponse response) {
        if (response == null) {
            throw new NullPointerException("没有设置IResponse回调监听");
        }
        if (mCacheUtil == null) {
            throw new NullPointerException("没有初始化缓存工具");
        }
        if(params != null && params.size() != 0){
            Set set = params.entrySet();
            int i = 0;
            for (Object aSet : set) {
                i++;
                Map.Entry entry = (Map.Entry) aSet;
                url += entry.getKey() + "=" + entry.getValue() + (i < params.size() ? "&" : "");
            }
        }
        final String url1 = url;
        request = new StringRequest(url, new Listener<String>() {
            @Override
            public void onResponse(String s) {
                L.i(TAG, "服务器返回数据【" + url1 + "】:" + s);
                mCacheUtil.putStringCache(url1, s);
                mCacheUtil.flush();
                response.onResponse(s);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String data = mCacheUtil.getStringCacche(url1);
                L.e(TAG, "请求服务器失败__从缓存中获取数据【" + url1 + "】", volleyError);
                L.i(TAG, "缓存数据[key:" + data + "]");
                if (TextUtils.isEmpty(data)) {
                    response.onError(volleyError);
                } else {
                    response.onResponse(data);
                }
            }
        });
        mRequestQueue.add(request);
    }


    public Request<String> getRequest() {
        return request;
    }

    /**
     * 多线程断点续传下载文件，暂停和继续
     *
     * @param context          必须添加该参数，不能使用全局变量的context
     * @param downloadUrl      下载路径
     * @param filePath         保存路径
     * @param downloadListener 下载进度监听 {@link com.arialyy.frame.http.HttpUtil.DownloadListener}
     * @see DownloadSetting
     */
    public void download(final Context context, @NonNull final String downloadUrl, @NonNull final String filePath,
                         final DownloadListener downloadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(downloadUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置请求的相关参数
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        int length = conn.getContentLength();
//                        File file1 = new File(filePath);
//                        if (file1.exists()) {
//                            file1.delete();
//                        }
                        //必须建一个文件
                        FileUtils.createFile(filePath);
                        RandomAccessFile file = new RandomAccessFile(filePath, "rwd");
                        //设置文件长度
                        file.setLength(length);
                        mDownloadSetting.DListener = downloadListener;
                        if(downloadListener != null){
                            downloadListener.onFileSize(length);
                            mDownloadSetting.fileSize = length;
                        }
                        //建立3条线程下载数据
                        int threadNO = 3;
                        //每条线程下载数据的大小
                        int blockSize = length / threadNO;
                        for (int i = 0; i < threadNO; i++) {
                            int startPosition = i * blockSize;//每条线程下载的开始位置
                            int endPosition = (i + 1) * blockSize;//每条线程下载的结束位置
                            if (i == (threadNO - 1)) {
                                endPosition = length;//如果整个文件的大小不为线程个数的整数倍，则最后一个线程的结束位置即为文件的总长度
                            }
                            if (context != null) {
                                DownLoadTask task = new DownLoadTask(context, i, downloadUrl, filePath, startPosition, endPosition);
                                new Thread(task).start();
                            }
                        }
                    }
                } catch (IOException e) {
                    FL.e(this, "下载失败【downloadUrl:" + downloadUrl + "】\n【filePath:" + filePath + "】" + FL.getPrintException(e));
                }
            }
        }).start();
    }

    /**
     * 配置下载设置
     */
    public class DownloadSetting {
        //下载大小
        int currentSize = 0;
        //下载监听
        IDownloadListener DListener;
        boolean pause = false;
        //文件大小
        int fileSize = 0;
        //下载完成标志，这里是3条线程
        int threadFlag = 0;

        /**
         * 设置暂停
         */
        public void setPause(boolean pause) {
            this.pause = pause;
        }
    }

    /**
     * 多线程下载任务类
     */
    private class DownLoadTask implements Runnable {
        private static final String TAG = "DownLoadTask";
        int threadId;
        String downLoadUrl;
        //线程开始的下载的位置
        int startPosition;
        //线程结束的位置
        int endPosition;
        //文件保存路径
        String filePath;
        Context context;

        public DownLoadTask(Context context, int threadId, String downLoadUrl, String filePath, int startPosition, int endPosition) {
            this.threadId = threadId;
            this.downLoadUrl = downLoadUrl;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.filePath = filePath;
            if (TextUtils.isEmpty(filePath)) {
                this.filePath = AndroidUtils.getDiskCacheDirName(context) + File.separator + "download" + File.separator + "downloadFile";
            }
            this.context = context;
        }

        @Override
        public void run() {
            try {
                final File downloadFile = new File(filePath);
                final File positionFile = new File(downloadFile.getParent() + File.separator + downloadFile.getName() + threadId + ".downloadCache");
                //必须建一个文件
                FileUtils.createFile(positionFile.getPath());
                URL url = new URL(downLoadUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                L.d("线程" + threadId + "正在下载【" + "开始位置 : " + startPosition + "，结束位置：" + endPosition + "】");
                FileInputStream fileInputStream = new FileInputStream(positionFile);
                //写入每条线程的下载信息
                byte[] data = StreamUtil.readStream(fileInputStream);
                if (data != null) {
                    String str = new String(data);
                    //如果postionFile中有记录，则改变开始下载的位置
                    int newStartPosition = TextUtils.isEmpty(str) ? 0 : Integer.parseInt(str);
                    if (newStartPosition > startPosition) {
                        startPosition = newStartPosition;
                    }
                }
                if(mDownloadSetting.DListener != null){
                    mDownloadSetting.DListener.onStart(startPosition);
                }
                //在头里面请求下载开始位置和结束位置
                conn.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                InputStream is = conn.getInputStream();
                RandomAccessFile file = new RandomAccessFile(filePath, "rwd");
                //设置每条线程写入文件的位置
                file.seek(startPosition);
                byte[] buffer = new byte[1024];
                int len = 0;
                int currentPosition = startPosition;
                while ((len = is.read(buffer)) != -1) {
                    if (mDownloadSetting.pause) {
                        L.d(TAG, "pause=========");
                        if(mDownloadSetting.DListener != null){
                            mDownloadSetting.DListener.onPause(mDownloadSetting.currentSize);
                        }
                        return;
                    }
                    //把下载数据数据写入文件
                    file.write(buffer, 0, len);
                    synchronized (HttpUtil.this) {
                        mDownloadSetting.currentSize += len;
                        if (mDownloadSetting.DListener != null) {
                            mDownloadSetting.DListener.onProgress(mDownloadSetting.currentSize);
                        }
                    }
                    //记录当前线程下载的信息
                    currentPosition += len;
                    String position = currentPosition + "";
                    FileOutputStream out = new FileOutputStream(positionFile);
                    //把记录写入文件
                    out.write(position.getBytes());
                    out.flush();
                    out.close();
                }
                file.close();
                fileInputStream.close();
                is.close();
                L.i(TAG, "线程【" + threadId + "】下载完毕");
                if (positionFile.exists()) {
                    positionFile.delete();
                }
                if(mDownloadSetting.DListener != null){
                    mDownloadSetting.DListener.onFinish(mDownloadSetting.currentSize);
                }
                mDownloadSetting.threadFlag ++;
                if(mDownloadSetting.DListener != null &&  mDownloadSetting.threadFlag == 3){
                    mDownloadSetting.DListener.onComplete();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                FL.e(this, "下载失败【" + downLoadUrl + "】" + FL.getPrintException(e));
            } catch (Exception e) {
                FL.e(this, "获取流失败" + FL.getPrintException(e));
            }
        }

    }

    /**
     * 下载监听
     */
    public static class DownloadListener implements IDownloadListener {

        @Override
        public void onProgress(int currentPosition) {

        }

        @Override
        public void onFileSize(int fileSize) {

        }

        @Override
        public void onFinish(int finishPosition) {

        }

        @Override
        public void onStart(int startPosition) {

        }

        @Override
        public void onPause(int pausePosition) {

        }

        @Override
        public void onStop(int stopPosition) {

        }

        @Override
        public void onComplete() {

        }
    }

    /**
     * 返回String类型的响应
     */
    public static class Response implements IResponse<String> {

        @Override
        public void onResponse(String data) {

        }

        @Override
        public void onError(Object error) {

        }
    }
}
