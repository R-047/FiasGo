package com.example.fiasgo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fiasgo.Adapters.NewsAdapter;
import com.example.fiasgo.utils.FiasGoApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.FormBodyPart;
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView news;
    NewsAdapter newsAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        news = view.findViewById(R.id.news_rv_container);
        String someData = "{\n" +
                "  \"preset\": \"latest\",\n" +
                "  \"query\":\n" +
                "    {\n" +
                "      \"value\": \"India\"\n" +
                "    },\n" +
                "    \"fields\":\n" +
                "    {\n" +
                "      \"include\": [\"description\"]\n" +
                "    },\n" +
                "    \"sort\": [\"date:desc\"],\n" +
                "    \"limit\": 20\n" +
                "}";
        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
        } catch(Exception e) {

        }

        System.out.println(someData);
        System.out.println(entity);
        RequestParams params = new RequestParams();
        params.put("appname", "pqw6ue+5mppgjtq1bxi0@spam4.me");
        FiasGoApi.post(getContext(),"https://api.reliefweb.int/v1/disasters", params, entity,"application/json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("success: "+ statusCode);
                try {
                    JSONObject result =new JSONObject(new String(responseBody));
                    JSONArray data = result.getJSONArray("data");
                    System.out.println("string response: "+data);
                    newsAdapter = new NewsAdapter(data, getContext());
                    news.setAdapter(newsAdapter);
                    news.setLayoutManager(new LinearLayoutManager(getActivity()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(statusCode);
                System.out.println(responseBody);

            }
        });

    }
}