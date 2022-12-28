package edu.upenn.cit594.util;

public class PropertyData {
	private final String zipcode;
	private final Double  marketValue;
	private final Double totalLivableArea;
	
	public PropertyData(String zipcode, Double marketValue, Double totalLivableArea){
		this.zipcode = zipcode;
		this.marketValue = marketValue;
		this.totalLivableArea = totalLivableArea;
	}

	public String getZipcode() {
		return zipcode;
	}

	public Double getTotalLivableArea() {
		return totalLivableArea;
	}
	
	public Double getMarketValue() {
		return marketValue;
	}

	
}
