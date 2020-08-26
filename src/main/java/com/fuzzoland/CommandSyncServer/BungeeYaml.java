package com.fuzzoland.CommandSyncServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.bukkit.entity.Player;

import net.md_5.bungee.config.YamlConfiguration;


public class BungeeYaml {
 
	private File file = null;
	private net.md_5.bungee.config.Configuration yaml;
	
	public BungeeYaml(String path, String f) {
		
		File directory = new File(path);
		
		if(!directory.exists()) {
			
			directory.mkdirs();
			
		}
		
		this.file = new File(path + File.separator + f);
		
		if(!file.exists()) {
			
			if(!loadfromStream(f)) {
				try {
					file.createNewFile();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.load();
	}
	
    public void download(Player staff, URL file) {
        try {
        		BufferedInputStream inputStream = new BufferedInputStream(file.openStream());  
        		FileOutputStream fileOS = new FileOutputStream(this.file);
        	    byte data[] = new byte[1024];
        	    int byteContent;
        	    while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
        	        fileOS.write(data, 0, byteContent);
        	    }
        	    fileOS.flush();
        	    inputStream.close();
        	    fileOS.close();
        } catch (Exception ec) {
            ec.printStackTrace();
        }
        this.load();
    }
    
	public boolean loadfromStream(String fileName) {
        try {
        	InputStream jarURL = CSS.getInstance().getResourceAsStream(fileName);
            if(jarURL!=null) {
	            copyFile(jarURL, file);
	            this.load();
	            return true;
            }else {
            	return false;
            }
        } catch (Exception e) {
    		e.printStackTrace();
        	return false;
        }
	}
	
    private void copyFile(InputStream in, File out) throws Exception {
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out,false);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
    
	 
	private void load() {
		try {
			this.yaml = net.md_5.bungee.config.ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
	 
	public void save() {
		try {
			 net.md_5.bungee.config.ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.yaml, this.file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	 
	public void delete() {
		try {
			this.file.delete();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	 
//	public FileConfiguration getcfg() {
//		return (FileConfiguration) YamlConfiguration.loadConfiguration(file);
//	}
	
	public int getInteger(String s) {
		return this.yaml.getInt(s);
	}
	 
	public int getInteger(String s,int s2) {
		return this.yaml.getInt(s,s2);
	}
	
	public void reload() {
		this.save();
		this.load();
	}
	 
	public String getString(String s) {
		return this.yaml.getString(s);
	}
	
	public String getString(String s,String s2) {
		return this.yaml.getString(s,s2);
	}
	
	public Object get(String s) {
		return this.yaml.get(s);
	}
	
	public boolean getBoolean(String s) {
		return this.yaml.getBoolean(s);
	}
	 
	public boolean getBoolean(String s,boolean s2) {
		return this.yaml.getBoolean(s,s2);
	}
	
	public void add(String s, Object o) {
		if(!this.contains(s)) {
			this.set(s, o);
		} 
	}
	 
	public void addToStringList(String s, String o) {
		this.yaml.getStringList(s).add(o);
	}
	 
	public void removeFromStringList(String s, String o) {
		this.yaml.getStringList(s).remove(o);
	}
	 
	public java.util.List<String> getStringList(String s) {
		return this.yaml.getStringList(s);
	}
	
//	public void addToIntegerList(String s, int o) {
//		this.yaml.getIntegerList(s).add(o);
//	}
//	 
//	public void removeFromIntegerList(String s, int o) {
//		this.yaml.getIntegerList(s).remove(o);
//	}
//
//	public java.util.List<Integer> getIntegerList(String s) {
//		return this.yaml.getIntegerList(s);
//	}
	 
	public void createNewStringList(String s, java.util.List<String> list) {
		this.yaml.set(s, list);
	}
	 
	public void createNewIntegerList(String s, java.util.List<Integer> list) {
		this.yaml.set(s, list);
	}
	 
	public void remove(String s) {
		this.set(s, null);
	}
	 
	public boolean contains(String s) {
		return this.yaml.contains(s);
	}
	 
	public double getDouble(String s) {
		return this.yaml.getDouble(s);
	}
	 
	public void set(String s, Object o) {
		this.yaml.set(s, o);
	}
	 
	public void increment(String s) {
		this.yaml.set(s, this.getInteger(s) + 1);
	}
	 
	public void decrement(String s) {
		this.yaml.set(s, this.getInteger(s) - 1);
	}

	public void increment(String s, int i) {
		this.yaml.set(s, this.getInteger(s) + i);
	}

	public void decrement(String s, int i) {
		this.yaml.set(s, this.getInteger(s) - i);
	}
	 
//	public YamlConfigurationOptions options() {
//		return this.yaml.options();
//	}

	public Long getLong(String s) {
		return this.yaml.getLong(s);
	}
	
//	public ConfigurationSection getConfigurationSection(String name) {
//		ConfigurationSection List = this.yaml.getConfigurationSection(name);
//		return List;
//	}

	public long getLong(String s, long s2) {
		return this.yaml.getLong(s,s2);
	}

	public float getDouble(String string, double d) {
		return (float) this.yaml.getDouble(string, d);
	}
}