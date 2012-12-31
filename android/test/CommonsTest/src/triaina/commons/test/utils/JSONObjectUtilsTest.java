package triaina.commons.test.utils;

import triaina.commons.exception.JSONRuntimeException;
import triaina.commons.utils.JSONObjectUtils;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import junit.framework.TestCase;

public class JSONObjectUtilsTest extends TestCase {

	public void testParse() throws Exception {
		JSONObject json = JSONObjectUtils.parse("{'test':'test'}");
		assertEquals("test", json.get("test"));
	}
	
	public void testParseOnException() throws Exception {
		try {
			JSONObjectUtils.parse("a");
			fail();
		} catch (JSONRuntimeException exp) {
		}
	}

	public void testPut() {
		JSONObject json = new JSONObject();
		JSONObjectUtils.put(json, "test", "test");
	}

	public void testPutObject() {
		JSONObject data = new JSONObject();
		JSONObject json = new JSONObject();
		JSONObjectUtils.put(data, "test", "test");
		JSONObjectUtils.put(json, "data", data);
	}
	
	public void testGetString() throws Exception{
		JSONObject json = new JSONObject("{'test':'test'}");
		assertEquals("test", JSONObjectUtils.getString(json, "test"));
	}
	
	public void testGetInt() throws Exception {
		JSONObject json = new JSONObject("{'test':1}");
		assertEquals(1, JSONObjectUtils.getInt(json, "test"));
		
		json = new JSONObject("{'test':'1'}");
		assertEquals(1, JSONObjectUtils.getInt(json, "test"));
	}
	
	public void testGetIntOnException() throws Exception {
		try {
			JSONObject json = new JSONObject("{'test':'test'}");
			assertEquals(1, JSONObjectUtils.getInt(json, "test"));
			fail();
		} catch (JSONRuntimeException exp) {
		}
	}

	public void testGetObject() throws Exception {
		JSONObject json = JSONObjectUtils.parse("{'data':{test:'test'}}");
		assertEquals("test", JSONObjectUtils.getJSONObject(json, "data").get("test"));
	}
	
	public void testToUriQueryParameter() throws Exception {
		JSONObject json = new JSONObject();
		json.put("ccc", "CCC");
		json.put("bbb", "BBB");
		json.put("aaa", "AA A");
		
		String params = JSONObjectUtils.toUriQueryParameter(json);
		assertEquals("?bbb=BBB&ccc=CCC&aaa=AA%20A", params);
	}
	
	public void testGetMap() throws Exception {
		JSONObject json = new JSONObject("{'data':{'aaa':'AAA','bbb':'BBB'}}");
		Map<String, String> map = JSONObjectUtils.getMap(json, "data");
		assertEquals("AAA", map.get("aaa"));
		assertEquals("BBB", map.get("bbb"));
	}
	
	public void testGetBundle() throws Exception {
		JSONObject json = new JSONObject("{'data':{'aaa':'AAA','bbb':'BBB'}}");
		Bundle bundle = JSONObjectUtils.getBundle(json, "data");
		assertEquals("AAA", bundle.getString("aaa"));
		assertEquals("BBB", bundle.getString("bbb"));
	}
	
