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
import com.vmloft.develop.app.screencast.entity.ClingDevice;
import com.vmloft.develop.app.screencast.manager.DeviceManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzan13 on 2018/3/9.
 * Cling 设备列表适配器
 */
public class ClingDeviceAdapter extends RecyclerView.Adapter<ClingDeviceAdapter.ClingHolder> {

    private LayoutInflater layoutInflater;
    private List<ClingDevice> clingDevices;
    private ItemClickListener clickListener;

    public ClingDeviceAdapter(Context context) {
        super();
        layoutInflater = LayoutInflater.from(context);
        clingDevices = DeviceManager.getInstance().getClingDeviceList();
    }

    @NonNull
    @Override
    public ClingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_common_layout, parent, false);
        return new ClingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClingHolder holder, final int position) {
        final ClingDevice device = clingDevices.get(position);
        if (device == DeviceManager.getInstance().getCurrClingDevice()) {
            holder.iconView.setVisibility(View.VISIBLE);
        } else {
            holder.iconView.setVisibility(View.INVISIBLE);
        }
        holder.nameView.setText(device.getDevice().getDetails().getFriendlyName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemAction(position, device);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clingDevices.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    static class ClingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_name) TextView nameView;
        @BindView(R.id.img_icon) ImageView iconView;

        public ClingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
