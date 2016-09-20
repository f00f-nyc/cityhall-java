This is the Java library for City Hall Enterprise Settings Server

# ABOUT

This project can be installed using:

```XML
    <dependency>  
    	<groupId>com.digitalBorderlands</groupId>
  		<artifactId>cityHall</artifactId>
  		<version>1.0</version>
    </dependency>
```

USAGE

The intention is to use the built-in City Hall web site for actual settings management, and then use this library for consuming those settings, in an application. As such, there is really only one command to be familiar with:

```Java
	Settings cityhall = Settings.create();
	String value = cityhall.get("/app/some_value");
```

For more functionality (creating/deleting values, audit trails, creating users, environments, etc), please check the javadocs.

LICENSE

This code is licensed under the MIT License
