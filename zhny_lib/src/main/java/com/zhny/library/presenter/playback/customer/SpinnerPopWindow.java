package com.zhny.library.presenter.playback.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhny.library.R;

import java.util.List;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-09 13:39
 */
public class SpinnerPopWindow<T> extends PopupWindow {

    private LayoutInflater inflater;
    private ListView mListView;
    private List<T> list;
    private SpinnerAdapter  mAdapter;


    public SpinnerPopWindow(Context context, List<T> list, AdapterView.OnItemClickListener clickListener) {
        super(context);
        inflater=LayoutInflater.from(context);
        this.list=list;
        mAdapter = new SpinnerAdapter();
        init(clickListener);
    }

    private void init(AdapterView.OnItemClickListener clickListener){
        View view = inflater.inflate(R.layout.spinner_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);

        mListView = view.findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(clickListener);
    }

    private class SpinnerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=inflater.inflate(R.layout.spinner_item_layout, null);
                holder.tvName= convertView.findViewById(R.id.spinner_item_label);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(getItem(position).toString());
            return convertView;
        }
    }

    private class ViewHolder{
        private TextView tvName;
    }


}
