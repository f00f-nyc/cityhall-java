package com.digitalBorderlands.cityHall.test;

import com.digitalBorderlands.cityHall.Settings;
import com.digitalBorderlands.cityHall.data.UserInfo;
import com.digitalBorderlands.cityHall.data.comm.Client;

public class PasswordTest {

	public static void main(String[] args) {
		try
		{
			Client client = new Client("localhost:5000/api");
			Settings cityhall = new Settings("alex", "", client);
			/*System.out.println(
					cityhall.values.get("demo/report_size/", "dev", "SNAPE")
			);*/
			
			//cityhall.values.set("demo/report_size", "dev", "SNAPE", "123");
			
			/*History hist = cityhall.values.getHistory("demo/report_size", "dev", "SNAPE");
			System.out.printf("length: %d\n", hist.entries.length);
			System.out.printf("entry 3 name=%s, value=%s, date=%s", hist.entries[3].name, hist.entries[3].value, hist.entries[3].datetime);*/
			
			/*Children node = cityhall.values.getChildren("demo/", "dev");
			System.out.printf("number of children: %d\n", node.children.length);
			System.out.printf("child 2: path=%s, value=%s. override=%s\n", node.children[2].path, node.children[2].value, node.children[2].override);*/
			
			/* cityhall.values.set("demo/test1", "dev", "", "value1");
			System.out.println(cityhall.get("demo/test1"));
			cityhall.values.delete("/demo/test1", "dev", "");
			System.out.println(cityhall.get("demo/test1")); */
			
			/* System.out.println(cityhall.environments.getDefaultEnviornment());
			cityhall.environments.setDefaultEnvironment("dev") */
			
			/* EnvironmentInfo users = cityhall.environments.get("dev");
			System.out.println(users.rights.length);
			System.out.printf("%s: %s", users.rights[0].user, users.rights[0].rights); */
			
			UserInfo info = cityhall.users.getUser("alex");
			System.out.println(info.rights.length);
			System.out.printf("%s: %s", info.rights[0].environment, info.rights[0].rights);
			
			cityhall.logout();
		} catch (Exception ex) {
			System.out.print("Caught exception executing: " + ex.getMessage());
		}
	}

}
