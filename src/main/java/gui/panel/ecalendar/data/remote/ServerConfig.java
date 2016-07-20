package gui.panel.ecalendar.data.remote;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {

	private ServerConfig() {
		loadProperties();
	}

	private void loadProperties() {
		FileInputStream fis;
		Properties property = new Properties();

		try {
			property.load(new FileInputStream("config.properties"));
			host = property.getProperty("db.host");
			port = Integer.valueOf(property.getProperty("db.port"));
			login = property.getProperty("db.login");
			password = property.getProperty("db.password");
		} catch (IOException ex) {
			System.out.println("Error: config.properties is not found. EX: " + ex);
		}
	}

	public static ServerConfig getInstance() {
		if (instance == null) {
			instance = new ServerConfig();
		}
		return instance;
	}

	public String getHost() {
		return host;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	private String host;
	private int port;
	private String login;
	private String password;
	private static ServerConfig instance = null;
}
