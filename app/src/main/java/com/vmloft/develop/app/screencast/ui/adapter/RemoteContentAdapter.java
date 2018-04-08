package com.vmloft.develop.app.screencast.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vmloft.develop.app.screencast.R;
import com.vmloft.develop.app.screencast.callback.ItemClickListener;
import com.vmloft.develop.app.screencast.entity.RemoteItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzan13 on 2018/3/9.
 * Cling 设备列表适配器
 */
public class RemoteContentAdapter extends RecyclerView.Adapter<RemoteContentAdapter.ContentHolder> {

    private LayoutInflater layoutInflater;
    private List<RemoteItem> contentList;
    private ItemClickListener clickListener;

    public RemoteContentAdapter(Context context, List<RemoteItem> list) {
        super();
        layoutInflater = LayoutInflater.from(context);
        contentList = list;
    }

    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_common_layout, parent, false);
        return new ContentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentHolder holder, final int position) {
        final RemoteItem item = contentList.get(position);
        holder.iconView.setVisibility(View.VISIBLE);
        holder.iconView.setColorFilter(R.color.vm_gray_87);
        holder.iconView.setImageResource(R.drawable.ic_file_24dp);
        holder.nameView.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemAction(position, item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    static class ContentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_name) TextView nameView;
        @BindView(R.id.img_icon) ImageView iconView;

        public ContentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
