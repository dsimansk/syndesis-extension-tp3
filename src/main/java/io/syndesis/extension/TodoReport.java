package io.syndesis.extension;

import java.util.List;

public class TodoReport {

	private String vendor;

	private String contactName;
	private String contactNumber;

	private String damagedItems;

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getDamagedItems() {
		return damagedItems;
	}

	public void setDamagedItems(String damagedItems) {
		this.damagedItems = damagedItems;
	}
}
