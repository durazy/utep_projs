import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

//TEST SUITE FOR ALL TESTS

@RunWith(org.junit.platform.runner.JUnitPlatform.class)
@SuiteDisplayName("JUnit Platform Suite Demo")
@SelectClasses({PurchaseTest.class, TestLoading.class, TestDataEntry.class})
public class TestUnit {
}

