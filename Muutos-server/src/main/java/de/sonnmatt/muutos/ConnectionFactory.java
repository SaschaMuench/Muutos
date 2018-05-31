package de.sonnmatt.muutos;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * @author MuenSasc
 *
 * ConnectionFactory for the system
 * Needed configurations:
 * tomcat context.xml
 * 		<ResourceLink global="jdbc/MuutosDB" name="jdbc/MuutosDB" type="javax.sql.DataSource"/>
 * Tomcat server.xml
 * 		<Resource auth="Container" 
 * 			description="Muutos database for development" 
 * 			driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver" 
 * 			name="jdbc/MuutosDB" 
 * 			password="SECRET" 
 * 			type="javax.sql.DataSource" 
 * 			url="jdbc:sqlserver://SERVER;instanceName=INSTANCE;portNumber=PORT;databaseName=DATABASE" 
 * 			username="LOGIN"/>
 */
public class ConnectionFactory {

	private static interface Singleton {
		final ConnectionFactory INSTANCE = new ConnectionFactory();
	}

	private DataSource dataSource;

	private ConnectionFactory() {
		DataSource ds = null; 
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:/comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/MuutosDB");
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.dataSource = ds;	
	}

	public static Connection getDatabaseConnection() throws SQLException {
		return Singleton.INSTANCE.dataSource.getConnection();
	}
}
