package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DownloadFilesFromOneDriveUrl {

	private static final int BUFFER_SIZE = 4096;

	@Value("${mailcredential.savedirectory}")
	private String saveDirectory;

	private void downloadFile(String fileURL, String saveDir) {
		try {
			if (fileURL.startsWith("https://1drv.ms/")) {
				fileURL = urlFormatter(fileURL);
			}
			if (fileURL != "") {
				URL url = new URL(fileURL);

				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				int responseCode = httpConn.getResponseCode();
				// always check HTTP response code first
				if (responseCode == HttpURLConnection.HTTP_OK) {
					String fileName = "";
					String disposition = httpConn.getHeaderField("Content-Disposition");

					if (disposition != null) {
						// extracts file name from header field
						int index = disposition.indexOf("filename=");
						if (index > 0) {
							fileName = disposition.substring(index + 10, disposition.length() - 1);
						}
					} else {
						// extracts file name from URL
						fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
					}

					log.info("fileName = " + fileName);
					// downloads pdf file only
					if (fileName.contains(".pdf")) {

						// opens input stream from the HTTP connection
						InputStream inputStream = httpConn.getInputStream();
						String saveFilePath = saveDir + File.separator + fileName;

						// opens an output stream to save into file
						FileOutputStream outputStream = new FileOutputStream(saveFilePath);

						int bytesRead = -1;
						byte[] buffer = new byte[BUFFER_SIZE];
						while ((bytesRead = inputStream.read(buffer)) != -1) {
							outputStream.write(buffer, 0, bytesRead);
						}

						outputStream.close();
						inputStream.close();

						log.info(fileName + " download Successful");

					} else {

						log.warn("This is not a pdf file");
					}
				} else {
					log.error("No file to download. Server replied HTTP code: " + responseCode);
				}
				httpConn.disconnect();
			} else {
				log.error("Url is not valid");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// modify url
	private String urlFormatter(String fileURL) {

		if (fileURL.contains("onedrive.live.com/redir")) {
			return fileURL.replace("redir", "download");
		} else {
			try {
				URL url = new URL(fileURL);

				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				int responseCode = httpConn.getResponseCode();
				// always check HTTP response code first
				if (responseCode == HttpURLConnection.HTTP_OK) {
					fileURL = httpConn.getURL().toString().replace("redir", "download");

				} else {
					fileURL = "";
					log.error("Cannot get refactored Url from Onedrive. Server replied HTTP code: " + responseCode);

				}
				httpConn.disconnect();
				return fileURL;
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	public void downloadFilesFromOneDriveUrl(String url) {

		downloadFile(url, saveDirectory);

	}

}
