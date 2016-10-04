package com.wowwee.lumidonglesampleproject.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wowwee.bluetoothrobotcontrollib.BluetoothRobotConstants;
import com.wowwee.bluetoothrobotcontrollib.RobotCommand;
import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobot;
import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobotFinder;
import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobot.LUMIRobotInterface;

import java.util.ArrayList;
import java.util.Date;

public class BaseViewFragment extends Fragment implements LUMIRobotInterface {
	private int layoutId;
	
	protected Rect viewRect;

	public LumiRobot lumiRobot;
	
	public static FragmentActivity activity;
	
	public BaseViewFragment(int layoutId) {
		this.layoutId = layoutId;
		lumiRobot = LumiRobotFinder.getInstance().firstConnectedLUMI();
		if(lumiRobot != null) {
			lumiRobot.setCallbackInterface(this);
		}
	}

	public static FragmentActivity getFragmentActivity(){
		return activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (container == null)
			return null;
		
		viewRect = new Rect();
		if (getActivity() != null){
			activity = getActivity();
		}
		activity.getWindowManager().getDefaultDisplay().getRectSize(viewRect);
		
		View view;
		if (layoutId == -1) {
			view = super.onCreateView(inflater, container, savedInstanceState);
		} else {
			view = inflater.inflate(layoutId, container, false);
		}
		
		return view;
	}

	public void scanLeDevice(final boolean enable) {
		if (enable) {
			Log.d("LumiScan", "Scan Le device start");
			// Stops scanning after a pre-defined scan period.
			LumiRobotFinder.getInstance().scanForLUMIContinuous();
		}else{
			Log.d("LumiScan", "Scan Le device stop");
			LumiRobotFinder.getInstance().stopScanForLUMIContinuous();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
	}

	@Override
	public void lumiDeviceReady(LumiRobot lumiRobot) {

	}

	@Override
	public void lumiDeviceDisconnected(LumiRobot lumiRobot) {

	}

	@Override
	public void lumiDidReceiveQuadcopterStatus(LumiRobot lumiRobot, int i) {

	}

	@Override
	public void lumiDidReceiveNotifyError(LumiRobot lumiRobot, int i) {

	}

	@Override
	public void lumiDidCalibrate(LumiRobot lumiRobot, boolean b) {

	}

	@Override
	public void lumiDidReceiveBeaconMode(LumiRobot lumiRobot, boolean b) {

	}

	@Override
	public void lumiDidReceiveAltitudeMode(LumiRobot lumiRobot, boolean b) {

	}

	@Override
	public void lumiDidReceiveSignalStrength(LumiRobot lumiRobot, int i) {

	}

	@Override
	public void lumiDidReceivePosition(LumiRobot lumiRobot, int i, int i1, int i2) {

	}

	@Override
	public void lumiDidRCTimeout(LumiRobot lumiRobot) {

	}

	@Override
	public void lumiDidReceivedIRCommand(LumiRobot lumiRobot, int i, int i1) {

	}

	@Override
	public void lumiDidReceiveUserStatus(LumiRobot lumiRobot, int i, int i1) {

	}

	@Override
	public void lumiDidReceiveFirmwareVersion(LumiRobot lumiRobot, int i, int i1) {

	}

	@Override
	public void lumiDidReceiveTestModeResponse(LumiRobot lumiRobot, String s) {

	}

	@Override
	public void lumiDidReceiveWallDetected(LumiRobot lumiRobot, int i) {

	}

	@Override
	public void lumiDidReceiveWallLost(LumiRobot lumiRobot, int i) {

	}

	@Override
	public void lumiDidReceiveWallDetectionModeResponse(LumiRobot lumiRobot, boolean b) {

	}

	@Override
	public void lumiDidReceiveCrashDetectionModeResponse(LumiRobot lumiRobot, boolean b) {

	}

	@Override
	public void lumiDidReceiveStallDetectionModeResponse(LumiRobot lumiRobot, boolean b) {

	}

	@Override
	public void lumiDidReceiveBatteryInfo(LumiRobot lumiRobot, int i) {

	}

	@Override
	public void lumiDidReceiveToyActivationStatus(LumiRobot lumiRobot, boolean b, boolean b1) {

	}

	@Override
	public void lumiDidReceiveCustomUserData(LumiRobot lumiRobot, int i, int i1) {

	}

	@Override
	public void lumiDidReceiveRawData(LumiRobot lumiRobot, ArrayList<Byte> arrayList) {

	}

	@Override
	public boolean lumiBluetoothDidProcessedReceiveRobotCommand(LumiRobot lumiRobot, RobotCommand robotCommand) {
		return false;
	}

	@Override
	public void lumiRobotFirmwareSent(int i) {

	}

	@Override
	public void lumiRobotFirmwareToChip(int i) {

	}

	@Override
	public void lumiRobotNuvotonChipstatus(BluetoothRobotConstants.nuvotonBootloaderMode nuvotonBootloaderMode) {

	}

	@Override
	public void lumiRobotFirmwareCompleteStatus(BluetoothRobotConstants.nuvotonFirmwareCompleteStatus nuvotonFirmwareCompleteStatus) {

	}

	@Override
	public void lumiRobotFirmwareDataStatus(BluetoothRobotConstants.nuvotonFirmwareStatus nuvotonFirmwareStatus) {

	}

	@Override
	public void lumiDidReceiveBatteryLevelReading(LumiRobot lumiRobot, int i) {

	}

	@Override
	public void lumiDidReceiveHardwareVersion(int i, int i1) {

	}

	@Override
	public void lumiDidReceiveSoftwareVersion(Date date, int i) {

	}

	@Override
	public void lumiDidReceiveVolumeLevel(int i) {

	}

	@Override
	public void lumiDidReceiveIRCommand(ArrayList<Byte> arrayList, int i) {

	}

	@Override
	public void lumiDidReceiveWeightReading(byte b, boolean b1) {

	}

	@Override
	public void lumiIsCurrentlyInBootloader(LumiRobot lumiRobot, boolean b) {

	}
}
