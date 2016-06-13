package com.arialyy.frame.http.inf;

/**
 * 数据响应接口
 */
public interface IResponse<T> {
    /**
     * 响应的数据回调
     */
    public void onResponse(T data);

    /**
     * 错误返回回掉
     */
    public void onError(Object error);
}