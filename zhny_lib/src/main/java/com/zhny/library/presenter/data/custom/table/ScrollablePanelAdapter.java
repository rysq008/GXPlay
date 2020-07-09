package com.zhny.library.presenter.data.custom.table;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelin.scrollablepanel.library.PanelAdapter;
import com.zhny.library.R;
import com.zhny.library.presenter.data.model.vo.CustomTableData;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class ScrollablePanelAdapter extends PanelAdapter {
    
    private static final int TITLE_TYPE = 4; //名称
    private static final int LEFT_TYPE = 0; //第一列
    private static final int TOP_TYPE = 1; //第一行
    private static final int DATA_TYPE = 2;//数据

    private OnTableRowSelectListener onTableRowSelectListener;

    private List<CustomTableData.Column> leftList = new ArrayList<>();
    private List<String> topList = new ArrayList<>();
    private List<List<CustomTableData.Column>> dataColumn  = new ArrayList<>();

    public ScrollablePanelAdapter(OnTableRowSelectListener listener) {
        this.onTableRowSelectListener = listener;
    }

    public void refresh(CustomTableData tableData) {
        topList.clear();
        leftList.clear();
        dataColumn.clear();
        topList.addAll(tableData.topList);
        leftList.addAll(tableData.firstColumn);
        dataColumn.addAll(tableData.dataColumn);
    }


    @Override
    public int getRowCount() {
        return leftList.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return topList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        int viewType = getItemViewType(row, column);
        switch (viewType) {
            case TOP_TYPE: //第一列
                setTopView(column, (TopViewHolder) holder);
                break;
            case LEFT_TYPE: //第一行
                setLeftView(row, (LeftViewHolder) holder);
                break;
            case DATA_TYPE: //数据
                setDataView(column, row, (DataViewHolder) holder);
                break;
            case TITLE_TYPE: //名称
                break;
        }
    }

    public int getItemViewType(int row, int column) {
        if (column == 0 && row == 0) {
            return TITLE_TYPE;
        }
        if (column == 0) {
            return LEFT_TYPE;
        }
        if (row == 0) {
            return TOP_TYPE;
        }
        return DATA_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TOP_TYPE:
                return new TopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_table_top, parent, false));
            case LEFT_TYPE:
                return new LeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_table_left, parent, false));
            case DATA_TYPE:
                return new DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_table_data, parent, false));
            case TITLE_TYPE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_table_title, parent, false));
            default:
                break;
        }
        return new DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_table_data, parent, false));
    }


    private void setTopView(int pos, TopViewHolder viewHolder) {
        String top = topList.get(pos - 1);
        if (!TextUtils.isEmpty(top)) {
            viewHolder.top.setText(top);
        }
    }

    private void setLeftView(int pos, LeftViewHolder viewHolder) {
        CustomTableData.Column left = leftList.get(pos - 1);
        if (left != null) {
            viewHolder.left.setText(left.value);
            viewHolder.left.setTextColor(Color.parseColor(left.index < 0 ? "#000000" : "#009688"));
            viewHolder.left.setOnClickListener(v -> onTableRowSelectListener.onTableRowSelect(left.index));
        }
    }

    private void setDataView(final int row, final int column, DataViewHolder viewHolder) {
        final CustomTableData.Column data = dataColumn.get(row - 1).get(column - 1);
        if (data != null) {
            viewHolder.columnData.setText(data.value);
            viewHolder.itemView.setOnClickListener(v -> onTableRowSelectListener.onTableRowSelect(data.index));
        }
    }


    private static class TopViewHolder extends RecyclerView.ViewHolder {
        TextView top;

        TopViewHolder(View itemView) {
            super(itemView);
            this.top = itemView.findViewById(R.id.top_column);
        }

    }

    private static class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView left;

        LeftViewHolder(View view) {
            super(view);
            this.left = view.findViewById(R.id.left_row);
        }
    }

    private static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView columnData;
        public View view;

        DataViewHolder(View view) {
            super(view);
            this.view = view;
            this.columnData = view.findViewById(R.id.table_data_column);
        }
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        TitleViewHolder(View view) {
            super(view);
            this.titleTextView = view.findViewById(R.id.table_title);
        }
    }

    public interface OnTableRowSelectListener {
        void onTableRowSelect(int index);
    }

}
