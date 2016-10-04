package com.wowwee.lumidonglesampleproject.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobot;
import com.wowwee.lumidonglesampleproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinlau on 3/10/2016.
 */

public class DonglePagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<LumiRobot> lumiFoundList;

    public DonglePagerAdapter(Context context, List<LumiRobot> lumiFoundList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lumiFoundList = lumiFoundList;
    }

    @Override
    public int getCount() {
        return lumiFoundList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.page_dongle, container, false);

        ImageView imgDongle = (ImageView) itemView.findViewById(R.id.img_dongle);
        TextView tvDongle = (TextView) itemView.findViewById(R.id.tv_dongle);

        tvDongle.setText(lumiFoundList.get(position).getName());

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void setDeviceList(List<LumiRobot> lumiFoundList) {
        this.lumiFoundList = lumiFoundList;
    }
}
