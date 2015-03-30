## The Problem ##

Writing tests for JavaBeans™ is...

  * repetitive,
  * boring,
  * time-consuming, and
  * error-prone.

However, we often build larger systems on top of JavaBeans™, so we need to be sure they're behaving correctly.  What's more, if our project uses a code-coverage threshold, our build will fail if we don't test them.

## The Missing Point ##

JavaBeans™ are supposed to be machine-processable!

## The Solution ##

**JavaBeanRunner automatically tests your JavaBean properties using the power of !JUnit.**

Writing tests with JavaBeanRunner is...

  * fast,
  * efficient, and
  * reliable.

Writing tests for a bean can be done in only a few lines of code, using only a couple of simple annotations.  Moreover, JavaBeanRunner will automatically fail if you add new, untested, properties to your bean.

## Example ##

To test this simple bean:

```
public class Bean {
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
```

You need only write the following test code:

```
@RunWith(JavaBeanRunner.class)
@Bean(Bean.class)
public BeanTest {
    @Property("name")
    public String name = "name";
}
```

### Resources ###

  * http://codebox.org.uk/pages/articles/unit-testing-javabeans
  * http://blog.coryfoy.com/2008/05/unit-testing-equals-and-hashcode-of-java-beans/
  * [Latest JUnit Javadoc](http://kentbeck.github.com/junit/javadoc/latest/)
  * [JavaBeans Tutorial](http://download.oracle.com/javase/tutorial/javabeans/index.html)
  * [java.beans](http://download.oracle.com/javase/6/docs/api/java/beans/package-summary.html)
  * [java.lang.Object](http://download.oracle.com/javase/6/docs/api/java/lang/Object.html)
  * [java.lang.Comparable](http://download.oracle.com/javase/6/docs/api/java/lang/Comparable.html)
  * [java.beans.ConstructorProperties](http://download.oracle.com/javase/6/docs/api/java/beans/ConstructorProperties.html)
  * http://outsidemybox.github.com/testUtils/
  * http://code.google.com/p/openpojo
  * http://javabeantester.sourceforge.net/
  * http://stackoverflow.com/questions/2218177/junit-test-method-for-getters-setters
  * http://stackoverflow.com/questions/108692/is-there-a-java-unit-test-framework-that-auto-tests-getters-and-setters
  * http://stackoverflow.com/questions/4026839/junit-for-getters-and-setters
  * http://www.unitils.org/summary.html