package Util;

public class InstallerUtilJNI {
	public native String whereIsIinstalled();
	public String installDir(){
		return whereIsIinstalled();
	}

}
