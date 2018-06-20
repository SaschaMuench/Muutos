package de.sonnmatt.muutos.jpa;

import java.util.List;

public interface IHierarchyElement {

	public String getId();
	
	public IHierarchyElement getParent();

	public int getLevel();

	public void setLevel(int level);

	public IHierarchyElement getTop();

	public void setTop(IHierarchyElement top);
	
	public Boolean isTop();

	public void setStructure(List<String> structure);
	
	public List<String> getStructure();
}