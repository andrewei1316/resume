package info.andrewei.resume.onlineInfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.andrewei.resume.R;
import info.andrewei.resume.tools.ProgressWebView;


public class Page1MyWeibo extends Fragment {

    private View mView;

    @SuppressLint("SetJavaScriptEnabled")
    private void openWeb() {
        ProgressWebView webView = (ProgressWebView) mView.findViewById(R.id.web_weibo);//实例化webview对象
        //设置webview属性能够执行javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //设置webView可以缩放，只可以双击缩放
        webView.getSettings().setSupportZoom(true);
        //设置是否可缩放
        webView.getSettings().setBuiltInZoomControls(true);
        //无限缩放
        webView.getSettings().setUseWideViewPort(true);
        //加载需要显示的网页
        webView.loadUrl("http://weibo.com/u/5305101643");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_page1_my_weibo, container, false);
        openWeb();
        return mView;
    }

}
