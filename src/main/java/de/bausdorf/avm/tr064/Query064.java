package de.bausdorf.avm.tr064;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Query064
{
	private static final Logger LOG = LoggerFactory.getLogger(Query064.class);
	static String ip = null;
	static String user = null;
	static String password = null;
	static String serviceName = null;
	static String actionName = null;
	static String param = null;
	
	public static void main(String[] args)
	{
		if( args.length < 5 ) {
			LOG.error("args: <fb-ip> <password> <user> <service> <action> [param]");
			System.exit(1);
		} else {
			ip = args[0];
			password = args[1];
			user = args[2];
			serviceName = args[3];
			actionName = args[4];
			if( args.length > 5 )
			{
				param = args[5];
			}
		}
		//Create a new FritzConnection with username and password
		FritzConnection fc = new FritzConnection(ip,user,password);
		try {
			//The connection has to be initiated. This will load the tr64desc.xml respectively igddesc.xml 
			//and all the defined Services and Actions. 
			fc.init();
		} catch (IOException | JAXBException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}		

		//Get the Service. Returns null if not existing 
		Service service = fc.getService(serviceName);
		if( service == null ) {
			LOG.error("service " + serviceName + " does not exist");
			System.exit(1);
		}
		//Get the Action. Returns null if not existing
		Action action = service.getAction(actionName);
		if( action == null ) {
			LOG.error("action " + actionName + " does not exist for service " + serviceName);
			System.exit(1);
		}
		try {
			//Execute the action without any In-Parameter.
			Response response1 = action.execute();
			for( String key : response1.getData().keySet() ) {
				System.out.println(key + " = " + response1.getData().get(key));
			}
		} catch (UnsupportedOperationException | IOException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}

	}
	
}