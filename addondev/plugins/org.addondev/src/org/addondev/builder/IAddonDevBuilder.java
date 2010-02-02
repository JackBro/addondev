package org.addondev.builder;

import org.eclipse.core.resources.IResourceDelta;

public interface IAddonDevBuilder {
	public void visit(IResourceDelta delta);
}
