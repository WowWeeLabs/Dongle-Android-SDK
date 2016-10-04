package com.wowwee.lumidonglesampleproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.wowwee.bluetoothrobotcontrollib.lumi.LumiCommandValues;
import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobot;
import com.wowwee.lumidonglesampleproject.R;
import com.wowwee.lumidonglesampleproject.utils.DonglePlayer;

/**
 * Created by yinlau on 3/10/2016.
 */

public class MainFragment extends BaseViewFragment {

    private ToggleButton tbDongle;

    private LumiRobot myDongle;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    //================================================================================
    // Override
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null)
            return null;

        myDongle = DonglePlayer.getInstance().getPlayerDongle();

        View view = super.onCreateView(inflater, container, savedInstanceState);

        tbDongle = (ToggleButton) view.findViewById(R.id.tb_dongle);
        tbDongle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                turnDongle(isChecked);
            }
        });

        return view;
    }

    private void turnDongle(boolean turnOn) {
        if(myDongle != null) {
            myDongle.turnDongle(new byte[]{turnOn ? LumiCommandValues.COMMAND_ON : LumiCommandValues.COMMAND_OFF});
        }
    }
}
