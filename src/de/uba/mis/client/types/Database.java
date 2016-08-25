package de.uba.mis.client.types;

/**
 * Wrapper enum for the MESAP databases covered
 * 
 * @author Kevin Hausmann
 */
public enum Database {
	ZSE("ZSE_aktuell"),
	ZSE2014("ZSE_Submission_2014_20140303"),
	ZSE2013("ZSE_Submission_2013_20130220"),
	BEU("BEU"),
	POSO("PoSo"),
	ESz("ESZ"),
	Enerdat("Enerdat");
	
	private String id;
	
	private Database(String id) {
		this.id = id;
	}
	
	/**
	 * Get database as refered to in JSON
	 * @param id JSON identifier
	 * @return Database in question
	 */
	public static Database getByID(String id) {
		for (Database database : Database.values())
			if (database.id.equalsIgnoreCase(id)) return database;
		
		return null;
	}
	
	/**
	 * @return A nice short display name for the database
	 */
	public String getDisplayName() {
		switch (this) {
			case ZSE: return "ZSE";
			case ZSE2014 : return "ZSE 2014";
			case ZSE2013 : return "ZSE 2013";
			case BEU: return "BEU";
			case POSO: return "PoSo";
			case ESz: return "ESz";
			case Enerdat: return "Enerdat";
		}
		
		return null;
	}
	
	/**
	 * @return A nice full display name for the database
	 */
	public String getLongDisplayName() {
		switch (this) {
			case ZSE: return "ZSE aktuell";
			case ZSE2014 : return "ZSE Submission 2014";
			case ZSE2013 : return "ZSE Submission 2013";
			case BEU: return "Bilanz der Emissionsursachen (BEU)";
			case POSO: return "Punktquellen (PoSo)";
			case ESz: return "Emissionsszenarien";
			case Enerdat: return "Enerdat";
		}
		
		return null;
	}
}
