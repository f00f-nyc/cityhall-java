package com.digitalBorderlands.cityHall.impl;

import org.apache.commons.lang3.StringUtils;

public class Path {
    public static String sanitize(String path) {
    	if (StringUtils.isBlank(path) || path.equals("/")) {
    		return "/";
    	}
    	
    	char first = path.charAt(0);
    	char last = path.charAt(path.length()-1);

    	if ((first == '/') && (last == '/')) {
    		return path;
    	} else if ((first != '/') && (last == '/')) {
    		return String.format("/%s", path);
    	} else if ((first == '/') && (last != '/')) {
    		return String.format("%s/", path);
    	}
    	
    	return String.format("/%s/", path);
    }
}
