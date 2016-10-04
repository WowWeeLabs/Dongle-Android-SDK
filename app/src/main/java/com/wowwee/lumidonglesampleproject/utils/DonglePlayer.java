package com.wowwee.lumidonglesampleproject.utils;

import com.wowwee.bluetoothrobotcontrollib.lumi.LumiRobot;

public class DonglePlayer {

	// Singleton
	private static DonglePlayer instance = null;

	private LumiRobot playerDongle = null;

	//================================================================================
    // Singleton
    //================================================================================

	public static DonglePlayer getInstance(){
		if (instance == null) {
			instance = new DonglePlayer();
		}
		return instance;
	}

	//================================================================================
    // Constructor
    //================================================================================

	public DonglePlayer() {
		super();
	}
	
	//================================================================================
    // Setter / Getter
    //================================================================================
	
	public void setPlayerDongle(LumiRobot dongle) {
		playerDongle = dongle;
	}
	
	public LumiRobot getPlayerDongle() {
		return playerDongle;
	}
}
