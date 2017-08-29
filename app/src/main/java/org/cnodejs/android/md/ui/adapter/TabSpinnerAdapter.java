package org.cnodejs.android.md.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cnodejs.android.md.model.entity.Tab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabSpinnerAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<Tab> tabList = new ArrayList<>();

    public TabSpinnerAdapter(@NonNull Context context, @NonNull List<Tab> tabList) {
        inflater = LayoutInflater.from(context);
        this.tabList.addAll(tabList);
    }

    @Override
    public int getCount() {
        return tabList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return tabList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createOrUpdateViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createOrUpdateViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    private View createOrUpdateViewFromResource(int position, View convertView, ViewGroup parent, @LayoutRes int layoutId) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.update(tabList.get(position));
        return convertView;
    }

    class ViewHolder {

        @BindView(android.R.id.text1)
        TextView tvText;

        ViewHolder(@NonNull View itemView) {
            ButterKnife.bind(this, itemView);
        }

        void update(@NonNull Tab tab) {
            tvText.setText(tab.getNameId());
        }

    }

}
