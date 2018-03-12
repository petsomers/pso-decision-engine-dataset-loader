package pso.decision_engine.dataset_loader;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {

	public static void main(String[] args) {
		if (args.length==0) {
			System.out.println("Please provide a config file location.");
			System.exit(-1);
			return;
		}
		try {
			Config c=ConfigLoaderService.getConfig(args[0]);
			String tempFileName=c.getDataSetName()+"-"+DateTimeFormatter.ofPattern("yyMMddHHmmssms").format(LocalDateTime.now())+".txt";
			File f=new File("temp");
			if (!f.exists()) {
				f.mkdirs();
			}
			String header=null;
			if (c.getDataSetType().equals("LOOKUP")) {
				header=String.join("\t", c.getHeader().split(","));
			};
			
			Path tempout=Paths.get("temp",tempFileName);
			System.out.print("Retreiving data from database");
			Class.forName(c.getDbDriverClass());
			try (Connection conn = DriverManager.getConnection(c.getDbConnectionString(), c.getDbUser(), c.getDbPassword())) {
			try(BufferedWriter writer = Files.newBufferedWriter(tempout, Charset.forName("UTF-8"))) {
				if (header!=null) writer.write(header+"\r\n");
			try (Statement stmt = conn.createStatement()) {
			try (ResultSet rs = stmt.executeQuery( c.getSql() )) {
				int columnCount=rs.getMetaData().getColumnCount();
				while (rs.next()) {
					for (int col=1;col<=columnCount;col++) {
						writer.write(clean(rs.getString(col)));
						if (col!=columnCount) writer.write('\t');
					}
					writer.write("\r\n");
				}
				writer.flush();
			}}}}
			System.out.println(" -> Done");
			System.out.println("Sending data");
			
			HttpClient.send(c, tempout.toFile());
			
			System.out.println(" -> Done");
		} catch (Exception e) {
			System.out.println("FATAL ERROR: "+e.getMessage());
			e.printStackTrace();
			System.exit(-1);
			return;
		}
	}
	
	static private String clean(String s) {
		return s==null?"":s.trim();
	}

}
