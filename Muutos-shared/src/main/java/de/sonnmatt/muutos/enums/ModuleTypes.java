package de.sonnmatt.muutos.enums;

/*
 * Right may need granted lower rights:
 * view -> modify -> create
 * Delete will only be possible if the entity was not actively used before.
 */
public enum ModuleTypes {
	UserManagement_View,
	UserManagement_Modify,
	UserManagement_Create,
	UserManagement_Delete,
	TenantManageent_View,
	TenantManagement_Modify,
	TenantMnagement_Create,
	TenantMnagement_Delete,
	TextManagement_View,
	TextManagement_Modify,
	TextManagement_Create,
	TextManagement_Delete;
}
