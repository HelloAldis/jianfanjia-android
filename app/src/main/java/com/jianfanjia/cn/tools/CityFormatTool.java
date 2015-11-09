package com.jianfanjia.cn.tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.application.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-11-05 14:20
 */
public class CityFormatTool {

    private static final String TAG = "CityFormatTool";

    public static final String CITY_FILE = "city.txt";
    public static final String PROVICE_FILE = "provice.txt";
    public static final String DISTRICT_FILE = "district.txt";

    public static String loadJson(String fileName) {
        String jsonString = null;
        try {
            InputStream is = MyApplication.getInstance().getAssets()
                    .open(fileName);
            jsonString = StringUtils.toConvertString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static class Provice {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class City {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class District {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

   /* public static void loadCity(String[] provices,Map<String,String[] cities> cityMap,Map<String,String[] districts> districtMap){
        String jsondata = loadCityJson();
        List<Provice> proviceList = new ArrayList<>();
        List<City> cityList = new ArrayList<>();
        List<District> districtList = new ArrayList<>();
        Provice provice = null;
        City city = null;
        District district = null;
        try {
            JSONObject jsonObject = new JSONObject(jsondata);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String id = iterator.next();
                if (id.endsWith("0000")) {
                    provice = new Provice();
                    provice.setId(id);
                    JSONArray jsonArray = jsonObject.getJSONArray(id);
                    provice.setName((String) jsonArray.get(0));
                    LogTool.d(TAG, "id = " + id + " name =" + provice.getName());
                    proviceList.add(provice);
                }else if(id.endsWith("00") && !id.endsWith("0000")){
                    city = new City();
                    city.setId(id);
                    JSONArray jsonArray = jsonObject.getJSONArray(id);
                    city.setName((String) jsonArray.get(0));
                    LogTool.d(TAG, "id = " + id + " name =" + city.getName());
                    cityList.add(city);
                }else{
                    district = new District();
                    district.setId(id);
                    JSONArray jsonArray = jsonObject.getJSONArray(id);
                    district.setName((String) jsonArray.get(0));
                    LogTool.d(TAG, "id = " + id + " name =" + district.getName());
                    districtList.add(district);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    //拿到省数组
    public static List<String> getProviceList() {
        String proviceData = loadJson(PROVICE_FILE);
        Gson gson = new Gson();
        List<String> provices = gson.fromJson(proviceData, new TypeToken<ArrayList<String>>() {
        }.getType());
        return provices;
    }

    //拿到市
    public static Map<String, List<String>> getCityMap(){
        String cityData = loadJson(CITY_FILE);
        Gson gson = new Gson();
        Map<String,List<String>> citys = gson.fromJson(cityData,new TypeToken<Map<String,ArrayList<String>>>(){}.getType());
        return citys;
    }

    //拿到区
    public static Map<String, List<String>> getDistrictMap(){
        String districtData = loadJson(DISTRICT_FILE);
        Gson gson = new Gson();
        Map<String,List<String>> districts = gson.fromJson(districtData,new TypeToken<Map<String,ArrayList<String>>>(){}.getType());
        return districts;
    }

    public static List<City> getCityByProviceId(String jsondata, String proId) {
        List<City> cityList = new ArrayList<>();
        City city = null;
        try {
            JSONObject jsonObject = new JSONObject(jsondata);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String id = iterator.next();
                if (id.startsWith(proId.substring(0, 2)) && id.endsWith("00") && !id.endsWith("0000")) {
                    city = new City();
                    city.setId(id);
                    JSONArray jsonArray = jsonObject.getJSONArray(id);
                    city.setName((String) jsonArray.get(0));
                    LogTool.d(TAG, "id = " + id + " name =" + city.getName());
                    cityList.add(city);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    public static List<District> getDistrictByCityId(String jsondata, String cityId) {
        List<District> districtList = new ArrayList<>();
        District district = null;
        try {
            JSONObject jsonObject = new JSONObject(jsondata);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String id = iterator.next();
                if (id.startsWith(cityId.substring(0, 4))) {
                    district = new District();
                    district.setId(id);
                    JSONArray jsonArray = jsonObject.getJSONArray(id);
                    district.setName((String) jsonArray.get(0));
                    LogTool.d(TAG, "id = " + id + " name =" + district.getName());
                    districtList.add(district);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return districtList;
    }


}
