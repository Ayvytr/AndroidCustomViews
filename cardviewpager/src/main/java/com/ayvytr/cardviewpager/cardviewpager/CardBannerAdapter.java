package com.ayvytr.cardviewpager.cardviewpager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ayvytr.cardviewpager.R;
import com.ayvytr.ktx.context.DpKt;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

/**
 * @author ayvytr
 */
public class CardBannerAdapter extends PagerAdapter {
    private final RequestOptions requestOptions;
    private List<String> list;

    public CardBannerAdapter(List<String> list) {
        this.list = list;
        requestOptions = new RequestOptions().transform(new GlideRoundTransform());
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                                  .inflate(R.layout.item_banner, container, false);
        CardView cardView = view.findViewById(R.id.card_view);
        ImageView iv = view.findViewById(R.id.iv);
        Glide.with(container.getContext())
             .load(list.get(position))
             .apply(requestOptions)
             .into(iv);

        cardView.setMaxCardElevation(DpKt.getDp2px(5));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
