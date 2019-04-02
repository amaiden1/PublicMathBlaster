package MathBlaster;

public final class Constants {

	/*
	 * CONSTANTS ONLY. All fields must be public and static.
	 *
	 * Nothing else should go in this file. It also should
	 * not be instantiated.
	 *
	 * For easier usage, add
	 * 'import static bindingofisaac.Constants.*' to
	 * your imports.
	 */

	// Dev Mode
	public static final boolean DEV_MODE = false;

	/*
	 * Menu Constants
	 */
	public static final int
		PAUSE_RESPONSE_CONTINUE = 0,
		PAUSE_RESPONSE_MAIN_MENU = 1,
		PAUSE_RESPONSE_QUIT = 2;

	/*
	 * Controller Constants
	 */
	public static final int
		FATAL_BUTTON_DIST = 400

		;

	// Prevent instantiation of the class
	private Constants() {}

}
