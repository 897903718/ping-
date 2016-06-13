package com.arialyy.frame.http.inf;

/**
 * 网络请求类
 */
public interface IRequest<T> {
    /**
     * post请求
     *
     * @param url      请求连接
     * @param param    请求参数
     * @param response 回调接口
     */
    public void post(String url, T param, IResponse response);

    /**
     * get请求
     *
     * @param url      请求连接
     * @param response 回调接口
     * @param param    请求参数
     */
    public void get(String url, T param, IResponse response);

    /**
     * 默认缓存文件夹
     */
    public static final String NET_CACHE_DIR = "HttpCache";
}