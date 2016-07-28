package com.jianfanjia.cn.activity;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.FlakyTest;

import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.common.tool.JsonParser;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    String html = "{\"html\":\"<ul class=\\\"m-list list-main interval " +
            "list-paddingleft-2\\\" style=\\\"list-style-type: none;\\\">\\n    <li>\\n        <p>\\n            <a " +
            "href=\\\"http://news.163.com/16/0727/12/BSVUN0FV0001124J.html\\\" style=\\\"color: rgb(37, 37, 37); " +
            "text-decoration: none; font-weight: bold;\\\">中国歼15飞行员在陆基模拟着舰训练中牺牲</a>\\n        </p>\\n    </li>\\n    " +
            "<li>\\n        <p>\\n            <a href=\\\"http://news.163" +
            ".com/16/0727/00/BSUMLJC000014JB6.html\\\" style=\\\"color: rgb(37, 37, 37); text-decoration: none;" +
            "\\\">中国7亿人遭高温\"烧烤\" 局地体感温度超50℃</a>\\n        </p>\\n    </li>\\n    <li>\\n        <p>\\n            <a " +
            "href=\\\"http://hb.news.163.com/16/0727/13/BT02OLJC039417NA.html\\\" style=\\\"color: rgb(37, 37, 37); " +
            "text-decoration: none;\\\">武汉职业技术学院回应 停收借道车辆穿行费</a>\\n        </p>\\n    </li>\\n    <li>\\n        " +
            "<p>\\n            <a href=\\\"http://hb.news.163" +
            ".com/16/0727/10/BSVNRRLF03941BB8.html\\\" style=\\\"color: rgb(37, 37, 37); text-decoration: none;" +
            "\\\">湖北神丹再伸援手抗洪灾 30万元捐乡镇</a>\\n        </p>\\n    </li>\\n    <li>\\n        <p>\\n            <a " +
            "href=\\\"http://hb.news.163.com/16/0727/13/BT028HFT039417NA.html\\\" style=\\\"color: rgb(37, 37, 37); " +
            "text-decoration: none;\\\">旅游大巴撞翻越野车 司机丢下满车乘客走了</a>\\n        </p>\\n    </li>\\n</ul>\\n<p>\\n    " +
            "<br/>\\n</p>\"}";

    @FlakyTest
    public void checkJson(){
        JsonParser.jsonToT(html, UserMessage.class);
    }
}