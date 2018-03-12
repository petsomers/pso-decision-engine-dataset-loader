package pso.decision_engine.dataset_loader;

import java.io.File;
import java.io.IOException;

import jodd.props.Props;

public class ConfigLoaderService {

	public static Config getConfig(String fileName) throws IOException {
		Config c=new Config();
		Props p=new Props();
		p.load(new File(fileName));
		c.setDbDriverClass(p.getValue("db.driverClass"));
		c.setDbConnectionString(p.getValue("db.connectionString"));
		c.setDbUser(p.getValue("db.user"));
		c.setDbPassword(p.getValue("db.password"));
		c.setSql(p.getValue("sql"));
		c.setDataSetName(p.getValue("dataSet.name"));
		c.setDataSetType(p.getValue("dataSet.type"));
		c.setHeader(p.getValue("dataSet.header"));
		c.setDecisionEngineUrl(p.getValue("decisionEngine.url"));
		c.setDecisionEngineUser(p.getValue("decisionEngine.user"));
		c.setDecisionEnginePassword(p.getValue("decisionEngine.password"));
		c.setUseProxyServer("true".equals(p.getValue("proxyServer.use")));
		c.setProxyServerHost(p.getValue("proxyServer.host"));
		try {
			c.setProxyServerPort(Integer.parseInt(p.getValue("proxyServer.port")));
		} catch (NumberFormatException e) {}
		return c;
	}
}
