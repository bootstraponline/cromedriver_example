package test_util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.openqa.selenium.chrome.ChromeDriverService;

public abstract class Util {
	public static final String chromeDriverVersion = "20.0.1133.0";

	public static String timeNow() {
		return DateFormat.getDateTimeInstance().format(new Date());
	}

	/**
	 * Reads in key from sauce.key in repository root.
	 * http://username:---@ondemand.saucelabs.com:80/wd/hub
	 **/
	public static String getSauceKey() {
		try {
			final File key = new File("sauce.key");
			final byte[] data = new byte[(int) key.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(key));
			dis.readFully(data);
			dis.close();

			return new String(data).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ChromeDriverService createAndStartService() {

		// Chrome must exist.
		if (!new File("/opt/google/chrome/google-chrome").exists()) {
			Assert.fail("Chrome symlink does not exist at /opt/google/chrome/google-chrome");
		}

		final String driver = Util.is64Bit() ? "chromedriver_linux64_"
				+ chromeDriverVersion : "chromedriver_linux32_"
				+ chromeDriverVersion;
		final File driverFile = new File("driver" + File.separator + driver);

		// Ensure executable bit is set on driver.
		driverFile.setExecutable(true);

		final ChromeDriverService service = new ChromeDriverService.Builder()
				.usingChromeDriverExecutable(driverFile).usingAnyFreePort()
				.build();
		try {
			service.start();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return service;
	}

	/**
	 * Only tested on Ubuntu. Will not work on windows.
	 * 
	 * http://stackoverflow.com/
	 * questions/4748673/how-can-i-check-the-bitness-of-
	 * my-os-using-java-j2se-not-os-arch
	 */
	public static boolean is64Bit() {
		final String arch = System.getProperty("os.arch");
		if (arch == null)
			return false;

		return arch.endsWith("64");
	}
}
