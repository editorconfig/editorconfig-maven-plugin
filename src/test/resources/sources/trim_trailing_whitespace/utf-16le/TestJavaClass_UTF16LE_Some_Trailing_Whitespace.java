package com.something.other;  

public class TestJavaClass_UTF_16LE_Some_Trailing_Whitespace {  

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
