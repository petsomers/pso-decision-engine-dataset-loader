package pso.decision_engine.dataset_loader;

import java.io.File;
import java.io.IOException;

import jodd.props.Props;

public class ConfigLoaderService {

	public static Config getConfig(String fileName) throws IOException {
		Config c=new Config();
		Props p=new Props();
		p.load(new File(fileName));
		c.setDbDriverClass(p.getValue("dbDriverClass"));
		c.setDbConnectionString(p.getValue("dbConnectionString"));
		c.setDbUser(p.getValue("dbUser"));
		c.setDbPassword(p.getValue("dbPassword"));
		c.setSql(p.getValue("sql"));
		c.setDataSetName(p.getValue("dataSetName"));
		c.setDataSetType(p.getValue("dataSetType"));
		c.setHeader(p.getValue("header"));
		c.setDecisionEngineUrl(p.getValue("decisionEngineUrl"));
		return c;
	}
}
