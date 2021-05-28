# Basic architecture of Spring Boot service

This repo is an example of Spring Boot Service with tools which are helpful to do the development

## Structure

    HTTP ---> Controller ---> Component ---> PersistanceService ---> Repository ---> DB
                                        ---> ConnectorService ---> HTTP
                                        ---> UtilService ---> PersistanceService ---> Repository ---> DB
                                                         ---> ConnectorService ---> HTTP
                                        


- **Controller** - responsible for restful
- **Component** - responsible for business logic
- **PersistanceService** - responsible for storage logic
- **Repository** - responsible for communication with DB
- **ConnectorService** - responsible for communication with other services
- **UtilService** - responsible for reusable business logic

## Annotations

### IsDate:

Validating date fields in DTO. If not parsable by provided format throw an exception.

Type: `ConstraintValidator`

Parameters: 
- **exception** - HttpConstants which will be thrown if validation fail(example: `HttpConstants.BAD_REQUEST`)
- **format** - Datetime format which will be used to validate the format

### IsEnum:

Validating a field(Type depends from the type of value in enum) for Enum in DTO. Enum class needs to implement the BaseEnum. If its not in the list of values of enum throw an exception.

Type: `ConstraintValidator`

Parameters: 
- **exception** - HttpConstants which will be thrown if validation fail(example: `HttpConstants.BAD_REQUEST`)
- **enumClass** - Enum class which will be used to validate the value

### Max:

Validating Integer|Long field. If it's more than value, throw an exception.

Type: `ConstraintValidator`

Parameters: 
- **exception** - HttpConstants which will be thrown if validation fail(example: `HttpConstants.BAD_REQUEST`)
- **value** - Value which will be used to compare with field

### Min:

Validating Integer|Long field. If it's less than value, throw an exception.

Type: `ConstraintValidator`

Parameters: 
- **exception** - HttpConstants which will be thrown if validation fail(example: `HttpConstants.BAD_REQUEST`)
- **value** - Value which will be used to compare with field

### Required:

Validating field if its not null and not empty. If its null or empty throw an exception.

Type: `ConstraintValidator`

Parameters: 
- **exception** - HttpConstants which will be thrown if validation fail(example: `HttpConstants.BAD_REQUEST`)

### Full Example:

```
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateUserRequest {

    @Required(exception = StatusConstants.HttpConstants.CARD_ID_IS_REQUIRED)
    private String cardId;

    @Required(exception = StatusConstants.HttpConstants.FIRST_NAME_IS_REQUIRED)
    private String firstName;

    @Required(exception = StatusConstants.HttpConstants.SECOND_NAME_IS_REQUIRED)
    private String secondName;

    @Required(exception = StatusConstants.HttpConstants.TYPE_IS_REQUIRED)
    @IsEnum(enumClass = UserType.class, exception = StatusConstants.HttpConstants.TYPE_IS_INVALID)
    private String type;

    @Required(exception = StatusConstants.HttpConstants.STATUS_IS_REQUIRED)
    @IsEnum(enumClass = UserStatus.class, exception = StatusConstants.HttpConstants.STATUS_IS_INVALID)
    private Integer status;

    @Required(exception = StatusConstants.HttpConstants.DATE_OF_BIRTH_IS_REQUIRED)
    @IsDate(format = DateTimeUtils.DATE_FORMAT_YYYY_MM_DD, exception = StatusConstants.HttpConstants.DATE_OF_BIRTH_IS_INVALID)
    private String dateOfBirth;

    @Required(exception = StatusConstants.HttpConstants.AGE_IS_REQUIRED)
    @Min(value = 18, exception = StatusConstants.HttpConstants.AGE_IS_INVALID)
    @Max(value = 54, exception = StatusConstants.HttpConstants.AGE_IS_INVALID)
    private Integer age;

    @Required(exception = StatusConstants.HttpConstants.MOBILE_NUMBER_IS_REQUIRED)
    private String mobileNumber;

    @Required(exception = StatusConstants.HttpConstants.MOBILE_BRAND_IS_REQUIRED)
    private String mobileBrand;

}
```

