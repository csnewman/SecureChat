package com.securechat.common.plugins;

import java.util.ArrayList;
import java.util.List;

public class HookNode {
	private List<HookNode> dependencies;
	private HookInstance instance;

	public HookNode(HookInstance instance) {
		this.instance = instance;
		dependencies = new ArrayList<HookNode>();
	}
	
	public HookInstance getInstance() {
		return instance;
	}
	
	public void addDependency(HookNode node){
		if(!dependencies.contains(node)){
			dependencies.add(node);
		}
	}
	
	public List<HookNode> getDependencies() {
		return dependencies;
	}

	public String getName() {
		return instance != null ? instance.getName() : "root";
	}
	
}
