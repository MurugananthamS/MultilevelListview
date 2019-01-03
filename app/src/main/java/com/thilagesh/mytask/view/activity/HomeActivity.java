package com.thilagesh.mytask.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.thilagesh.mytask.Model.Model;
import com.thilagesh.mytask.R;
import com.thilagesh.mytask.view.Interface.Apis;
import com.thilagesh.mytask.view.Utils.ConnectionDetector;
import com.thilagesh.mytask.view.adapter.Adapter;
import com.thilagesh.mytask.view.adapter.ListAdapter;
import com.thilagesh.mytask.view.volley.ServiceRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, com.thilagesh.mytask.view.adapter.ListAdapter.OnItemClickListener {
    private ServiceRequest mRequest;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    RecyclerView rvList;
    ListAdapter adapter1;
    Adapter adapter;
    ArrayList<Model> models;
    ArrayList<Model> modelsSearch;
    ArrayList<Model> modelsRefresh;
    AVLoadingIndicatorView indicator;


    ImageView search;
    ArrayList<String> previousSearches;
    RelativeLayout refreshview, searchnewid;
    ListView listView;
    SearchView searchnew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        FindViews();
    }

    private void FindViews() {
        cd = new ConnectionDetector(HomeActivity.this);
        isInternetPresent = cd.isConnectingToInternet();

        indicator = findViewById(R.id.indicator);

        previousSearches = new ArrayList<>();

        refreshview = findViewById(R.id.refreshview);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        refreshview.setOnClickListener(this);
        listView = findViewById(R.id.list_view);
        searchnew = findViewById(R.id.searchnew);
        searchnewid = findViewById(R.id.searchnewid);

        if (isInternetPresent) {
            if (mRequest != null) {
                mRequest.cancelRequest();
            }
            PostRequest(Apis.url);
        } else {
            Toast.makeText(HomeActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        adapter1 = new ListAdapter(HomeActivity.this, previousSearches, HomeActivity.this);
        listView.setAdapter(adapter1);

        searchnew.setActivated(true);
        searchnew.setQueryHint("Type your keyword here");
        searchnew.onActionViewExpanded();
        searchnew.setIconified(false);
        searchnew.clearFocus();
        searchnew.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter1.getFilter().filter(newText);

                return false;
            }
        });

    }


    private void PostRequest(String Url) {

        indicator.setVisibility(View.VISIBLE);
        Log.d("url", Url);

        mRequest = new ServiceRequest(HomeActivity.this);
        mRequest.makeServiceRequest(Url, Request.Method.GET, null, new ServiceRequest.ServiceListener() {
            @Override
            public void onCompleteListener(String response) {

                Log.d("reponse", response);
                String keyRoom = "", key = "", title = "", element_name = "", element_qty = "", unit = "", service_type = "", elementSingle = "";

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.length() > 0) {

                        if (object.has("room")) {
                            models = new ArrayList<>();
                            modelsRefresh = new ArrayList<>();
                            JSONObject objRoom = object.getJSONObject("room");
                            Iterator<String> keys = objRoom.keys();
                            while (keys.hasNext()) {

                                key = (String) keys.next();
                                JSONObject obj = new JSONObject();
                                obj = objRoom.getJSONObject(key);

                                keyRoom = obj.getString("name");
                                previousSearches.add(keyRoom);
                                Model model1_1 = new Model(keyRoom, 1, "");
                                //Requirement
                                JSONObject objRoomRequirement = obj.getJSONObject("requirement");
                                Iterator<String> keysRequirement = objRoomRequirement.keys();
                                while (keysRequirement.hasNext()) {
                                    String keyRequirement = (String) keysRequirement.next();
                                    JSONObject objRequirement = new JSONObject();
                                    objRequirement = objRoomRequirement.getJSONObject(keyRequirement);

                                    title = objRequirement.getString("title");
                                    Model model1_1_1 = new Model(title, 2, "");
                                    //Elements
                                    JSONObject objRoomElements = objRequirement.getJSONObject("elements");
                                    Iterator<String> keysElements = objRoomElements.keys();
                                    while (keysElements.hasNext()) {


                                        String keyElements = (String) keysElements.next();
                                        JSONObject objElements = new JSONObject();
                                        objElements = objRoomElements.getJSONObject(keyElements);

                                        element_name = objElements.getString("element_name");
                                        element_qty = objElements.getString("element_qty");
                                        unit = objElements.getString("unit");
                                        service_type = objElements.getString("service_type");

                                        elementSingle = "Element Name:" + element_name
                                                + "\n" + "Element Qty:" + element_qty
                                                + "\n" + "Unit:" + unit
                                                + "\n" + "Service Type:" + service_type;


                                        Model model1_2_1_1 = new Model(elementSingle, 3, "");

                                        model1_1_1.models.add(model1_2_1_1);


                                    }


                                    model1_1.models.add(model1_1_1);
                                }
                                models.add(model1_1);
                                modelsRefresh = models;
                            }

                        }

                        setAapterView(1);


                        indicator.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onErrorListener() {
                indicator.setVisibility(View.GONE);
            }
        });


    }

    public void setAapterView(int flag) {
        if (rvList != null) {
            rvList = null;
        }

        if (flag == 1) {
            rvList = findViewById(R.id.list);
            adapter = new Adapter(HomeActivity.this);
            rvList.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
            rvList.setAdapter(adapter);
            adapter.setData(models);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == search) {
            searchnewid.setVisibility(View.VISIBLE);

        } else if (view == refreshview) {
            models = modelsRefresh;
            setAapterView(1);
        }
    }

    @Override
    public void onBackPressed() {

        if (searchnewid.getVisibility() == View.VISIBLE) {
            searchnewid.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(String item) {


        searchnewid.setVisibility(View.INVISIBLE);

        modelsSearch = new ArrayList<>();
        Model model1_1_1 = null;
        Model model1_1 = new Model(item, 1, "");
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).name.equalsIgnoreCase(item)) {
                for (int k = 0; k < models.get(i).models.size(); k++) {
                    model1_1_1 = new Model(models.get(i).models.get(k).name, 2, "");

                    for (int j = 0; j < models.get(i).models.get(k).models.size(); j++) {
                        String elementSingle = models.get(i).models.get(k).models.get(j).name;

                        Model model1_2_1_1 = new Model(elementSingle, 3, "");
                        model1_1_1.models.add(model1_2_1_1);
                    }
                    model1_1.models.add(model1_1_1);
                }
            }
        }
        modelsSearch.add(model1_1);


        rvList = null;
        rvList = findViewById(R.id.list);
        adapter.notifyDataSetChanged();
        adapter = new Adapter(HomeActivity.this);
        rvList.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(adapter);
        adapter.setData(modelsSearch);


        Log.d("item", item);
    }

    /**/
}
