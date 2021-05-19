package com.example.myprojectyear32.data.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectyear32.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter {
    private ArrayList<Notification> mNotification;
    // Lưu Context để dễ dàng truy cập

    public NotificationAdapter(ArrayList<Notification> _notification) {
        this.mNotification = _notification;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View notificationView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(notificationView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Notification notification = mNotification.get(position);

        ((ViewHolder) holder).descriptionTV.setText(notification.getDescription());
        ((ViewHolder) holder).dateTV.setText(notification.getTime());
        ((ViewHolder) holder).detail_type.setImageResource(notification.getImage());
    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    /**
     * Lớp nắm giữ cấu trúc view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionTV;
        public TextView dateTV;
        public ImageView detail_type;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionTV = itemView.findViewById(R.id.notification_tv);
            dateTV = itemView.findViewById(R.id.time_tv);
            //Xử lý khi nút Chi tiết được bấm
            detail_type = itemView.findViewById(R.id.image_type);
        }
    }
}
