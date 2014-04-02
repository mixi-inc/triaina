package triaina.commons.test.json;

import java.util.Arrays;

import triaina.commons.exception.JSONConvertException;
import triaina.commons.json.JSONConverter;
import triaina.commons.json.annotation.Exclude;
import triaina.commons.utils.JSONObjectUtils;
import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class JSONConverterTest extends TestCase {
	
	private static final String VOID_JSON_DATA = "{ }";

	//this is on purpose, we use them for tests :
	private String notInitializedString;
	@SuppressWarnings("unused")
	private int    notInitializedInt;
	@SuppressWarnings("unused")
	private float  notInitializedFloat;
	
	public void testToObjectTest1() throws Exception {
		Test1 t1 = JSONConverter.toObject("{'aaa_aaa': 'aaa', 'bbb_bbb': 1.1, 'ccc_ccc' : 1, 'ddd_ddd' : true, 'exclusion' : 'test'}",
												Test1.class);
		assertEquals("aaa", t1.mAaaAaa);
		assertEquals(1.1, t1.mBbbBbb);
		assertEquals(1, t1.mCccCcc.intValue());
		assertEquals(true, t1.mDddDdd.booleanValue());
		assertNull(t1.mExclusion);
		
		t1 = JSONConverter.toObject("{'none': 'none'}", Test1.class);
		assertNull(t1.mAaaAaa);
		assertNull(t1.mBbbBbb);
		assertNull(t1.mCccCcc);
		assertNull(t1.mDddDdd);
	}
	
	public void testToObjectTest1onError() throws Exception {
		try {
			JSONConverter.toObject("{error}", Test1.class);
			fail();
		} catch (JSONConvertException exp) {
		}
	}
	
	//has child
	public void testToObjectTest2() throws Exception {
		Test2 t2 = JSONConverter.toObject("{'aaa_aaa': 'aaa', 'child' : {'bbb_bbb' : 'bbb'}}",
												Test2.class);
		assertEquals("aaa", t2.mAaaAaa);
		assertEquals("bbb", t2.mChild.mBbbBbb);
	}
	
	public void testToObjectTest2ChildEmpty() throws Exception {
		final Test2 t2 = JSONConverter.toObject("{'aaa_aaa': 'aaa'}",
												Test2.class);
		assertEquals("aaa", t2.mAaaAaa);
		assertNull(t2.mChild);
	}
	
	/**
	 * Tests behavior when inflating JSON data with keys which don't match members in the destination object.
	 * Expected behavior : only superfluous data will be ignored and no exception is thrown by the inflation process.
	 */
	public void testToObjectTest2SuperfluousJsonData() throws Exception {
		final Test2 t2 = JSONConverter.toObject("{'aaa_aaa': 'aaa', 'aaa_aab':'ignored', 'child' : {'xxx':'ignored', 'bbb_bbb' : 'bbb'}}", Test2.class);
		assertEquals("aaa", t2.mAaaAaa);
		assertNotNull(t2.mChild);
		assertEquals("bbb", t2.mChild.mBbbBbb);
	}

	//has array
	public void testToObjectTest3() throws Exception {
		Test3 t3 = JSONConverter.toObject("{'array':[{'bbb_bbb': 'bbb'}, {'bbb_bbb': 'BBB'}]}", Test3.class);
		assertEquals(2, t3.mArray.length);
		assertEquals("bbb", t3.mArray[0].mBbbBbb);
		assertEquals("BBB", t3.mArray[1].mBbbBbb);
	}
	
	//has array
	public void testToObjectTest3EmptyArray() throws Exception {
		Test3 t3 = JSONConverter.toObject("{'array':[]}", Test3.class);
		assertEquals(0, t3.mArray.length);
	}
	
	//has Bundle
	public void testToObjectTest4() throws Exception {
		Test4 t4 = JSONConverter.toObject("{'bundle':{'aaa':'AAA','bbb':'BBB', 'ccc': 1}}", Test4.class);
		assertEquals("AAA", t4.mBundle.getString("aaa"));
		assertEquals("BBB", t4.mBundle.getString("bbb"));
		assertEquals("1", t4.mBundle.getString("ccc"));
	}
	
	//has Bundle
	public void testToObjectTest4ArrayIncluded() throws Exception {
		Test4 t4 = JSONConverter.toObject("{'bundle':{'aaa':['a', 'b', 'c']}}", Test4.class);
		String[] arr = t4.mBundle.getStringArray("aaa");
		assertEquals("a", arr[0]);
		assertEquals("b", arr[1]);
		assertEquals("c", arr[2]);
		assertEquals(3, arr.length);
	}
	
	public void testConversionNull () throws JSONConvertException, JSONException {
		//-- test with a String member variable - not specified in the JSON data
		final Child nullChildFromVoidJson = JSONConverter.toObject(VOID_JSON_DATA, Child.class);
		assertNull(nullChildFromVoidJson.mBbbBbb);
		
		//-- test with a String member variable - explicitly specified as NULL in the JSON data
		final Child nullChildFromNullJson = JSONConverter.toObject("{'bbb_bbb':NULL}", Child.class);
		assertNull(nullChildFromNullJson.mBbbBbb);
		
		//-- test with an array - not specified in the JSON data
		final Test3 nullArrayObjectFromVoidJson = JSONConverter.toObject(VOID_JSON_DATA, Test3.class);
		assertNull(nullArrayObjectFromVoidJson.mArray);

		//-- test with an array - explicitly specified as NULL in the JSON data
		final Test3 nullArrayObjectFromNullJson = JSONConverter.toObject("{'array':NULL}", Test3.class);
		assertNull(nullArrayObjectFromNullJson.mArray);

		//-- test JSON <--> Java object
		final String nullTest = "{child:NULL}";
		final Test2 nullTestToObjectConversionResult = JSONConverter.toObject(nullTest, Test2.class);
		// to object
		assertNull(nullTestToObjectConversionResult.mAaaAaa);
		assertNull(nullTestToObjectConversionResult.mChild);
		// to JSON
		final JSONObject nullTestDataObject = new JSONObject(nullTest);
		final JSONObject nullTestToJsonConversionResult = JSONConverter.toJSON(nullTestToObjectConversionResult);
		assertTrue(JSONObjectUtils.areDataEquivalent(nullTestDataObject, nullTestToJsonConversionResult));
	}

	//----- Inner classes
	
	//both classes must be public and the outer one static to be instantiable from the JSON conversion library
	public static class EncapsulatingClass {
		public class InnerClass {
			private String mTextMember;
		}
		private String     mTextMember;
		private InnerClass mInnerClass;
	}
	
	public void testConversionObjectInnerClass () throws JSONConvertException, JSONException {
		final String encapsulatingClassTextMember = "encapsulating class text member";
		final String innerClassTextMember         = "inner class text member";
		final String jsonData = "{" +
						"'inner_class':{'text_member':'"+innerClassTextMember+"'}" + ", " +
						"'text_member':'"+encapsulatingClassTextMember+"'" +
						"}";
		
		// -- JSON -> Java object
		
		final EncapsulatingClass toObjectConvertionResult = JSONConverter.toObject(jsonData, EncapsulatingClass.class);
		assertNotNull(toObjectConvertionResult);
		assertNotNull(toObjectConvertionResult.mTextMember);
		assertEquals(EncapsulatingClass.class, toObjectConvertionResult.getClass());
		assertEquals(EncapsulatingClass.InnerClass.class, toObjectConvertionResult.mInnerClass.getClass());
		assertNotNull(toObjectConvertionResult.mTextMember);
		assertNotNull(toObjectConvertionResult.mInnerClass.mTextMember);
		assertEquals(toObjectConvertionResult.mTextMember, encapsulatingClassTextMember);
		assertEquals(toObjectConvertionResult.mInnerClass.mTextMember, innerClassTextMember);
		
		//also test for null object
		final EncapsulatingClass voidConvertionResult = JSONConverter.toObject(VOID_JSON_DATA, EncapsulatingClass.class);
		assertNull(voidConvertionResult.mInnerClass);
		
		// -- Java object -> JSON
		
		final JSONObject jsonDataObject = new JSONObject(jsonData);
		final JSONObject toJsonConversionResult = JSONConverter.toJSON(toObjectConvertionResult);
		assertTrue(JSONObjectUtils.areDataEquivalent(jsonDataObject, toJsonConversionResult));  //JSONObject#equals isn't implemented
	}
	
	public void testConversionObjectArray () throws JSONConvertException, JSONException {
		final String arrayJsonDataValue01 = "value 01";
		final String arrayJsonDataValue02 = "value 02";
		final String arrayJsonDataValue03 = "value 03";
		final String arrayJsonData =
				"[" +
						"{bbb_bbb:"+"'"+arrayJsonDataValue01+"'"+"}"+"," +  //[0]
						"null"+"," +                                        //[1]
						"{bbb_bbb:"+"'"+arrayJsonDataValue02+"'"+"}"+"," +  //[2]
						"{}"+"," +                                          //[3]
						"{bbb_bbb:null}"+"," +                              //[4]
						"{bbb_bbb:"+"'"+arrayJsonDataValue03+"'"+"}"+"," +  //[5]
						//null (cf. previous comma)                         //[6]
						"]";
		final JSONArray arrayJsonDataArray = new JSONArray(arrayJsonData);
		final int arrayJsonDataLength = arrayJsonDataArray.length();
		
		final Child[] toObjectArrayConversionResult = JSONConverter.toObjectArray(arrayJsonDataArray, Child.class);
		assertEquals(arrayJsonDataLength, toObjectArrayConversionResult.length);
		assertNotNull(toObjectArrayConversionResult[0]);
		assertEquals(arrayJsonDataValue01, toObjectArrayConversionResult[0].mBbbBbb);
		assertNull(toObjectArrayConversionResult[1]);
		assertNotNull(toObjectArrayConversionResult[2]);
		assertEquals(arrayJsonDataValue02, toObjectArrayConversionResult[2].mBbbBbb);
		assertNotNull(toObjectArrayConversionResult[3]);
		assertEquals(notInitializedString, toObjectArrayConversionResult[3].mBbbBbb);
		assertNotNull(toObjectArrayConversionResult[4]);
		assertNull(toObjectArrayConversionResult[4].mBbbBbb);
		assertNotNull(toObjectArrayConversionResult[5]);
		assertEquals(arrayJsonDataValue03, toObjectArrayConversionResult[5].mBbbBbb);
		assertNull(toObjectArrayConversionResult[6]);
		
		//TODO array of number (and quoted ones) and array of arrays
		//TODO to json array
	}
	
	public void testConversionNumberArray () throws JSONException, JSONConvertException {
		final int arrayJsonDataValue01 = 13;
		final float arrayJsonDataValue02 = 02.456f;
		final double arrayJsonDataValue03 = -432.43f;
		final String arrayJsonData =
				"[" +
						arrayJsonDataValue01+"," +          //[0]
						"null"+"," +                        //[1]
						arrayJsonDataValue02+"," +          //[2]
						arrayJsonDataValue03+"," +          //[3]
						"'"+arrayJsonDataValue03+"'"+"," +  //[4]
						//null (cf. previous comma)         //[5]
						"]";
		final JSONArray arrayJsonDataArray = new JSONArray(arrayJsonData);
		final int arrayJsonDataLength = arrayJsonDataArray.length();

		// JSON -> Java object
		final Float[] toFloatArrayConversionResult = JSONConverter.toObjectArray(arrayJsonDataArray, Float.class);
		assertEquals(arrayJsonDataLength, toFloatArrayConversionResult.length);
		assertNotNull(toFloatArrayConversionResult[0]);
		assertEquals(arrayJsonDataValue01, (int)(toFloatArrayConversionResult[0].intValue()));
		assertNull(toFloatArrayConversionResult[1]);
		assertNotNull(toFloatArrayConversionResult[2]);
		assertEquals(arrayJsonDataValue02, toFloatArrayConversionResult[2], 0);
		assertNotNull(toFloatArrayConversionResult[3]);
		assertEquals((float)arrayJsonDataValue03, toFloatArrayConversionResult[3]);
		assertNotNull(toFloatArrayConversionResult[4]);
		assertEquals((float)arrayJsonDataValue03, toFloatArrayConversionResult[4]);
		assertNull(toFloatArrayConversionResult[5]);
		
		// Java object -> JSON
		final JSONArray toJsonArrayConversionResult = JSONConverter.toJSONArray(toFloatArrayConversionResult);
		assertNotNull(toJsonArrayConversionResult);
		assertEquals(arrayJsonDataArray.getInt(0), toJsonArrayConversionResult.getInt(0));
		assertTrue(toJsonArrayConversionResult.isNull(1));
		assertEquals((float)(arrayJsonDataArray.getDouble(2)), (float)(toJsonArrayConversionResult.getDouble(2)));
		assertEquals(arrayJsonDataArray.getDouble(3), toJsonArrayConversionResult.getDouble(3));
		assertEquals(arrayJsonDataArray.getDouble(4), toJsonArrayConversionResult.getDouble(4));  //automatically coerced from String
		assertTrue(toJsonArrayConversionResult.isNull(5));
		
		//  Java object <-> JSON
		final Float[] toFloatArrayFromConvertedArrayConversionResult = JSONConverter.toObjectArray(toJsonArrayConversionResult, Float.class);
		assertTrue(Arrays.equals(toFloatArrayConversionResult, toFloatArrayFromConvertedArrayConversionResult));
	}
	
	public void testToJSONTest1() throws Exception {
		Test1 t1 = new Test1();
		t1.mAaaAaa = "aaa";
		t1.mBbbBbb = 1.1;
		t1.mCccCcc = 3;
		t1.mDddDdd = true;
		t1.mExclusion = "test";
		
		JSONObject json = JSONConverter.toJSON(t1);
		assertEquals("aaa", json.get("aaa_aaa"));
		assertEquals(1.1, json.get("bbb_bbb"));
		assertEquals(3, json.get("ccc_ccc"));
		assertEquals(true, json.get("ddd_ddd"));
		assertNull(json.opt("exclusion"));
		
		t1 = new Test1();
		json = JSONConverter.toJSON(t1);
		assertNull(json.opt("aaa_aaa"));
		assertNull(json.opt("bbb_bbb"));
		assertNull(json.opt("ccc_ccc"));
		assertNull(json.opt("ddd_ddd"));
	}
	
	public void testToJSONTest2() throws Exception {
		Test2 t2 = new Test2();
		t2.mAaaAaa = "aaa";
		t2.mChild = new Child();
		t2.mChild.mBbbBbb = "bbb";
		
		JSONObject json = JSONConverter.toJSON(t2);
		assertEquals("aaa", json.get("aaa_aaa"));
		JSONObject c = json.getJSONObject("child");
		assertEquals("bbb", c.get("bbb_bbb"));
	}
	
	public void testToJSONTest3() throws Exception {
		Test3 t3 = new Test3();
		t3.mArray = new Child[3];
		
		for (int i = 0; i < t3.mArray.length; i++) {
			t3.mArray[i] = new Child();
			t3.mArray[i].mBbbBbb = "bbb" + i;
		}
		
		JSONObject json = JSONConverter.toJSON(t3);
		JSONArray arr = json.getJSONArray("array");
		assertEquals(3, arr.length());
		
		for (int i = 0; i < arr.length(); i++) {
			JSONObject child = arr.getJSONObject(i);
			assertEquals("bbb" + i, child.getString("bbb_bbb"));
		}
	}
	
	public void testToJSONTest4() throws Exception {
		Test4 t4 = new Test4();
		Bundle b1 = new Bundle();
		Bundle b2 = new Bundle();
		
		b1.putBundle("child1", b2);
		b2.putBoolean("true", true);
		b1.putString("aaa", "aaa");
		b1.putString("bbb", "bbb");
		b1.putStringArray("arr", new String[]{"a", "b"});
		t4.mBundle = b1;
		
		JSONObject json = JSONConverter.toJSON(t4).getJSONObject("bundle");
		assertEquals("aaa", json.getString("aaa"));
		assertEquals("bbb", json.getString("bbb"));
		
		JSONArray arr = json.getJSONArray("arr");
		assertEquals(2, arr.length());
		assertEquals("a", arr.get(0));
		assertEquals("b", arr.get(1));
		
		JSONObject child = json.getJSONObject("child1");
		assertEquals(true, child.getBoolean("true"));		
	}
	
	public static class Test1 {
		private String mAaaAaa;
		private Double mBbbBbb;
		private Integer mCccCcc;
		private Boolean mDddDdd;
		
		@Exclude
		private String mExclusion;
	}
	
	public static class Test2 {
		private String mAaaAaa;
		private Child mChild;
	}

	public static class Test3 {
		private Child[] mArray;
	}
	
	public static class Child {
		private String mBbbBbb;
	}
	
	public static class Test4 {
		private Bundle mBundle;
	}
}
