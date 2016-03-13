package com.example.nano1.af_promotionalcard;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

/**
 * Created by nano1 on 3/11/2016.
 */
public class MyAdapter extends BaseAdapter {

    Context mContext;
    List<Promotion.PromotionsEntity> mListOfPro;

    MyAdapter(Context context, List<Promotion.PromotionsEntity> mPromotions) {
        this.mContext = context;
        this.mListOfPro = mPromotions;
    }

    class ViewHolder {
        TextView mItemTitle;
        SimpleDraweeView draweeView;

        ViewHolder(View row) {
            mItemTitle = (TextView) row.findViewById(R.id.text_Title);
            draweeView = (SimpleDraweeView) row.findViewById(R.id.image_Thumbnail);
        }
    }

    @Override
    public int getCount() {
        return mListOfPro.size();
    }

    @Override
    public Object getItem(int position) {
        return mListOfPro.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.main_single_item, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Promotion.PromotionsEntity mPromotion = mListOfPro.get(position);
        holder.mItemTitle.setText(mPromotion.getTitle());
        if (mPromotion.getImage()!= null) {
            Uri uri = Uri.parse(mPromotion.getImage());
            holder.draweeView.setImageURI(uri);
        }
        return row;
    }

}



