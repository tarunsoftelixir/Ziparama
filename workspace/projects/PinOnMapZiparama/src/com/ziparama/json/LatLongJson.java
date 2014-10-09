package com.ziparama.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LatLongJson {

	public List<LatLongData> getLatLong(String jsonString) throws JSONException {
		List<LatLongData> list = new ArrayList<LatLongData>();

		JSONObject json = new JSONObject(jsonString);
		JSONArray map = json.getJSONArray("maps");

		for (int i = 0; i < map.length(); i++) {
			JSONObject latLong = map.getJSONObject(i);
			double latitude = Double.parseDouble(latLong.getString("lat"));
			double longitude = Double.parseDouble(latLong
					.getString("longitude"));
			String title = latLong.getString("title");
			String description = latLong.getString("description");
			String image_url = latLong.getString("image_url");
			String category=latLong.getString("category");
			String subcategory=latLong.getString("subcategory");
			String pid=latLong.getString("mid");
			list.add(new LatLongData(latitude, longitude, pid,title, description,
					image_url,category,subcategory));
		}

		return list;
	}

}
