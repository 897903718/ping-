package chanlytech.paydemo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pingplusplus.android.PaymentActivity;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONObject;


import java.lang.reflect.Proxy;

import chanlytech.paydemo.R;
import chanlytech.paydemo.base.BaseActivity;
import chanlytech.paydemo.http.LoadDatahandler;
import chanlytech.paydemo.http.MyAsyncHttpResponseHandler;
import chanlytech.paydemo.http.MyHttpClient;
import chanlytech.paydemo.model.HttpMoldel;

public class MainActivity extends BaseActivity<HttpMoldel> implements View.OnClickListener {
    private Button wechatButton;
    private Button alipayButton;
    private Button upmpButton;
    private Button bfbButton;
    private Button jdpayButton;

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";
    private static  String CHANNEL;

    private static  String URL = "http://10.6.0.235/pingpp-php-master/test.php?channel=bfb&amount=100&type=live";
    private static final int REQUEST_CODE_PAYMENT = 1;
    /**
     * 请求队列
     */
    private RequestQueue requestQueue;
    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
     */
    public static final int NOHTTP_WHAT_TEST = 0x001;


    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        wechatButton = (Button) findViewById(R.id.wechatButton);
        alipayButton = (Button) findViewById(R.id.alipayButton);
        upmpButton = (Button) findViewById(R.id.upmpButton);
        bfbButton = (Button) findViewById(R.id.bfbButton);
        jdpayButton = (Button) findViewById(R.id.jdpayButton);
        wechatButton.setOnClickListener(MainActivity.this);
        alipayButton.setOnClickListener(MainActivity.this);
        upmpButton.setOnClickListener(MainActivity.this);
        bfbButton.setOnClickListener(MainActivity.this);
        jdpayButton.setOnClickListener(MainActivity.this);
        requestQueue=NoHttp.newRequestQueue();
    }

    @Override
    public HttpMoldel initModule() {
        return new HttpMoldel(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alipayButton://支付宝
                CHANNEL=CHANNEL_ALIPAY;
                    //http://10.6.0.235/pingpp-php-master/test.php?channel=alipay&amount=100&type=live
                URL = "http://10.6.0.235/pingpp-php-master/test.php?channel=alipay&amount=1&type=live";
//                noHttp(URL);
//                String name="完美生活";
//                URL="http://so.ard.iyyin.com/s/song_with_out?q="+name+"&page=1&size=1";
//                tEST(URL);
//                getModule().getData(URL);
                getChanger(URL);
//                noHttp(URL);
//                new PaymentTask.
                break;
            case R.id.wechatButton://微信
                CHANNEL=CHANNEL_WECHAT;
                URL = "http://10.6.0.235/pingpp-php-master/test.php?channel=wx&amount=100&type=live";
//                getChanger(URL);
                noHttp(URL);
                break;
            case R.id.upmpButton://银联
                CHANNEL=CHANNEL_UPACP;
                URL = "http://10.6.0.235/pingpp-php-master/test.php?channel=upacp&amount=100&type=live";
                getChanger(URL);
                break;
            case R.id.bfbButton://百度钱包
                CHANNEL=CHANNEL_BFB;
                URL = "http://10.6.0.235/pingpp-php-master/test.php?channel=bfb&amount=100&type=live";
                getChanger(URL);
                break;
            case R.id.jdpayButton://京东
                CHANNEL=CHANNEL_JDPAY_WAP;
                URL = "http://10.6.0.235/pingpp-php-master/test.php?channel=jdpay_wap&amount=100&type=live";
                getChanger(URL);
                break;
        }
    }


    //http请求
    private void getChanger(String url) {
        //按键点击之后的禁用，防止重复点击
        wechatButton.setOnClickListener(null);
        alipayButton.setOnClickListener(null);
        upmpButton.setOnClickListener(null);
        bfbButton.setOnClickListener(null);
        MyHttpClient.getInstance(this).post(url,null, new MyAsyncHttpResponseHandler(new LoadDatahandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
//                Pingpp.createPayment(ClientSDKActivity.this, data);
                Intent intent = new Intent();
                String packageName = getPackageName();
                ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
                intent.setComponent(componentName);
                intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }

            @Override
            public void onFailure(String error, String message) {
                super.onFailure(error, message);
            }
        }));
    }


    //http请求
    private void tEST(String url) {
        //按键点击之后的禁用，防止重复点击
        wechatButton.setOnClickListener(null);
        alipayButton.setOnClickListener(null);
        upmpButton.setOnClickListener(null);
        bfbButton.setOnClickListener(null);
        MyHttpClient.getInstance(this).get(url, null, new MyAsyncHttpResponseHandler(new LoadDatahandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                Log.i("data", data);
//                Pingpp.createPayment(ClientSDKActivity.this, data);
//                Intent intent = new Intent();
//                String packageName = getPackageName();
//                ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
//                intent.setComponent(componentName);
//                intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
//                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }

            @Override
            public void onFailure(String error, String message) {
                super.onFailure(error, message);
            }
        }));
    }


    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        wechatButton.setOnClickListener(MainActivity.this);
        alipayButton.setOnClickListener(MainActivity.this);
        upmpButton.setOnClickListener(MainActivity.this);
        bfbButton.setOnClickListener(MainActivity.this);

        //支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }


    private void noHttp(String url){
        Request<String> request= NoHttp.createStringRequest(url, RequestMethod.GET);

        requestQueue.add(NOHTTP_WHAT_TEST, request, onResponseListener);
    }

    /**
     * 回调对象，接受请求结果
     */
    private OnResponseListener<String> onResponseListener = new OnResponseListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == NOHTTP_WHAT_TEST) {// 判断what是否是刚才指定的请求
                // 请求成功
                String result = response.get();// 响应结果
                Log.i("onSucceed",result);
                // 响应头
                Headers headers = response.getHeaders();
                headers.getResponseCode();// 响应码
                response.getNetworkMillis();// 请求花费的时间
                Intent intent = new Intent();
                String packageName = getPackageName();
                ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
                intent.setComponent(componentName);
                intent.putExtra(PaymentActivity.EXTRA_CHARGE, result);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }
        }

        @Override
        public void onStart(int what) {
            // 请求开始，显示dialog
//            mWaitDialog.show();
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，关闭dialog
//            mWaitDialog.dismiss();
        }

        @Override
        public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
            Log.i("onFailed",o.toString());
        }
    };

    @Override
    protected void dataCallback(int result, Object data) {
        super.dataCallback(result, data);
        switch (result){
            case NOHTTP_WHAT_TEST:
                Intent intent = new Intent();
                String packageName = getPackageName();
                ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
                intent.setComponent(componentName);
                intent.putExtra(PaymentActivity.EXTRA_CHARGE, data.toString());
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                break;
        }
    }
}
