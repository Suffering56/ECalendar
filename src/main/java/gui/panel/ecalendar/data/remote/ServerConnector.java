package gui.panel.ecalendar.data.remote;

import prime_tass.connect.BadParametersException;
import prime_tass.connect.client_api.ConnectionClientAPI;
import prime_tass.connect.client_api.ConnectionClientAPI.TYPE;
import prime_tass.connect.client_api.interf.INetworkStatusInterface;

public class ServerConnector {

	private ServerConnector() {
		initNetworkListener();
		initConnectionClient();
	}

	private void initNetworkListener() {
		networkListener = new INetworkStatusInterface() {
			public void connectionEstablished() {
//				System.out.println("Connection Established");
			}

			public void networkActivity() {
//				System.out.println("Network Activity");
			}

			public void serverRefusedConnection(String reason) {
				System.out.println("server Refused Connection");
			}

			public void wrongCredentials() {
				System.out.println("Wrong credentials");
			}

			public void disconnectedFromServer() {
				System.out.println("Disconnected From Server");
			}

			public void serverAcceptedCredentials() {
//				System.out.println("serverAcceptedCredentials");
			}
		};
	}

	private void initConnectionClient() {
		try {
			ServerConfig config = ServerConfig.getInstance();

			connectionClient = new ConnectionClientAPI(networkListener);
			connectionClient.assignNewConnectCredentials(config.getLogin(), config.getPassword(), config.getHost(), TYPE.INFO, config.getPort(), 30000, 30000,
					100);

		} catch (BadParametersException ex) {
			System.out.println("assignNewConnectCredentials()");
		}
	}

	public static ServerConnector getInstance() {
		if (instance == null) {
			instance = new ServerConnector();
		}
		return instance;
	}

	public ConnectionClientAPI getConnectionClient() {
		return connectionClient;
	}

	private static ServerConnector instance = null;
	private ConnectionClientAPI connectionClient = null;
	private INetworkStatusInterface networkListener;
}
