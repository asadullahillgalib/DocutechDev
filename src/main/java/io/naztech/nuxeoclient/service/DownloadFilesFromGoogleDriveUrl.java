package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DownloadFilesFromGoogleDriveUrl {

	private static final int BUFFER_SIZE = 4096;

	@Value("${mailcredential.savedirectory}")
	private String saveDirectory;

	private void downloadFile(String fileURL, String saveDir) {
		try {
			String newUrl = urlFormatter(fileURL);
			URL url = new URL(newUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();

			// always check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String fileName = "";
				String disposition = httpConn.getHeaderField("Content-Disposition");

				fileName = getFileNameFromUrl(disposition, newUrl);
				log.info("fileName = " + fileName);
				
				//downloads pdf file only
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

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFileNameFromUrl(String disposition, String newUrl) {
		String fileName = "";

		int index1 = disposition.indexOf("filename=");
		index1 += 10;
		int index2 = disposition.indexOf("\";filename*=UTF-8");
		fileName = disposition.substring(index1, index2);
		return fileName;
	}

	/*
	 * Handles 3 kinds of links (they can be preceeded by https://): -
	 * drive.google.com/open?id=FILEID - (Still in progress)
	 * drive.google.com/file/d/FILEID/view?usp=sharing -
	 * drive.google.com/uc?id=FILEID&export=download
	 */
	private String urlFormatter(String url) {
		int index1 = 0;
		int index2 = 0;
		if (url.contains("export=download")) {
			return url;
		} else if (url.contains("file/d/")) {

			index1 = url.indexOf("file/d/");
			index1 += 7;
			index2 = url.indexOf("/view");

		} else if (url.contains("open?id=")) {
			index1 = url.indexOf("open?id=");
			index1 += 8;
			index2 = url.length();
		}

		String fileId = url.substring(index1, index2);
		String finalUrl = "https://drive.google.com/uc?authuser=0&id=" + fileId + "&export=download";
		return finalUrl;
	}

	public void downloadFileFromGoogledrive(String url) {
		downloadFile(url, saveDirectory);

	}
}
