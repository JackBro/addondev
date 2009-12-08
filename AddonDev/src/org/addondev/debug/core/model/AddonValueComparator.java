package org.addondev.debug.core.model;

import java.util.Comparator;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;

public class AddonValueComparator implements Comparator<IVariable> {

	@Override
	public int compare(IVariable o1, IVariable o2) {	
		// TODO Auto-generated method stub
		int res = 0;
		try {
			res = o1.getName().compareTo(o2.getName());
		} catch (DebugException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

}
