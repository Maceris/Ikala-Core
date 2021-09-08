package com.ikalagaming.logging;

import com.ikalagaming.util.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Prints the logs to log files.
 *
 * @author Ches Burks
 *
 */
public class FileAppender implements LogAppender {

	/**
	 * Maximum size of a file in bytes.
	 */
	private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;
	/**
	 * Formatter for date times for (probably) unique filenames
	 */
	private static DateTimeFormatter formatter =
		DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
	private static final String LOG_FOLDER_PATH = System.getProperty("user.dir")
		+ File.separator + "logs" + File.separator;

	/**
	 * The current file we are logging to.
	 */
	private File currentFile;
	/**
	 * The file writer we are currently using to log. Wrapped by a buffered
	 * writer but stored so it can be closed.
	 */
	private FileWriter currentFileWriter;
	/**
	 * The buffered writer that wraps the current file writer to reduce system
	 * calls.
	 */
	private BufferedWriter currentBufferWriter;

	/**
	 * Create a file appender, set up the first log file.
	 */
	public FileAppender() {
		FileUtils.createFolder(FileAppender.LOG_FOLDER_PATH, "");
		this.startNewFile();
	}

	@Override
	public void append(String log) {
		if (this.currentFile.length() > FileAppender.MAX_FILE_SIZE) {
			this.startNewFile();
		}
		try {
			this.currentBufferWriter.write(log);
			this.currentBufferWriter.newLine();
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Close the old file writers, create a new file, and set up to start
	 * writing to that.
	 */
	private void startNewFile() {
		if (null != this.currentBufferWriter) {
			try {
				this.currentBufferWriter.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (null != this.currentFileWriter) {
			try {
				this.currentFileWriter.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		final String filename =
			LocalDateTime.now().format(FileAppender.formatter) + ".log";
		this.currentFile = new File(FileAppender.LOG_FOLDER_PATH + filename);
		try {
			FileUtils.createFile(FileAppender.LOG_FOLDER_PATH, filename);
			this.currentFileWriter = new FileWriter(this.currentFile);
			this.currentBufferWriter =
				new BufferedWriter(this.currentFileWriter);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		try {
			this.currentBufferWriter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
