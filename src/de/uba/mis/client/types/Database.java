package de.uba.mis.client.types;

/**
 * Wrapper enum for the MESAP databases covered
 * 
 * @author Kevin Hausmann
 */
public enum Database {
	ZSE("ZSE_aktuell"),
	ZSE2018("ZSE_Submission_2018_20180214"),
	BEU("BEU"),
	POSO("PoSo"),
	ESz("ESZ"),
	Enerdat("Enerdat"),
	EMMa("EMMa"),
	EMMa2018("EMMa_2018");
	
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
			case ZSE2018: return "ZSE 2018";
			case BEU: return "BEU";
			case POSO: return "PoSo";
			case ESz: return "ESz";
			case Enerdat: return "Enerdat";
			case EMMa: return "EMMa";
			case EMMa2018: return "EMMa 2018";
		}
		
		return null;
	}
	
	/**
	 * @return A nice full display name for the database
	 */
	public String getLongDisplayName() {
		switch (this) {
			case ZSE: return "ZSE aktuell";
			case ZSE2018: return "ZSE Submission 2018";
			case BEU: return "Bilanz der Emissionsursachen (BEU)";
			case POSO: return "Punktquellen (PoSo)";
			case ESz: return "Emissionsszenarien";
			case Enerdat: return "Enerdat";
			case EMMa: return "Emissionsminderungsmaßnahmen";
			case EMMa2018: return "Emissionsminderungsmaßnahmen 2018";
		}
		
		return null;
	}
}
