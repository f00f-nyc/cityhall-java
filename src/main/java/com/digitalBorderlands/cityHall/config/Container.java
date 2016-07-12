package com.digitalBorderlands.cityHall.config;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.parameters.ComponentParameter;

import com.digitalBorderlands.cityHall.data.comm.Client;

public class Container {
	// TODO: The value of this url should come from config
	
	public static MutablePicoContainer Self = new DefaultPicoContainer()
			.addComponent(Client.class, Client.class, new ComponentParameter("http://not.a.real.uri/"));
	
	public static Client getClient() {
		return Container.Self.getComponent(Client.class);
	}
}
