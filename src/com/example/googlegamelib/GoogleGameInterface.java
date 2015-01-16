package com.example.googlegamelib;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.games.Games;

/**
 * 
 * @author chendd
 * @version 1.0.0
 * @date 2015/01/09
 * @description Class to support Google Play Game Services.
 *              Include submitscore ,achievement ,leaderboard ...
 */
public class GoogleGameInterface {

	// Important variables
	private static Context context = null;
	private static MainActivity app = null;
	private static GoogleGameInterface instance = null;

	// ID's
	private static final int REQUEST_ACHIEVEMENTS = 10000;
	private static final int REQUEST_LEADERBOARDS = 10001;
	private static final int REQUEST_LEADERBOARD = 10002;

	// TAG
	private static final String TAG = "GOOGLEGAME_TAG";

	/**
	 * Singleton
	 */
	public static GoogleGameInterface sharedInstance() {
		if (instance == null)
			instance = new GoogleGameInterface();
		return instance;
	}

	/**
	 * Configure Context
	 * cocos2d-x.
	 */
	public static void configure(Context context) {
		GoogleGameInterface.context = context;
		GoogleGameInterface.app = (MainActivity) GoogleGameInterface.context;
	}

	/**
	 * Show Message
	 * @param message
	 */
	public static void displayAlert(final String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setNeutralButton(
				context.getResources().getString(android.R.string.ok), null);
		builder.create().show();
	}

	/*
	 * Google play games services methods. Requirements:
	 * google-play-services_lib as library.
	 */

	/**
	 * Check if user is Signed In.
	 */
	public static boolean isSignedIn() {
		return app.mHelper.isSignedIn();
	}

	/**
	 * Google Play Games Services Sign In
	 */
	public static void gameServicesSignIn() {
		app.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (!isSignedIn())
					app.mHelper.beginUserInitiatedSignIn();
			}
		});
	}

	/**
	 * Google Play Games Services Sign Out
	 */
	public static void gameServicesSignOut() {
		app.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isSignedIn())
					app.mHelper.signOut();
			}
		});
	}

	/**
	 * Submit a score in a leaderboard.
	 * 
	 * @param leaderboardID(from google play developer console)
	 * @param score
	 */
	public static void submitScore(final String leaderboardID, final long score) {
		app.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isSignedIn()) {
					Log.d(TAG, "Submit score " + score + " to " + leaderboardID);

					Games.Leaderboards.submitScore(app.getApiClient(),
							leaderboardID, score);

				} else {
//					String message = context.getResources().getString(
//							R.string.fail_submit_score_leaderboard);
//					message = message.replace("{score}", score + "");
//					message = message.replace("{leaderboardID}", leaderboardID);
//					displayAlert(message);
					gameServicesSignIn();
				}
			}
		});

	}

	/**
	 * Unlock an achievement.
	 * 
	 * @param achievementId(from google play developer console)
	 */
	public static void unlockAchievement(final String achievementID) {
		app.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isSignedIn()) {
					Log.d(TAG, "Try to unlock achievement " + achievementID);
					Games.Achievements.unlock(app.getApiClient(),
							achievementID);
				} else {
//					String message = context.getResources().getString(
//							R.string.fail_unlock_achievement);
//					message = message.replace("{achievementID}", achievementID);
//					displayAlert(message);
					gameServicesSignIn();
				}
			}
		});
	}

	/**
	 * Increment the achievement in numSteps.
	 * 
	 * @param achievementId(from google play developer console)
	 * @param numSteps
	 */
	public static void incrementAchievement(final String achievementID,
			final int numSteps) {
		app.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isSignedIn())
					Games.Achievements.increment(app.getApiClient(),
							achievementID, numSteps);
				else {
//					String message = context.getResources().getString(
//							R.string.fail_increment_achievement);
//					message = message.replace("{achievementID}", achievementID);
//					message = message.replace("{numSteps}", numSteps + "");
//					displayAlert(message);
					gameServicesSignIn();
				}
			}
		});
	}

	/**
	 * Show all achievements.
	 */
	public static void showAchievements() {
		app.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isSignedIn())
					app.startActivityForResult(Games.Achievements
							.getAchievementsIntent(app.getApiClient()),
							REQUEST_ACHIEVEMENTS);
				else {
//					String message = context.getResources().getString(
//							R.string.fail_show_achievements);
//					displayAlert(message);
					gameServicesSignIn();
				}

			}
		});

	}

	/**
	 * Show all leaderboard.
	 */
	public static void showLeaderboards() {

		app.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isSignedIn())
					app.startActivityForResult(
							Games.Leaderboards.getAllLeaderboardsIntent(app
									.getApiClient()),
							REQUEST_LEADERBOARDS);
				else {
//					String message = context.getResources().getString(
//							R.string.fail_show_leaderboards);
//					displayAlert(message);
					gameServicesSignIn();
				}
			}
		});

	}

	/**
	 * Show single leaderboard.
	 * @param leaderboardID(from google play developer console)
	 */
	public static void showLeaderboard(final String leaderboardID) {

		app.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isSignedIn())
					app.startActivityForResult(
							Games.Leaderboards.getLeaderboardIntent(
									app.getApiClient(), leaderboardID),
							REQUEST_LEADERBOARD);
				else {
//					String message = context.getResources().getString(
//							R.string.fail_show_leaderboard);
//					message = message.replace("{leaderboardID}", leaderboardID);
//					displayAlert(message);
					gameServicesSignIn();
				}
			}
		});

	}

}
