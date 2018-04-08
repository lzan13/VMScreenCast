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

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzan13 on 2018/3/19.
 */

public class LocalContentAdapter extends RecyclerView.Adapter<LocalContentAdapter.ContentHolder> {

    private LayoutInflater layoutInflater;
    private List<DIDLObject> objectList;
    private ItemClickListener clickListener;

    public LocalContentAdapter(Context context, List<DIDLObject> list) {
        super();
        objectList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_common_layout, parent, false);
        return new ContentHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ContentHolder holder, final int position) {
        final DIDLObject object = objectList.get(position);

        if (object instanceof Container) {
            Container container = (Container) object;
            holder.nameView.setText(container.getTitle());
            holder.iconView.setImageResource(R.drawable.ic_folder_24dp);
        } else if (object instanceof Item) {
            Item item = (Item) object;
            holder.nameView.setText(item.getTitle());
            holder.iconView.setImageResource(R.drawable.ic_file_24dp);
        }
        holder.iconView.setVisibility(View.VISIBLE);
        holder.iconView.setColorFilter(R.color.vm_gray_87);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemAction(position, object);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return objectList.size();
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