## Converters

For converters used *MapStruct* and String *Converter<S, T>*

MapStruct is a code generator that greatly simplifies the implementation of mappings between Java bean types based on a convention over configuration approach.

Converter<S, T> is a solid type conversion SPI for developing our custom converters.

Example: 
```
@Mapper(componentModel = "spring")
public abstract class UserCommandToUserEntityConverter implements Converter<UserCommand, UserEntity> {

    public abstract UserEntity convert(UserCommand source);

}
```

**@Mapper(componentModel = "spring")** - To be able to use Spring IoC in our mapper

For fields which we can't map we use `@Mapping(source = "sourceField", target = "targetField")`

For the fields which have different types we can use abstract functions:
```
public UserStatus intToUserStatus(Integer id) {
    return UserStatus.find(id)
            .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
}
```

Converters automatically registered with `ApplicationListener<ContextRefreshedEvent>` by adding them to the `GenericConversionService` at the start of application: 

```
@Component
public class SpringContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger LOG = LoggerFactory.getLogger(SpringContextListener.class);

    @Autowired
    private Set<Converter<?, ?>> converters;

    @Autowired
    private ConversionService conversionService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        GenericConversionService genericConversionService = (GenericConversionService) conversionService;
        for(Converter<?, ?> converter : converters) {
            genericConversionService.addConverter(converter);
            LOG.info("CONVERTER REGISTERED: {} ", converter.getClass().getName());
        }
    }
}
```

###Full example:

```
@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {

    private String cardId;

    private String firstName;

    private String secondName;

    private String type;

    private Integer status;

    private String dateOfBirth;

    private Integer age;

    private String mobileNumber;

    private String mobileBrand;

}
```

```
@Getter
@Setter
@NoArgsConstructor
public class UserCommand {

    private Long id;

    private String cardId;

    private String firstName;

    private String secondName;

    private UserType type;

    private UserStatus status;

    private UserLevel level;

    private String dateOfBirth;

    private Integer age;

    private MobileCommand mobile;

}
```

```
@Getter
@Setter
@NoArgsConstructor
public class MobileCommand {

    private Long id;

    private Long userId;

    private String mobileNumber;

    private String mobileBrand;

}
```

```
@Mapper(componentModel = "spring")
public abstract class CreateUserRequestToUserCommandConverter implements Converter<CreateUserRequest, UserCommand> {

    @Mapping(source = "mobileNumber", target = "mobile.mobileNumber")
    @Mapping(source = "mobileBrand", target = "mobile.mobileBrand")
    public abstract UserCommand convert(CreateUserRequest source);

    public UserStatus intToUserStatus(Integer id) {
        return UserStatus.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

    public UserType stringToUserType(String id) {
        return UserType.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

    public UserLevel intToUserLevel(Integer id) {
        return UserLevel.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

}
``` 

## Exceptions

For the exception handler used `@ControllerAdvice` with `ResponseEntityExceptionHandler`

`@ControllerAdvice` is a specialization of the @Component annotation which allows to handle exceptions across the whole application in one global handling component. It can be viewed as an interceptor of exceptions thrown by methods annotated with @RequestMapping and similar.

`ResponseEntityExceptionHandler` is a convenient base class for controller advice classes. It provides exception handlers for internal Spring exceptions. If we don’t extend it, then all the exceptions will be redirected to DefaultHandlerExceptionResolver which returns a ModelAndView object. Since we are on the mission to shape our own error response, we don’t want that.

Example: 

