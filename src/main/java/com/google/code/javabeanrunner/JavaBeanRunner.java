package com.google.code.javabeanrunner;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * This class is a JUnit {@link org.junit.runner.Runner} implementation used for testing 
 * JavaBeans&trade; and POJOs.
 * <p>
 * The JavaBean under test is identified by the {@link Bean Bean} annotation.  The runner
 * will attempt to determine validity for a property by:
 * <ol>
 *    <li>invoking the property setter with a provided value</li>
 *    <li>invoking the property getter and comparing it against the provided value</li>
 * </ol>
 * The runner will search the test class for {@link Property Property}-annotated fields and 
 * methods.  If no fields or methods are found, the runner is unable to test that property,
 * and declares that property a test failure. 
 * <p>
 * The following snippet shows a JavaBean:
 * 
 * <pre>
 * public class Bean {
 *     private String name;
 *     
 *     public String getName() {
 *         return name;
 *     }
 *     
 *     public void setName(String name) {
 *         this.name = name;
 *     }
 * }
 * </pre>
 * and the test class used to test that bean:
 * <pre>
 * &#64;RunWith(JavaBeanRunner.class)
 * &#64;Bean(Bean.class)
 * public BeanTest {
 *     &#64;Property("name")
 *     public String name = "example";
 * }
 * </pre>
 * 
 * @author David Grant
 */
public class JavaBeanRunner extends Runner {
	private final Class<?> fixture;
	private final Constructor<?> constructor;
	private final Description description;
	private final Set<PropertyDescriptor> properties;
	private final Map<Description, Statement> stmtMap;
	
	/**
	 * Creates a new instance of this runner for the given test class.
	 * 
	 * @param testClass the test class to run.
	 * @throws Throwable if the test class cannot be used.
	 */
	public JavaBeanRunner(Class<?> testClass) throws Throwable {
		fixture = findFixture(testClass);
		properties = findMutableProperties(fixture);
		constructor = findConstructor(fixture);
		stmtMap = new HashMap<Description, Statement>();
		
		PropertyDataSource dataSource = new PropertyDataSource(testClass.newInstance());
		
		if (properties.isEmpty() || dataSource.isEmpty()) {
			description = Description.EMPTY;
		} else {
			description = Description.createSuiteDescription(testClass);
			for (PropertyDescriptor prop : properties) {
				Description childDesc = Description.createTestDescription(fixture, prop.getName());
				description.addChild(childDesc);
		
				Object target = constructor.newInstance();
				Statement stmt = new MutationStatement(dataSource, prop, target);
				stmtMap.put(childDesc, stmt);
			}
		}
	}
	
	private Set<PropertyDescriptor> findMutableProperties(Class<?> fixtureClass) throws InitializationError {
		Set<PropertyDescriptor> propSet = new HashSet<PropertyDescriptor>();
		try {
			BeanInfo info = Introspector.getBeanInfo(fixtureClass, Object.class);
			PropertyDescriptor[] props = info.getPropertyDescriptors();
			for (PropertyDescriptor prop : props) {
				if (prop.getWriteMethod() != null) {
					propSet.add(prop);
				}
			}
		} catch (IntrospectionException e) {
			throw new InitializationError(e);
		}
		return propSet;
	}

	private Class<?> findFixture(Class<?> testClass) throws InitializationError {
		Bean fixture = testClass.getAnnotation(Bean.class);
		if (fixture == null) {
			throw new InitializationError("Fixture annotation not present");
		}
		return fixture.value();
	}

	private Constructor<?> findConstructor(Class<?> fixtureClass) throws InitializationError {
		try {
			return new ConstructorFinder(fixtureClass).findConstructor();
		} catch (IllegalArgumentException e) {
			throw new InitializationError(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Description getDescription() {
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run(RunNotifier notifier) {
		evaluate(description, notifier);
	}
	
	private void evaluate(Description desc, RunNotifier notifier) {
		notifier.fireTestStarted(desc);
		for (Description child : desc.getChildren()) {
			evaluate(child, notifier);
		}
		if (stmtMap.containsKey(desc)) {
			Statement stmt = stmtMap.get(desc);
			try {
				stmt.evaluate();
				notifier.fireTestFinished(desc);
			} catch (Throwable e) {
				Failure failure = new Failure(desc, e);
				notifier.fireTestFailure(failure);
			}
		} else {
			notifier.fireTestFinished(desc);
		}
	}
	
	/**
	 * This annotation is used for identifying the JavaBean under test.
	 * <p>
	 * The bean identified by this annotation must have a no-argument constructor
	 * and have at least one mutable property that follows the naming pattern
	 * of the JavaBeans&trade; specification.
	 * 
	 * @author David Grant
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface Bean {
		Class<?> value();
	}
	
	/**
	 * This annotation is used for providing property test parameters.
	 * <p>
	 * The {@link JavaBeanRunner} will inspect the fields and methods of the test 
	 * class for this annotation (in that order), and will ignore all duplicates.  If
	 * a property annotation is declared for a non-existing property, it will also
	 * be ignored.
	 * <p>
	 * The field type or method return type must be valid types for the bean
	 * property, or the property test will fail.
	 * 
	 * @author David Grant
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.METHOD})
	public @interface Property {
		/**
		 * The name of a property as defined by the JavaBean specification.
		 */
		String value();
	}
}
