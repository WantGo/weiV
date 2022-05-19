package cn.flutterfirst.weiv_example;

import cn.flutterfirst.weiv.core.activities.WeiVJavaActivity;
import cn.flutterfirst.weiv.core.others.JavaOnly;
import cn.flutterfirst.weiv_example.ext.weiVButton;

abstract class BaseWeiVJavaActivity extends WeiVJavaActivity {
    @JavaOnly
    public weiVButton Button() {
        return getWeiV().addLeafRenderWidget(new weiVButton());
    }
}
