package com.ikats.shop.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikats.shop.model.ProvinceBean;

import cn.droidlover.xdroidmvp.base.XListAdapter;

public class AddressAdapter<T> extends XListAdapter<T> {

    public AddressAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
        TextView textView = (TextView) view;
        textView.setPadding(3, 3, 3, 3);
        final Object item = data.get(position);
        if (item instanceof ProvinceBean) {
            textView.setText(((ProvinceBean) item).name);
        } else if (item instanceof ProvinceBean.CityListBean) {
            textView.setText(((ProvinceBean.CityListBean) item).name);
        } else if (item instanceof ProvinceBean.AreaListBean) {
            textView.setText(((ProvinceBean.AreaListBean) item).name);
        }
        return view;
    }

}
