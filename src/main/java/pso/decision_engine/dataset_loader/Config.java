package pso.decision_engine.dataset_loader;

import lombok.Data;

@Data
public class Config {
	private String dbDriverClass;
	private String dbConnectionString;
	private String dbUser;
	private String dbPassword;
	private String sql;
	private String dataSetName;
	private String dataSetType;
	private String header;
	private String decisionEngineUrl;
	private String decisionEngineUser;
	private String decisionEnginePassword;
	
	private boolean useProxyServer;
	private String proxyServerHost;
	private int proxyServerPort;
}
