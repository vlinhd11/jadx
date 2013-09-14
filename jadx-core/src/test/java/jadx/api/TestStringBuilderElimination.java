package jadx.api;

import jadx.core.dex.nodes.ClassNode;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class TestStringBuilderElimination extends InternalJadxTest {

	public static class MyException extends Exception {
		public MyException(String str, Exception e) {
//			super("msg:" + str, e);
		}

		public void method(int k) {
			System.out.println("k=" + k);
		}
	}

	@Test
	public void test() {
		ClassNode cls = getClassNode(MyException.class);
		String code = cls.getCode().toString();

		assertThat(code, not(containsString("new StringBuilder")));
		assertThat(code, containsString("System.out.println(\"k=\" + k);"));
	}
}