package org.addondev.parser.xul;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;

public class XULParser {
	//<!DOCTYPE overlay SYSTEM "chrome://dendzones/locale/dendzones.dtd">
	//<?xml-stylesheet href="chrome://dendzones/skin/dendzones.css" type="text/css"?>
	
	private static Pattern content = Pattern.compile("<!DOCTYPE overlay SYSTEM \"{.*} \">");
	
	public XULParser()
	{
		
	}

	public void parse(IFile file, int firstpos)
	{
		
	}
	
	public void parseDTD(String xml)
	{
		
	}
}
