package com.ipda3.bloodbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.generalResponse.GeneralResponseData;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<GeneralResponseData> generalResponseData = new ArrayList<>();
    private LayoutInflater inflter;
    public Integer selectedId = 0;

    public SpinnerAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void setData(List<GeneralResponseData> generalResponseData, String hint) {
        this.generalResponseData=new ArrayList<>();
        this.generalResponseData.add(new GeneralResponseData(0, hint));
        this.generalResponseData.addAll(generalResponseData);
    }

    @Override
    public int getCount() {
        return generalResponseData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_custom_spinner, null);

        TextView names = (TextView) view.findViewById(R.id.item_custom_spinner_tv_text);

        names.setText(generalResponseData.get(i).getName());
        selectedId = generalResponseData.get(i).getId();

        return view;
    }
}




