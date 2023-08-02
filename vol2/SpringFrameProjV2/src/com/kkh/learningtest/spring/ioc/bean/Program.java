package com.kkh.learningtest.spring.ioc.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Program {
  
  public static void main(String[] args) {
    
      ApplicationContext ctx = 
          new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
      
      AnnotatedHello hello = 
          ctx.getBean("annotatedHello", AnnotatedHello.class);
      AnnotatedHelloConfig config = 
          ctx.getBean("annotatedHelloConfig", AnnotatedHelloConfig.class);
      
      AnnotatedHello confighello = config.annotatedHello();
      
      System.out.println(config.equals(confighello));
  }
}
