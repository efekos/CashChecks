package me.efekos.cashchecks.config;

import me.efekos.cashchecks.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config {

    private String resourceName;

    private File file;
    private FileConfiguration fileConfiguration;


    public Config(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setup(){
        file = new File(Main.getInstance().getDataFolder(),resourceName);

        if(!file.exists()){
            try{
                Main.getInstance().saveResource(resourceName, false);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get(){
        return  fileConfiguration;
    }

    public String getString(String path,String def){
        return fileConfiguration.getString(path,def);
    }
    public int getInt(String path,Integer def){
        return fileConfiguration.getInt(path,def);
    }
    public boolean getBoolean(String path,Boolean def){
        return fileConfiguration.getBoolean(path,def);
    }
    public List<String> getStringList(String path){
        return fileConfiguration.getStringList(path);
    }
    public Location getLocation(String path,Location def){
        return fileConfiguration.getLocation(path,def);
    }
    public Location getLocation(String path){
        return fileConfiguration.getLocation(path);
    }
    public double getDouble(String path,double def){
        return fileConfiguration.getDouble(path,def);
    }

    public void reload(){
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
