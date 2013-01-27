package org.jproggy.snippetory.test;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.jproggy.snippetory.Repo;
import org.jproggy.snippetory.Template;
import org.junit.Test;

public class NumberFormatTest {
	
	@Test
	public void formatNumber() {
		Template number = Repo.parse("{v:test number=\"0.00#\"}", Locale.GERMAN);
		number.set("test", "x");
		assertEquals("x", number.toString());
		number.set("test", "123456");
		assertEquals("123456", number.toString());
		number.set("test", 1.6);
		assertEquals("1,60", number.toString());
		number.set("test", 1.6333);
		number = Repo.parse("{v:test}", Locale.US);
		number.set("test", "x");
		assertEquals("x", number.toString());
		number.set("test", "123456");
		assertEquals("123456", number.toString());
		number.set("test", 1.6);
		assertEquals("1.6", number.toString());
		number.set("test", 1.6333);
		assertEquals("1.633", number.toString());
		number = Repo.parse("{v:test number='0.00#'}", Locale.GERMANY);
		number.set("test", 1.55);
		assertEquals("1,55", number.toString());
		number.set("test", 1.55555);
		assertEquals("1,556", number.toString());
		number = Repo.parse("{v:test number='000'}", Locale.GERMANY);
		number.set("test", 1.55);
		assertEquals("002", number.toString());
		number.set("test", 1000);
		assertEquals("1000", number.toString());
		number = Repo.parse("{v:test number='tostring'}", Locale.GERMANY);
		number.set("test", 1.55);
		assertEquals("1.55", number.toString());
		number.set("test", 10000);
		assertEquals("10000", number.toString());
		number = Repo.parse("{v:test}", Locale.GERMANY);
		number.set("test", 1.55);
		assertEquals("1,55", number.toString());
		number.set("test", 10000);
		assertEquals("10.000", number.toString());
		number = Repo.parse("{v:test}");
		number.set("test", 1.55);
		assertEquals("1.55", number.toString());
		number.set("test", 10000);
		assertEquals("10000", number.toString());
		number = Repo.parse("{v:test}", Locale.US);
		number.set("test", 1.55);
		assertEquals("1.55", number.toString());
		number.set("test", 10000);
		assertEquals("10,000", number.toString());
	}
	
	@Test
	public void formatNumberInheritance() {
		Template t = Repo.parse("before<t: number='000'>->{v:test}<-</t:>after", Locale.GERMAN);
		assertEquals("beforeafter", t.toString());
		t.set("test", 5.1);
		assertEquals("before->005<-after", t.toString());
		t = Repo.parse("before<t: number='000'>-><t:>{v:test}</t:><-</t:>after");
		assertEquals("beforeafter", t.toString());
		t.set("test", 5);
		assertEquals("before->005<-after", t.toString());
	}
}