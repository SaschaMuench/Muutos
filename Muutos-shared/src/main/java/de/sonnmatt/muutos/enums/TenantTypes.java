package de.sonnmatt.muutos.enums;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum TenantTypes implements Serializable, IsSerializable {
	Server,
	Group,
	Tenant,
	Company;
	
	@Override
	public String toString() {
		return super.toString();
	}
}
