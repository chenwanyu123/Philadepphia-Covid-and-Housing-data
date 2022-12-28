package edu.upenn.cit594.util;

public class CovidData {

	private final String etlTimestamp;
	private final String zipcode;
	private final Integer NEG;
	private final Integer POS;
	private final Integer deaths;
	private final Integer hospitalized;
	private final Integer partiallyVaccinated;
	private final Integer fullyVaccinated;
	private final Integer boosted;
	
	public CovidData(String etlTimestamp, String zipcode, Integer NEG, Integer POS, Integer deaths,
					 Integer hospitalized, Integer partiallyVaccinated, Integer fullyVaccinated, Integer boosted) {
		this.etlTimestamp = etlTimestamp;
		this.zipcode = zipcode;
		// negative test result
		this.NEG = NEG; // cumulative
		// positive test result
		this.POS = POS; // cumulative
		this.deaths = deaths; // cumulative
		this.hospitalized = hospitalized; // cumulative
		this.partiallyVaccinated = partiallyVaccinated; // cumulative
		this.fullyVaccinated =  fullyVaccinated; // cumulative
		this.boosted = boosted; // cumulative
	}

	public String getEtlTimestamp() { return this.etlTimestamp; }
	public String getZipcode() { return this.zipcode; }
	public Integer getNEG() { return this.NEG; }
	public Integer getPOS() { return this.POS; }
	public Integer getDeaths() { return this.deaths; }
	public Integer getHospitalized() { return this.hospitalized; }
	public Integer getPartiallyVaccinated() {
		return partiallyVaccinated;
	}
	public Integer getFullyVaccinated() {
		return fullyVaccinated;
	}
	public Integer getBoosted() {
		return boosted;
	}
	
}