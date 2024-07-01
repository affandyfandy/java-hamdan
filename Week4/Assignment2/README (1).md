
# Explanation and Examples of Spring Annotations

## @Configuration
- **Explanation**: Indicates that the class can be used by the Spring IoC container as a source of bean definitions.
- **Example**:
  ```java
  @Configuration
  public class AppConfig {
      @Bean
      public MyBean myBean() {
          return new MyBean();
      }
  }

  public class MyBean {
      public void doSomething() {
          System.out.println("MyBean is doing something!");
      }
  }

  public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyBean myBean = context.getBean(MyBean.class);
      myBean.doSomething();
  }
  ```
- **Output**:
  ```plaintext
  MyBean is doing something!
  ```

## @Bean
- **Explanation**: Indicates that a method produces a bean to be managed by the Spring container.
- **Example**: Same as above.
- **Output**: Same as above.

## @ComponentScan
- **Explanation**: Configures component scanning directives for use with @Configuration classes.
- **Example**:
  ```java
  @Configuration
  @ComponentScan(basePackages = "com.example")
  public class AppConfig {
  }

  @Component
  public class MyComponent {
      public void doSomething() {
          System.out.println("MyComponent is doing something!");
      }
  }

  public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyComponent myComponent = context.getBean(MyComponent.class);
      myComponent.doSomething();
  }
  ```
- **Output**:
  ```plaintext
  MyComponent is doing something!
  ```

## @Component
- **Explanation**: Indicates that an annotated class is a "component". Such classes are considered as candidates for auto-detection when using annotation-based configuration and classpath scanning.
- **Example**: Same as above.
- **Output**: Same as above.

## @Service
- **Explanation**: Indicates that an annotated class is a "Service". This annotation is a specialization of @Component.
- **Example**:
  ```java
  @Service
  public class MyService {
      public void doSomething() {
          System.out.println("MyService is doing something!");
      }
  }

  @Configuration
  @ComponentScan(basePackages = "com.example")
  public class AppConfig {
  }

  public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyService myService = context.getBean(MyService.class);
      myService.doSomething();
  }
  ```
- **Output**:
  ```plaintext
  MyService is doing something!
  ```

## @Repository
- **Explanation**: Indicates that an annotated class is a "Repository". This annotation is a specialization of @Component.
- **Example**:
  ```java
  @Repository
  public class MyRepository {
      public void doSomething() {
          System.out.println("MyRepository is doing something!");
      }
  }

  @Configuration
  @ComponentScan(basePackages = "com.example")
  public class AppConfig {
  }

  public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyRepository myRepository = context.getBean(MyRepository.class);
      myRepository.doSomething();
  }
  ```
- **Output**:
  ```plaintext
  MyRepository is doing something!
  ```

## @Autowired
- **Explanation**: Marks a constructor, field, setter method, or config method to be autowired by Spring's dependency injection facilities.
- **Example**:
  ```java
  @Component
  public class MyComponent {
      private MyService myService;

      @Autowired
      public MyComponent(MyService myService) {
          this.myService = myService;
      }

      public void doSomething() {
          myService.doSomething();
      }
  }

  @Service
  public class MyService {
      public void doSomething() {
          System.out.println("MyService is doing something!");
      }
  }

  @Configuration
  @ComponentScan(basePackages = "com.example")
  public class AppConfig {
  }

  public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyComponent myComponent = context.getBean(MyComponent.class);
      myComponent.doSomething();
  }
  ```
- **Output**:
  ```plaintext
  MyService is doing something!
  ```

## @Scope
- **Explanation**: Configures the scope of the bean. Common scopes are "singleton" and "prototype".
- **Example**:
  ```java
  @Component
  @Scope("prototype")
  public class MyPrototypeBean {
      public void doSomething() {
          System.out.println("MyPrototypeBean is doing something!");
      }
  }

  @Configuration
  @ComponentScan(basePackages = "com.example")
  public class AppConfig {
  }

  public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyPrototypeBean bean1 = context.getBean(MyPrototypeBean.class);
      MyPrototypeBean bean2 = context.getBean(MyPrototypeBean.class);
      bean1.doSomething();
      bean2.doSomething();
      System.out.println(bean1 == bean2); // Should print false
  }
  ```
- **Output**:
  ```plaintext
  MyPrototypeBean is doing something!
  MyPrototypeBean is doing something!
  false
  ```

## @Qualifier
- **Explanation**: Indicates a specific bean to be injected when multiple beans of the same type are present.
- **Example**:
  ```java
  @Component
  public class MyComponent {
      private MyBean myBean;

      @Autowired
      public MyComponent(@Qualifier("specificBean") MyBean myBean) {
          this.myBean = myBean;
      }

      public void doSomething() {
          myBean.doSomething();
      }
  }

  @Component("specificBean")
  public class MySpecificBean implements MyBean {
      @Override
      public void doSomething() {
          System.out.println("MySpecificBean is doing something!");
      }
  }

  @Component("anotherBean")
  public class MyAnotherBean implements MyBean {
      @Override
      public void doSomething() {
          System.out.println("MyAnotherBean is doing something!");
      }
  }

  public interface MyBean {
      void doSomething();
  }

  @Configuration
  @ComponentScan(basePackages = "com.example")
  public class AppConfig {
  }

  public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyComponent myComponent = context.getBean(MyComponent.class);
      myComponent.doSomething();
  }
  ```
- **Output**:
  ```plaintext
  MySpecificBean is doing something!
  ```

## @PropertySource, @Value
- **Explanation**: 
  - `@PropertySource` is used to specify the location of a properties file to be used by the Spring Environment.
  - `@Value` is used to inject values from a properties file into fields.
- **Example**:
  ```java
  @Configuration
  @PropertySource("classpath:application.properties")
  public class AppConfig {
      @Value("${my.property}")
      private String myProperty;

      @Bean
      public MyBean myBean() {
          return new MyBean(myProperty);
      }
  }

  public class MyBean {
      private String myProperty;

      public MyBean(String myProperty) {
          this.myProperty = myProperty;
      }

      public void doSomething() {
          System.out.println("My property value: " + myProperty);
      }
  }

  public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyBean myBean = context.getBean(MyBean.class);
      myBean.doSomething();
  }
  ```
- **Output**:
  ```plaintext
  My property value: someValueFromPropertiesFile
  ```

## @PreDestroy, @PostConstruct
- **Explanation**: 
  - `@PostConstruct` is used on a method that needs to be executed after dependency injection is done to perform any initialization.
  - `@PreDestroy` is used on methods as a callback notification to signal that the instance is in the process of being removed by the container.
- **Example**:
  ```java
  @Component
  public class MyComponent {
      @PostConstruct
      public void init() {
          System.out.println("Initialization code");
      }

      @PreDestroy
      public void destroy() {
          System.out.println("Cleanup code");
      }

      public void doSomething() {
          System.out.println("MyComponent is doing something!");
      }
  }

  @Configuration
  @ComponentScan(basePackages = "com.example")
  public class AppConfig {
  }

  public static void main(String[] args) {
      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyComponent myComponent = context.getBean(MyComponent.class);
      myComponent.doSomething();
      context.close();
  }
  ```
- **Output**:
  ```plaintext
  Initialization code
  MyComponent is doing something!
  Cleanup code
  ```

This covers the explanations, examples, and outputs for each of the Spring annotations.