	public void testJsonObjectEquals () throws JSONException {
		final String jsonData01 =
				"{'key 01':'value of key 01','key two'={'inner key 1'=>NULL,'innerkey2':val-key-2,'key 03':{};key-4:[arrayval,[]]};'key 02':41}";
		final String jsonData01SyntacticChanges =
				"{'key 01'  : 'value of key 01';'key two':{'inner key 1'=NULL,'innerkey2'=val-key-2,'key 03'=>{    };  key-4 : [\"arrayval\";[]]};'key 02':41}";
		final String jsonData01NullAbsentChanges =
				"{'key 01':'value of key 01','key two'={'innerkey2':val-key-2,'key 03':{};key-4:[arrayval,[]]};'key 02':41}";
		final String jsonData01NonNullAbsentChanges =
				"{'key 01':'value of key 01','key two'={'inner key 1'=>NULL,'key 03':{};key-4:[arrayval,[]]};'key 02':41}";
		final String jsonData01ArrayChange =
				"{'key 01':'value of key 01','key two'={'inner key 1'=>NULL,'innerkey2':val-key-2,'key 03':{};key-4:[arrayvalC,[]]};'key 02':41}";

		final String jsonData02 =
				"{key:NULL}";
		final String jsonData02EmptyObjChange =
				"{key:{}}";
		final String jsonData02EmptyArrayChange =
				"{key:[]}";
		final String jsonData02ValueChange01 =
				"{key:'val'}";
		final String jsonData02ValueChange02 =
				"{key:'val,'}";
		final String jsonData02TypeChange =
				"{key:'NULL'}";
		
		final String jsonData03 =
				"{num:'41'}";
		final String jsonData03TypeChange =
				"{num:41}";
		final String jsonData03ValChange =
				"{num:'42'}";
		final String jsonData03FloatChange =
				"{num:41.000}";
		final String jsonData03FloatQuotedChange =
				"{num:'41.000'}";
		final String jsonData03KeyChange =
				"{'num ':'41'}";
		
		final String jsonData04 =
				"{'array':[val1,[inval1, null, 11],val3,[]]}";
		final String jsonData04AbsentEmptyArray =
				"{'array':[val1,[inval1, null, 11],val3]}";
		final String jsonData04AbsentNullValue =
				"{'array':[val1,[inval1, 11],val3,[]]}";
		final String jsonData04EmptyInnerArray =
				"{'array':[val1,[],val3,[]]}";
		final String jsonData04ValueChange =
				"{'array':[val1,[inval1, null, 11],val32,[]]}";
		final String jsonData04NumberChange =
				"{'array':[val1,[inval1, null, 11.0000],val3,[]]}";
		final String jsonData04TypeChange =
				"{'array':[val1,[inval1, null, '11'],val3,[]]}";
		
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01), new JSONObject(jsonData01SyntacticChanges)));
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01), new JSONObject(jsonData01NullAbsentChanges)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01), new JSONObject(jsonData01NonNullAbsentChanges)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01), new JSONObject(jsonData01ArrayChange)));

		//reflexivity
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01), new JSONObject(jsonData01)));
		//symmetry
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01SyntacticChanges), new JSONObject(jsonData01NullAbsentChanges)));
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01NullAbsentChanges), new JSONObject(jsonData01SyntacticChanges)));
		//transitivity
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01SyntacticChanges), new JSONObject(jsonData01)));
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01), new JSONObject(jsonData01NullAbsentChanges)));
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01SyntacticChanges), new JSONObject(jsonData01NullAbsentChanges)));
		//null
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData01SyntacticChanges), null));
		assertFalse(JSONObjectUtils.areDataEquivalent(null, new JSONObject(jsonData01SyntacticChanges)));
		assertTrue(JSONObjectUtils.areDataEquivalent(null, null));
		
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData02), new JSONObject(jsonData02EmptyObjChange)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData02), new JSONObject(jsonData02EmptyArrayChange)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData02EmptyObjChange), new JSONObject(jsonData02EmptyArrayChange)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData02), new JSONObject(jsonData02ValueChange01)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData02), new JSONObject(jsonData02ValueChange02)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData02ValueChange01), new JSONObject(jsonData02ValueChange02)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData02), new JSONObject(jsonData02TypeChange)));

		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData03), new JSONObject(jsonData03TypeChange)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData03), new JSONObject(jsonData03ValChange)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData03TypeChange), new JSONObject(jsonData03ValChange)));
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData03TypeChange), new JSONObject(jsonData03FloatChange)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData03), new JSONObject(jsonData03FloatQuotedChange)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData03), new JSONObject(jsonData03KeyChange)));

		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData04), new JSONObject(jsonData04)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData04), new JSONObject(jsonData04AbsentEmptyArray)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData04), new JSONObject(jsonData04AbsentNullValue)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData04), new JSONObject(jsonData04EmptyInnerArray)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData04), new JSONObject(jsonData04ValueChange)));
		assertTrue(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData04), new JSONObject(jsonData04NumberChange)));
		assertFalse(JSONObjectUtils.areDataEquivalent(new JSONObject(jsonData04), new JSONObject(jsonData04TypeChange)));
	}
}
