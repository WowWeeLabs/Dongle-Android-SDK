package com.wowwee.lumidonglesampleproject.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobot;
import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobotFinder;
import com.wowwee.lumidonglesampleproject.R;
import com.wowwee.lumidonglesampleproject.utils.DonglePagerAdapter;
import com.wowwee.lumidonglesampleproject.utils.DonglePlayer;
import com.wowwee.lumidonglesampleproject.utils.MultiViewPager;
import com.wowwee.lumidonglesampleproject.utils.ZoomOutPageTransformer;

import java.util.List;

public class ScanFragment extends BaseViewFragment implements OnClickListener {

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler handler;

    private Button btnConnect;
    private ProgressBar progressBar;

    private DonglePagerAdapter donglePagerAdapter;
    private MultiViewPager vpDongle;
    private int dongleIndex = 0;
    private boolean isInitViewPager = false;

    // Connect logic
    private static final int CONNECTION_IDLE = 0;
    private static final int CONNECTION_SCANNING = 1;
    private static final int CONNECTION_SCAN_HOLD = 2;
    private static final int CONNECTION_CONNECTING = 3;
    private static final int CONNECTION_CONNECTED = 4;
    private int connectionState = CONNECTION_IDLE;
    private long connectTimestamp;

    //================================================================================
    // Constructors
    //================================================================================

    public ScanFragment() {
        super(R.layout.fragment_scan);
    }

    //================================================================================
    // Override
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null)
            return null;

        handler = new Handler();

        View view = super.onCreateView(inflater, container, savedInstanceState);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        vpDongle = (MultiViewPager)view.findViewById(R.id.vp_dongle);
        btnConnect = (Button) view.findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(this);

        vpDongle.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                dongleIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        vpDongle.setPageTransformer(true, new ZoomOutPageTransformer());

        // Init bluetooth
        initBluetooth();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                connectDrone(dongleIndex);
                break;
        }
    }

    private void lockScreen() {
        btnConnect.setEnabled(false);
    }

    private void connectToLumi(LumiRobot lumi) {
        lumi.setCallbackInterface(this);
        lumi.connect(getActivity());
        Log.d("BLE", "try to connect selectedLumiRobot name = " + lumi.getName());
    }

    private void connectDrone(int selectedLumiIndex) {
        List<LumiRobot> lumiFoundList = LumiRobotFinder.getInstance().getLumiFoundList();
        if (lumiFoundList.size() > selectedLumiIndex && selectedLumiIndex >= 0){
            LumiRobot selectedLumiRobot = lumiFoundList.get(selectedLumiIndex);
            if(selectedLumiRobot != null) {
                final LumiRobot connectLumiRobot = selectedLumiRobot;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        lockScreen();
                        progressBar.setVisibility(View.VISIBLE);
                        // Connect to Lumi
                        connectToLumi(connectLumiRobot);
                        // Connection state
                        setConnectionState(CONNECTION_CONNECTING);
                        // Stop scan Lumi
                        scanLeDevice(false);
                    }
                });
            }
        }
    }

    private void rescanDrones() {
        // Start scan
        LumiRobotFinder.getInstance().clearFoundLUMIList();
        scanLeDevice(false);
        updateDongleList();
        scanLeDevice(true);
    }

    private void updateDongleList(){
        List<LumiRobot> dongleFoundList = LumiRobotFinder.getInstance().getLumiFoundList();
        if(dongleFoundList != null && dongleFoundList.size() > 0) {
            if(isInitViewPager) {
                donglePagerAdapter.setDeviceList(dongleFoundList);
                donglePagerAdapter.notifyDataSetChanged();
            } else {
                isInitViewPager = true;
                donglePagerAdapter = new DonglePagerAdapter(getActivity(), dongleFoundList);
                vpDongle.setAdapter(donglePagerAdapter);
            }
            vpDongle.setVisibility(View.VISIBLE);
        } else {
            vpDongle.setVisibility(View.INVISIBLE);
        }
    }

    //================================================================================
    // Lumi connection
    //================================================================================
    @Override
    public void onResume() {
        super.onResume();

        // Register LumiRobotFinder broadcast receiver
        getFragmentActivity().registerReceiver(mLumiFinderBroadcastReceiver, LumiRobotFinder.getLumiRobotFinderIntentFilter());

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        rescanDrones();
    }

    @Override
    public void onPause() {
        super.onPause();
        getFragmentActivity().unregisterReceiver(mLumiFinderBroadcastReceiver);
        scanLeDevice(false);
    }

    private void initBluetooth(){
        final BluetoothManager bluetoothManager = (BluetoothManager) getFragmentActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        LumiRobotFinder.getInstance().setBluetoothAdapter(mBluetoothAdapter);
        LumiRobotFinder.getInstance().setApplicationContext(getFragmentActivity());
    }

    //================================================================================
    // Connect Logic
    //================================================================================
    private void setConnectionState(int state) {
        if(connectionState != state) {
            connectionState = state;
            switch (connectionState) {
                default:
                case CONNECTION_SCANNING:
                    break;
                case CONNECTION_SCAN_HOLD:
                    connectTimestamp = System.currentTimeMillis();
                    break;
                case CONNECTION_CONNECTING:
                    break;
                case CONNECTION_CONNECTED: {
                    goToMainMenu();
                }
                break;
            }
        }
    }

    private void goToMainMenu() {
        long connectDeltaTime = System.currentTimeMillis() - connectTimestamp;
        long delay = 1200 - connectDeltaTime;
        if(delay < 0) {
            delay = 0;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check firmware update
                FragmentHelper.switchFragment(getActivity().getSupportFragmentManager(), new MainFragment(), R.id.view_id_content, false);
            }
        }, (delay + 500));
    }

    //================================================================================
    // LumiRobotFinder broadcast receiver
    //================================================================================
    private final BroadcastReceiver mLumiFinderBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (LumiRobotFinder.LUMIRobotFinder_LUMIFound.equals(action)){
                BluetoothDevice device = (BluetoothDevice)(intent.getExtras().get("BluetoothDevice"));
                Log.d("BLE", "LumiScanFragment broadcast receiver found LUMI: " + device.getName());
                updateDongleList();
            } else if (LumiRobotFinder.LUMIRobotFinder_LUMIListCleared.equals(action)) {
                updateDongleList();
            }
        }
    };

    //================================================================================
    // LumiRobot callback
    //================================================================================
    @Override
    public void lumiDeviceReady(LumiRobot lumi) {
        // Stop scan
        scanLeDevice(false);

        // Set player lumi
        DonglePlayer.getInstance().setPlayerDongle(lumi);

        // Set connected status
        if (getFragmentActivity() != null) {
            getFragmentActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Animate connected
                    setConnectionState(CONNECTION_CONNECTED);
                }
            });
        }
    }

    @Override
    public void lumiDeviceDisconnected(LumiRobot lumi) {
        setConnectionState(CONNECTION_SCANNING);
    }
}
