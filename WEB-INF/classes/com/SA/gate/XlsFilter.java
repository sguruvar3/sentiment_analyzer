package com.SA.gate;
import java.io.File;
import java.io.FilenameFilter;

public class XlsFilter implements FilenameFilter {
	String ext;
	public XlsFilter(String ext)
	{
		 this.ext = "."+ext;
	}
	public boolean accept(File dir, String name) {
		return name.endsWith(ext);
	}

	  
}
