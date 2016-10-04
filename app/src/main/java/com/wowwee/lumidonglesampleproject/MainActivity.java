package com.wowwee.lumidonglesampleproject;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.wowwee.bluetoothrobotcontrollib.BluetoothRobot;
import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobot;
import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobotFinder;
import com.wowwee.lumidonglesampleproject.fragment.FragmentHelper;
import com.wowwee.lumidonglesampleproject.fragment.ScanFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        FragmentHelper.switchFragment(getSupportFragmentManager(), new ScanFragment(), R.id.view_id_content, false);

        BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        for (LumiRobot robot : LumiRobotFinder.getInstance().getmLumiRobotConnectedList()){
            robot.disconnect();
        }
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // disable idle timer
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (LumiRobot robot : LumiRobotFinder.getInstance().getmLumiRobotConnectedList()){
            robot.disconnect();
        }

        BluetoothRobot.unbindBluetoothLeService(MainActivity.this);

        System.exit(0);
    }

}