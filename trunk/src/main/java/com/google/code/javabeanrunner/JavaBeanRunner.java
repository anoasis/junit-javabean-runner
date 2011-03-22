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
 * The JavaBean under test is identified by the {@link Fixture Fixture} annotation.  The runner
 * will attempt to determine if the class getter and setter methods (a.k.a. accessors and mutators)
 * behave correctly by comparing the property value before and after the setter has been invoked.
 * <p>
 * In cases where properties are of primitive type (e.g. int, boolean), the runner will use a non-default
 * value ({@code MAX_VALUE} for byte, char, short, int, long, float and double; and {@code true} for 
 * boolean) to test correctness.  In cases where properties are objects, the runner will attempt to 
 * construct an object by invoking the no-arg constructor of the property class where appropriate.
 * <p>
 * This behaviour can be overridden by annotating fields and methods in the test class using the
 * {@link Property Property} annotation with a value of the property name.  If the class under test
 * uses an object which does not provide a no-arg constructor (e.g. {@link java.awt.Point}), failure
 * to provide a member annotated with {@link Property Property} will result in that property being
 * ignored (similar to using the JUnit {@link org.junit.Ignore Ignore} annotation).  Properties will 
 * also be ignored if the default primitive property value is identical to the  value used by the 
 * runner.  Note that this will not occur where no value is assigned in the field  declaration or 
 * bean constructor. 
 * <p>
 * The following snippet shows typical usage for the following bean:
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
 * 
 * &#64;RunWith(JavaBeanRunner.class)
 * &#64;Fixture(Bean.class)
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
	private final Map<String, MemberAdapter> memberMap;
	private final Set<PropertyDescriptor> properties;
	// FIXME: This is a naive concept
	private final Set<Description> ignoreList;
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
		memberMap = findMembers(testClass, properties);
		constructor = findConstructor(fixture);
		ignoreList = new HashSet<Description>();
		stmtMap = new HashMap<Description, Statement>();
		
		if (properties.isEmpty() || memberMap.isEmpty()) {
			description = Description.EMPTY;
		} else {
			description = Description.createSuiteDescription(testClass);
			for (PropertyDescriptor prop : properties) {
				Description childDesc = Description.createTestDescription(fixture, prop.getName());
				description.addChild(childDesc);
				
				if (memberMap.containsKey(prop.getName()) == false) {
					ignoreList.add(childDesc);
				} else {
					MemberAdapter member = memberMap.get(prop.getName());
					Object sourceValue = member.getValue(testClass.newInstance());
					Object target = constructor.newInstance();
					Statement stmt = new MutationStatement(sourceValue, target, prop.getReadMethod(), prop.getWriteMethod());
					stmtMap.put(childDesc, stmt);
				}
			}
		}
	}
	
	private Map<String, MemberAdapter> findMembers(Class<?> testClass, Set<PropertyDescriptor> props) throws InitializationError {
		MemberFinder finder = new MemberFinder(testClass);
		return finder.findMembers();
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
		Fixture fixture = testClass.getAnnotation(Fixture.class);
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
		if (ignoreList.contains(desc)) {
			notifier.fireTestIgnored(desc);
		} else if (stmtMap.containsKey(desc)) {
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
	 * The bean identified by this annotation is introspected for compliance with the
	 * JavaBeans&trade; specification.
	 * 
	 * @author David Grant
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface Fixture {
		Class<?> value();
	}
	
	/**
	 * This annotation is used for providing property test parameters.
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