```
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = {InvalidRequestException.class})
    protected ResponseEntity<Object> handleInvalidRequestException(RuntimeException e, HttpServletRequest request) {
        LOG.error("Failed {} {}: {}", request.getMethod(), request.getServletPath(), e.getMessage());
        return new ResponseEntity<>(new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

## Logging for controllers

Logging for controllers is done via `HandlerInterceptorAdapter` and `WebMvcConfigurer`

`WebMvcConfigurer` - allow us to register our interceptors

`HandlerInterceptorAdapter` - Abstract adapter class for the AsyncHandlerInterceptor interface, for simplified implementation of pre-only/post-only interceptors

The `HandlerInterceptorAdapter` have 3 methods: 

`preHandle()` - method will be called before the actual handler is executed. This method returns a boolean value which can be used to continue or break the control flowing to the DispatcherServlet.

`postHandle()` - method will be called after the handler is executed but before the view being rendered. So, you can add more model objects to the view but you can not change the HttpServletResponse

`afterCompletion()` - method will be called after the request has completed and the view is rendered.

Example: 

```
@Component
public class ControllerLogsInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOG = LoggerFactory.getLogger(ControllerLogsInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Instant instant = Instant.now();
        long startTime = instant.toEpochMilli();
        LOG.info("Started {} {}", request.getMethod(), request.getServletPath());
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        LOG.info("Finished {} {}, Status: {}, Duration: {}", request.getMethod(), request.getServletPath(), response.getStatus(), (Instant.now().toEpochMilli() - startTime));
    }

}
```

## Unit tests

For unit tests used JUnit5, Assertj and Spring boot integration tests

Example of integration test:

```
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PassedVerificationController.class)
class PassedVerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassedVerificationComponent passedVerificationComponent;

    @Test
    @DisplayName("Add passed verification - success")
    public void testAddPassedVerification_success() throws Exception {
        AddPassedVerificationRequest actualRequest = new AddPassedVerificationRequest()
                .setUserId(1L)
                .setPassedVerificationType(PassedVerificationType.TYPE_1);

        MockHttpServletResponse response = mockMvc.perform(post("/v1/passed-verifications")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        //Check passing arguments of passedVerificationComponent.addPassedVerification
        ArgumentCaptor<AddPassedVerificationRequest> addPassedVerificationRequestArgumentCaptor = ArgumentCaptor.forClass(AddPassedVerificationRequest.class);
        Mockito.verify(passedVerificationComponent, Mockito.times(1)).addPassedVerification(addPassedVerificationRequestArgumentCaptor.capture());
        Assertions.assertThat(addPassedVerificationRequestArgumentCaptor.getValue().getUserId()).isEqualTo(1L);
        Assertions.assertThat(addPassedVerificationRequestArgumentCaptor.getValue().getPassedVerificationType()).isEqualTo(PassedVerificationType.TYPE_1);

        //Check actual response
        TypeReference<Response<Void>> typeReference = new TypeReference<Response<Void>>(){};
        Response<Void> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.SUCCESS.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.SUCCESS.getDesc());
        Assertions.assertThat(actualResponse.getData()).isNull();

    }
}
```

`@ExtendWith(SpringExtension.class)` - JUnit5 annotation instead of deprecated `@RunWith`

`@WebMvcTest(controllers = PassedVerificationController.class)` - used to run integration test for controller

`@MockBean` - used to mock bean

`@DisplayName("Add passed verification - success")` -  used to show custom name in the result

`Assertions.assertThat` - is used instead of `Assert.equalTo`. Giving us more flexability for validation.

`ArgumentCaptor` - allows us to capture an argument passed to a method in order to inspect it. This is especially useful when we can't access the argument outside of the method we'd like to test.

Example of unit test:

```
@ExtendWith(MockitoExtension.class)
class UserComponentTest {

    @InjectMocks
    private UserComponent userComponent;

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private UserUtilService userUtilService;

    @Mock
    private ConversionService conversionService;

