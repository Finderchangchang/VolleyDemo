package com.zcs.demo.volley;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.zcs.demo.volley.adapter.VolleyListAdapter;
import com.zcs.demo.volley.entity.VolleyItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    // 列表长度
    static final int LIST_SIZE = 2000;

    private ListView mListView;
    private TextView mTxt;
    private VolleyListAdapter mAdapter;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Volley加载大量图片");

        // TODO 初始化VolleyRequestQueue对象,这个对象是Volley访问网络的直接入口
        mQueue = Volley.newRequestQueue(this);

        initList();
        mTxt = (TextView) findViewById(R.id.volley_txt);

        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, "http://apis.baidu.com/apistore/weatherservice/recentweathers?cityname=%E5%8C%97%E4%BA%AC&cityid=101010100", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String s = response.toString();
                        mTxt.setText(s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", "706da19045e60c089cd457bd10e5e733");
                return headers;
            }
        };
        mQueue.add(jsonRequest);
    }

    /**
     * 初始化List
     */
    private void initList() {
        mListView = (ListView) findViewById(R.id.volley_listview);

        // TODO 初始化数据
        ArrayList<VolleyItem> items = new ArrayList<VolleyItem>(LIST_SIZE);
//		String imgUrl = "http://img0.bdstatic.com/img/image/%E6%9C%AA%E6%A0%87%E9%A2%98-1.jpg";
        String imgUrl = "http://pic24.nipic.com/20120920/10361578_112230424175_2.jpg";
        for (int i = 1; i <= LIST_SIZE; i++) {
            VolleyItem item = new VolleyItem();
            item.setName("我所看到的香港-" + i);
            // TODO 为图片地址添加一个尾数,是为了多次访问图片,而不是读取第一张图片的缓存
            item.setImgUrl(imgUrl + "?rank=" + i);
            items.add(item);
        }

        // TODO 绑定数据
        mAdapter = new VolleyListAdapter(this, mQueue, items);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO 取消所有未执行完的网络请求
        mQueue.cancelAll(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
