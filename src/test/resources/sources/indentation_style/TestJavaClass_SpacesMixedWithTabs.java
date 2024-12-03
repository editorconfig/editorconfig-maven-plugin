package com.something.other;

public class TestJavaClass_SpacesMixedWithTabs {

	public static final String PARAM = "PARAM";

	public static void main(String[] args) {
	    System.out.println("Hey!");

	    String property = System
		  .getProperties()
		  .getProperty(PARAM);

	    if (property == null)
		return;

	    System.out.println(property);
	}
}
