package com.ziparama.json;

public class LatLongData {

	private double latitude;
	private double longitude;
	private String title;
	private String description;
	private String mid;

	private String imageUrl;
	private String category;
	private String subcategory;

	public LatLongData(double latitude, double longitude, String pid,
			String title, String description, String imageUrl, String category,
			String subcategory) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
		this.description = description;
		this.imageUrl = imageUrl;
		this.mid = pid;
		this.setCategory(category);
		this.setSubcategory(subcategory);
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

}