    @Test
    public void testGetUser() {
        long id = 1L;
    
        UserCommand userCommand = new UserCommand()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));
    
        Mockito.when(userPersistenceService.findById(id))
                .thenReturn(Optional.of(userCommand));
    
        UserCommand actualResponse = userComponent.getUser(id);
    
        //Check actual response
        Assertions.assertThat(actualResponse.getCardId()).isEqualTo("cardId");
        Assertions.assertThat(actualResponse.getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(actualResponse.getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(actualResponse.getType()).isEqualTo(UserType.USER);
        Assertions.assertThat(actualResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(actualResponse.getDateOfBirth()).isEqualTo("11-11-1991");
        Assertions.assertThat(actualResponse.getAge()).isEqualTo(18);
        Assertions.assertThat(actualResponse.getMobile().getMobileNumber()).isEqualTo("12345678901");
        Assertions.assertThat(actualResponse.getMobile().getMobileBrand()).isEqualTo("Apple");
    
        Mockito.verify(userPersistenceService, Mockito.times(1)).findById(id);
    }
}
```

### Recommendations for tests

**Use the Prefixes `actual*` and `expected*`**

```
// Don't
ProductDTO product1 = requestProduct(1);

ProductDTO product2 = new ProductDTO("1", List.of(State.ACTIVE, State.REJECTED))
assertThat(product1).isEqualTo(product2);
If you are going to use variables in an equals assertion, prefix the variables with “actual” and “expected”. This increases the readability and clearifies the intention of the variable. Moreover, it’s harder to mix them up in the equals assertion.
```

```
// Do
ProductDTO actualProduct = requestProduct(1);

ProductDTO expectedProduct = new ProductDTO("1", List.of(State.ACTIVE, State.REJECTED))
assertThat(actualProduct).isEqualTo(expectedProduct); // nice and clear.
```

**Use Fixed Data Instead of Randomized Data**

Avoid randomized data as it can lead to toggling tests which can be hard to debug and omit error messages that make tracing the error back to the code harder.

```
// Don't
Instant ts1 = Instant.now(); // 1557582788
Instant ts2 = ts1.plusSeconds(1); // 1557582789
int randomAmount = new Random().nextInt(500); // 232
UUID uuid = UUID.randomUUID(); // d5d1f61b-0a8b-42be-b05a-bd458bb563ad
Instead, use fixed values for everything. They will create highly reproducible tests, which are easy to debug and create error messages that can be easily traced back to the relevant line of code.
```

```
// Do
Instant ts1 = Instant.ofEpochSecond(1550000001);
Instant ts2 = Instant.ofEpochSecond(1550000002);
int amount = 50;
UUID uuid = UUID.fromString("00000000-000-0000-0000-000000000001");
```

**Don’t Hide the Relevant Parameters (in Helper Functions)**
```
// Don't
insertIntoDatabase(createProduct());
List<ProductDTO> actualProducts = requestProductsByCategory();
assertThat(actualProducts).containsOnly(new ProductDTO("1", "Office"));
Yes, you should use helper functions for creating data and assertions, but you have to parameterize them. Define a parameter for everything that is important for the test and needs to be controlled by the test. Don’t force the reader to jump to a function definition in order to understand the test. Rule of thumb: You should see the essentials of a test by looking only at the test method.
```

```
// Do
insertIntoDatabase(createProduct("1", "Office"));
List<ProductDTO> actualProducts = requestProductsByCategory("Office");
assertThat(actualProducts).containsOnly(new ProductDTO("1", "Office"));
```

**Use AssertJ**

AssertJ is an extremely powerful and mature assertion library with a fluent type-safe API, a huge variety of assertions and descriptive failure messages. There is an assertion for everything you want to do. This prevents you from writing complex assertion logic with loops and conditions while keeping the test code short. Here are some examples:
```
assertThat(actualProduct)
        .isEqualToIgnoringGivenFields(expectedProduct, "id");

assertThat(actualProductList).containsExactly(
        createProductDTO("1", "Smartphone", 250.00),
        createProductDTO("1", "Smartphone", 250.00)
);

assertThat(actualProductList)
        .usingElementComparatorIgnoringFields("id")
        .containsExactly(expectedProduct1, expectedProduct2);

assertThat(actualProductList)
        .extracting(Product::getId)
        .containsExactly("1", "2");

assertThat(actualProductList)
        .anySatisfy(product -> assertThat(product.getDateCreated()).isBetween(instant1, instant2));

assertThat(actualProductList)
        .filteredOn(product -> product.getCategory().equals("Smartphone"))
        .allSatisfy(product -> assertThat(product.isLiked()).isTrue());
```

**Avoid assertTrue() and assertFalse()**

Avoid simple `assertTrue()` or `assertFalse()` assertions as they produce cryptic failure messages:
```
// Don't 
assertTrue(actualProductList.contains(expectedProduct));
assertTrue(actualProductList.size() == 5);
assertTrue(actualProduct instanceof Product);
expected: <true> but was: <false>
Instead, use AssertJ’s assertions which produce nice failure messages out-of-the-box.
```

```
// Do
assertThat(actualProductList).contains(expectedProduct);
assertThat(actualProductList).hasSize(5);
assertThat(actualProduct).isInstanceOf(Product.class);
```

```
Expecting:
 <[Product[id=1, name='Samsung Galaxy']]>
to contain:
 <[Product[id=2, name='iPhone']]>
but could not find:
 <[Product[id=2, name='iPhone']]>
If you really have to check for a boolean, consider AssertJ’s as() to improve the failure message.
```

**Use Parameterized Tests**

Parameterized Tests allow rerunning a single test multiple times with different values. This way, you can easily test several cases without writing more test code. JUnit5 provides great means to write those tests with `@ValueSource`, `@EnumSource`, `@CsvSource`, and `@MethodSource`.
```
// Do
@ParameterizedTest
@ValueSource(strings = ["§ed2d", "sdf_", "123123", "§_sdf__dfww!"])
public void rejectedInvalidTokens(String invalidToken) {
    client.perform(get("/products").param("token", invalidToken))
            .andExpect(status().is(400))
}

@ParameterizedTest
@EnumSource(WorkflowState::class, mode = EnumSource.Mode.INCLUDE, names = ["FAILED", "SUCCEEDED"])
public void dontProcessWorkflowInCaseOfAFinalState(WorkflowState itemsInitialState) {
    // ...
}
```
I highly recommend to extensively use them, because you can test more cases with a minimal amount of effort.

Finally, I like to highlight `@CsvSource` and `@MethodSource` which can be used for more advanced parameterized test scenarios where you can also control the expected output with a parameter.

```
@ParameterizedTest
@CsvSource({
    "1, 1, 2",
    "5, 3, 8",
    "10, -20, -10"
})
public void add(int summand1, int summand2, int expectedSum) {
    assertThat(calculator.add(summand1, summand2)).isEqualTo(expectedSum);
}
```

`@MethodSource` is powerful in conjunction with a dedicated test object containing all relevant test parameters and the expected output. Unfortunately, in Java, writing those data structures (POJOs) is cumbersome. That’s why I’ll demonstrate this feature using Kotlin’s data classes.

**Don’t Use Static Access. Never. Ever.**

Static access is an anti-pattern. First, it obfuscates dependencies and side-effects making the whole code harder to understand and more error-prone. Second, static access harms testability. You can’t exchange the objects anymore. But in a test, you want to use mocks or use the real objects with a different configuration (like a DAO object pointing to a test database).

So instead of access code statically, put it into non-static methods, instantiate the class and pass the object to the constructor of the object where you need it.
```
// Don't 
public class ProductController {
    public List<ProductDTO> getProducts() {
        List<ProductEntity> products = ProductDAO.getProducts();
        return mapToDTOs(products);
    }
}
```

```
// Do 
public class ProductController {
    private ProductDAO dao;
    public ProductController(ProductDAO dao) {
        this.dao = dao;
    }
    public List<ProductDTO> getProducts() {
        List<ProductEntity> products = dao.getProducts();
        return mapToDTOs(products);
    }
}
```

Fortunately, DI frameworks like Spring are providing an easy way to avoid static access because it handles the creation and wiring of all objects for us.

## Sources

https://junit.org/junit5/docs/current/user-guide/

https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/

https://phauer.com/2019/modern-best-practices-testing-java/

https://reflectoring.io/spring-boot-exception-handling/

https://medium.com/swlh/understanding-java-8s-consumer-supplier-predicate-and-function-c1889b9423d

https://www.baeldung.com/exception-handling-for-rest-with-spring

