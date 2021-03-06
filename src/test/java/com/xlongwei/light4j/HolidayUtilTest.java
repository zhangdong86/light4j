package com.xlongwei.light4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.TreeMap;

import org.junit.Test;

import com.xlongwei.light4j.util.DateUtil;
import com.xlongwei.light4j.util.HolidayUtil;

/**
 * holiday util test
 * @author xlongwei
 *
 */
public class HolidayUtilTest {
	
	@Test public void test() throws Exception {
		new TreeMap<>(HolidayUtil.holidays).forEach((k,v) -> System.out.println(k+"="+v));
		new TreeMap<>(HolidayUtil.plans).forEach((k,v) -> System.out.println(k+"="+v));
		assertEquals("元旦节", HolidayUtil.nameOf(1));
		assertEquals("春节", HolidayUtil.nameOf(2));
		assertEquals("中秋节", HolidayUtil.nameOf(7));
	}

	@Test public void isworkday() throws Exception {
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-01-01")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2020-01-02")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2020-01-19")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-01-24")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-01-25")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-01-26")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-01-27")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-01-28")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-01-29")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-01-30")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-01-31")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-02-01")));
		assertFalse(HolidayUtil.isworkday(DateUtil.parse("2020-02-02")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2020-02-03")));
	}
	
	@Test public void nextworkday() throws Exception {
		assertEquals("2020-01-02", DateUtil.dateFormat.format(HolidayUtil.nextworkday(DateUtil.parse("2020-01-01"))));
		assertEquals("2020-01-20", DateUtil.dateFormat.format(HolidayUtil.nextworkday(DateUtil.parse("2020-01-18"), true)));
		assertEquals("2020-01-23", DateUtil.dateFormat.format(HolidayUtil.nextworkday(DateUtil.parse("2020-01-23"))));
		assertEquals("2020-02-03", DateUtil.dateFormat.format(HolidayUtil.nextworkday(DateUtil.parse("2020-01-24"))));
		assertEquals("2020-02-03", DateUtil.dateFormat.format(HolidayUtil.nextworkday(DateUtil.parse("2020-01-30"))));
		assertEquals("2020-02-03", DateUtil.dateFormat.format(HolidayUtil.nextworkday(DateUtil.parse("2020-02-01"))));
		assertEquals("2020-02-03", DateUtil.dateFormat.format(HolidayUtil.nextworkday(DateUtil.parse("2020-02-02"))));
		assertEquals("2020-02-03", DateUtil.dateFormat.format(HolidayUtil.nextworkday(DateUtil.parse("2020-02-01"), true)));
	}
	
	@Test public void offsetworkday() throws Exception {
		assertEquals("2020-01-02", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-01"), 1)));
		assertEquals("2020-01-19", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-18"), 1)));
		assertEquals("2020-01-20", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-17"), 1, true)));
		assertEquals("2020-01-20", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-18"), 1, true)));
		assertEquals("2020-02-03", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-23"), 1)));
		assertEquals("2020-02-03", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-30"), 1)));
		assertEquals("2020-02-03", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-31"), 1)));
		assertEquals("2020-02-04", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-30"), 2)));
		assertEquals("2020-01-23", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-02-03"), -1)));
		assertEquals("2020-01-23", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-02-08"), -6)));
		assertEquals("2020-02-07", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-02-09"), -1)));
		assertEquals("2020-01-23", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-02-09"), -6)));
		assertEquals("2020-01-19", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-31"), -5)));
		assertEquals("2020-02-03", DateUtil.dateFormat.format(HolidayUtil.offsetworkday(DateUtil.parse("2020-01-31"), 1, true)));
	}
	
	@Test public void betweenworkday() {
		assertEquals(25, HolidayUtil.betweenworkday(DateUtil.parse("2020-01-01"), DateUtil.parse("2020-02-12"), false));
		assertEquals(24, HolidayUtil.betweenworkday(DateUtil.parse("2020-01-01"), DateUtil.parse("2020-02-12"), true));
	}
	
	@Test public void test2017To2011() {
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2011-10-8")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2011-12-31")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2012-4-1")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2013-4-27")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2014-5-4")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2015-2-15")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2016-9-18")));
		assertTrue(HolidayUtil.isworkday(DateUtil.parse("2017-4-1")));
	}
}
