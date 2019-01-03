package com.thilagesh.mytask.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.thilagesh.mytask.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anupamchugh on 07/02/16.
 */
public class ListAdapter extends BaseAdapter implements Filterable {

    List<String> mData;
    List<String> mStringFilterList;
    ValueFilter valueFilter;
    private LayoutInflater inflater;
    ViewHolder viewHolder;
    OnItemClickListener onitemclicklistener;
    Context cxt;
    public ListAdapter(Context cxt,List<String> cancel_type, OnItemClickListener onitemclicklistener1) {
        mData=cancel_type;
        mStringFilterList = cancel_type;
        onitemclicklistener=onitemclicklistener1;
        this.cxt=cxt;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView stringName;

    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final View result;
        if( convertView == null ){
            //We must create a View:
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(cxt);
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.stringName = (TextView) convertView.findViewById(R.id.stringname);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.stringName.setText(mData.get(position));

        viewHolder.stringName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onitemclicklistener.onItemClick(viewHolder.stringName.getText().toString());
            }
        });

        return result;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<String> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mData = (List<String>) results.values;
            notifyDataSetChanged();
        }

    }
    public interface OnItemClickListener {
        void onItemClick(String item);
    }
}
