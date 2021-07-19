package com.peixueshi.crm.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.text.TextUtils;
import android.util.Log;

import com.peixueshi.crm.bean.ChanceUserInfo;
import com.peixueshi.crm.bean.DetailsBean;


/**
 *
 * Created by zhaobaolei on 2017/5/16 0016.
 */
public class JSONUtil {

	private static final String TAG = "JSONUtil";

	/**
	 * 解析JSON
	 * 
	 * @param object
	 *            JSON字符串
	 * @param clazz
	 *            要封装的对象
	 * @return
	 */
	public static <T> T parser(JSONObject object, Class clazz,int reqId) {
		@SuppressWarnings("unchecked")
		T t = null;
		try {
			t = (T) clazz.newInstance();
			Field[] fields = t.getClass().getFields();
			for (Field item : fields) {
				item.setAccessible(true);
				try {
					String type = item.getType().getName();
					String name = item.getName();
					String value = object.get(name).toString();
					if (type.equals("java.lang.String")) {
						if (TextUtils.isEmpty(value)) {
							value = null;
						}
						item.set(t, value);
					} else if ("int".equals(type)) {
						if (value.length() > 0)
							item.set(t, Integer.parseInt(value));
					} else if ("boolean".equals(type)) {
						if (value.length() > 0)
							item.set(t, Boolean.parseBoolean(value));
					} else if ("float".equals(type)) {
						if (value.length() > 0)
							item.set(t, Float.parseFloat(value));
					} else if ("double".equals(type)) {
						if (value.length() > 0)
							item.set(t, Double.parseDouble(value));
					}else if("java.util.List".equals(type)){
						Log.e("tag", "parser: ss" );
                        if (name.equals("details")&&reqId==1){//comm/orderBList
							List<DetailsBean> list = JSONUtil.getList(object, value, name, reqId);
							item.set(t, list);
						}
                        if (name.equals("workPool")&&reqId==2){//cmc/work_pool
							List<ChanceUserInfo> list = JSONUtil.getChance(object, value, name, reqId);
							item.set(t, list);
						}
						if (name.equals("workProTg")&&reqId==2){//cmc/work_pool
							List<ChanceUserInfo> list = JSONUtil.getChance(object, value, name, reqId);
							item.set(t, list);
						}
					}else if("java.util.Map".equals(type)){
                        Map<String,String> map = JSONUtil.getMap(object,name);
                        item.set(t, map);
					}
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return t;
		}
		return t;
	}






	/**
	 * 封装对象集合
	 * 
	 * @param jsonArray
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parserArrayList(JSONArray jsonArray, Class<T> clazz,int reqId) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				list.add((T) parser(jsonArray.getJSONObject(i), clazz,reqId));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static <T> List<Integer> parserArrayList(JSONArray jsonArray) {
		List<Integer> lists = new ArrayList<Integer>();
		String str;
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				lists.add(jsonArray.getInt(i));
			} catch (Exception e) {
				lists.add(0);
			}
		}
		return lists;
	}

	public static <T> List<String> parserStringArrayList(JSONArray jsonArray) {
		List<String> lists = new ArrayList<String>();
		String str;
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				lists.add(jsonArray.getString(i));
			} catch (Exception e) {

			}
		}
		return lists;
	}


	public  static List<DetailsBean> getList(JSONObject object, String str,String name,int reqId) {
		try {
			JSONArray jsonObject = object.getJSONArray(name);

			List<DetailsBean> detailsBeans = parserArrayList(jsonObject, DetailsBean.class, reqId);
			return detailsBeans;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}


    public  static List<ChanceUserInfo> getChance(JSONObject object, String str,String name,int reqId) {
        try {
            JSONArray jsonObject = object.getJSONArray(name);

            List<ChanceUserInfo> detailsBeans = parserArrayList(jsonObject, ChanceUserInfo.class, reqId);
            return detailsBeans;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static Map<String, String> getMap(JSONObject object, String str) {
		try {
			JSONObject jsonObject = object.getJSONObject(str);
			return getMap(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

    public static Map<String, String> getStringMap( String name,String str) {
        JSONObject jsonObject=new JSONObject();

        try {
			jsonObject.put(name,str);
            return getMap(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static Map<String, String> getMap(JSONObject object) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Iterator iterator = object.keys();
			String key = null;
			String obj = null;
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				obj = object.getString(key);
				map.put(key, obj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}




	/**
	 * 把JSONarray封装在map集合中
	 * 
	 * @param jsonArray
	 * @param str
	 * @return
	 */
	public static Map<String, List<Map<String, String>>> getMapObject(
			JSONArray jsonArray, String str) {
		Map<String, List<Map<String, String>>> mapObj = null;
		try {
			mapObj = new HashMap<String, List<Map<String, String>>>();
			List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				Map<String, String> map = getMap(object);
				mapList.add(map);
			}
			mapObj.put(str, mapList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mapObj;
	}

	/**
	 * 把JSONarray封装在map集合中
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static List<Map<String, String>> getListMap(JSONArray jsonArray) {
		List<Map<String, String>> mapList = null;
		try {
			mapList = new ArrayList<Map<String, String>>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				Map<String, String> map = getMap(object);
				mapList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mapList;
	}

	/**
	 * 返回由对象的属性为key,值为map的value的Map集合
	 * 
	 * @param obj
	 *            Object
	 * @return mapValue Map<String,String>
	 * @throws Exception
	 */
	public static Map<String, String> getFieldVlaue(Object obj)
			throws Exception {
		Map<String, String> mapValue = new HashMap<String, String>();
		Class<?> cls = obj.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			Object object = field.get(obj);
			String value = object != null ? object.toString() : "";
			mapValue.put(name, value);
		}
		return mapValue;
	}
}
