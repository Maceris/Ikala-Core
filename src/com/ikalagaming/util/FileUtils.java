
package com.ikalagaming.util;

import java.io.File;
import java.io.IOException;

/**
 * 
 * Contains helper methods for handling files.
 * 
 * @author Ches Burks
 * 
 */
public class FileUtils {

	/**
	 * Returns true if a file or directory exists at the given path.
	 * 
	 * @param path the path to the file/directory
	 * @return true if the supplied file/directory exists
	 */
	public static boolean fileExists(String path) {
		if (path == null) {
			return false;
		}
		File f;
		/*
		 * Will not throw an exception because path can't be null
		 */
		f = new File(path);
		boolean exists = false;
		try {
			exists = f.exists();
		}
		catch (SecurityException e) {
			/*
			 * Cannot read that location due to security manager, so just assume
			 * it does not exist.
			 */
			exists = false;
		}
		return exists;
	}

	/**
	 * Returns a reference to the requested file. The file may be null, and no
	 * permissions are guaranteed.
	 * 
	 * @param path the path of the file
	 * @return the file requested
	 */
	public static File getFile(String path) {
		if (path == null) {
			return null;
		}
		File f;
		f = new File(path);
		return f;
	}

	/**
	 * Creates a blank file at the supplied path with the supplied name. If the
	 * file already exists, or the file location is not writable by the program,
	 * no file is created and false is returned.
	 * 
	 * @param path the path of the parent folder
	 * @param filename the name of the file
	 * @return true if the file was created, otherwise false
	 */
	public static boolean createFile(String path, String filename) {
		File f;
		boolean success = true;
		f = new File(path + filename);
		if (!fileExists(path + filename)) {
			try {
				f.createNewFile();
			}
			catch (IOException e) {
				success = false;
			}
			catch (SecurityException e2) {
				success = false;
			}
		}
		else {
			success = false;
		}
		return success;
	}

	/**
	 * Creates an empty folder at the supplied path with the supplied name. If
	 * the folder already exists, or the folder location is not writable by the
	 * program, no folder is created and false is returned. Any necessary but
	 * nonexistent parent directories will be created.
	 * 
	 * @param path the path of the folder to create
	 * @return true if the folder was created, otherwise false
	 */
	public static boolean createFolder(String path) {
		File f;
		boolean success = true;
		f = new File(path);
		if (!fileExists(path)) {
			try {
				f.mkdirs();
			}
			catch (SecurityException e) {
				success = false;
			}
		}
		else {
			success = false;
		}
		return success;
	}

	/**
	 * If the supplied path is a file, it exists, and it can be read and written
	 * to, then the file is deleted.
	 * 
	 * @param path the path of the file
	 */
	public static void deleteFile(String path) {
		if (path == null) {
			return;
		}
		File f;
		f = new File(path);
		try {
			if (!f.exists()) {
				return;
			}
			if (!f.isFile()) {
				return;
			}
			if (!f.canRead()) {
				return;
			}
			if (!f.canWrite()) {
				return;
			}
			f.delete();
		}
		catch (SecurityException e) {
			return;// if it can't be accessed, then it can't be deleted.
		}
	}

	/**
	 * If the supplied path is a folder, it exists, is empty, and it can be read
	 * and written to, then the folder is deleted.
	 * 
	 * @param path the path of the folder
	 */
	public static void deleteFolder(String path) {
		if (path == null) {
			return;
		}
		File f;
		f = new File(path);
		try {
			if (!f.exists()) {
				return;
			}
			if (!f.isDirectory()) {
				return;
			}
			if (!f.canRead()) {
				return;
			}
			if (!f.canWrite()) {
				return;
			}
			if (!(f.list().length == 0)) {
				return;// it contains files
			}
			f.delete();
		}
		catch (SecurityException e) {
			return;// if it can't be accessed, then it can't be deleted.
		}
	}
}
