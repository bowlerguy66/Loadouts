package me.bowlerguy66.loadouts;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileLoading {
	
	String basePath;
	
	public FileLoading(String basePath) {
		this.basePath = basePath;
	}
	
	HashMap<String, FileConfiguration> loadedFiles = new HashMap<String, FileConfiguration>();

	public String getBasePath() {
		return basePath;
	}
	
	public static String getSeperator() {
		return System.getProperty("file.separator");
	}
	
	public FileConfiguration getFile(String fileName) {
		fileName = basePath + fileName;
		if(loadedFiles.containsKey(fileName)) {
			return loadedFiles.get(fileName);
		} else {
			loadFile(fileName);
			return loadedFiles.get(fileName);			
		}
	}
		
	public void saveFile(String fileName) {

		fileName = basePath + fileName;

		if(!loadedFiles.containsKey(fileName)) {
			return;
		}
		
		File file = new File(fileName);
		FileConfiguration fileConfig = loadedFiles.get(fileName);
		
		try {
			fileConfig.save(file);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadFile(String path) {
		
		File file = new File(path);
		FileConfiguration fileConfiguration = null;
		
		String FILE_WAS_LOADED = "Loaded file ";
		String FILE_CREATED = "Created file ";
		String FILE_COULD_NOT_BE_LOADED = "Failed to load ";
		String FILE_FAILED_CREATION = "Failed to create file ";
		
		if(!file.exists()) {
			print("File doesn't exist");
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
				print(FILE_CREATED + file.getPath());			
			} catch(Exception e) {
				print(FILE_FAILED_CREATION + file.getPath());	
				return;
			}

		}

		YamlConfiguration conf = new YamlConfiguration();
		try {
			conf.load(file);
		} catch (Exception e) {
			print(FILE_COULD_NOT_BE_LOADED + file.getPath());
			e.printStackTrace();
			return;
		}
		fileConfiguration = conf;

		print(FILE_WAS_LOADED + file.getPath());
		loadedFiles.put(path, fileConfiguration);
		
	}
	
	public void print(String s) {
		boolean print = false;
		if(print) {
			System.out.println(s);
		}
	}
	
}