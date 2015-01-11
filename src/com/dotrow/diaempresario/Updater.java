package com.dotrow.diaempresario;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 21/01/14 07:02 PM
 */
public class Updater {
	private static Updater ourInstance = new Updater();

	public static Updater getInstance() {
		return ourInstance;
	}

	private Updater() {
	}
}
