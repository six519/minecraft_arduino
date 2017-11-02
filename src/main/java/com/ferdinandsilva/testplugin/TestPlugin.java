package com.ferdinandsilva.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import jssc.SerialPort;
import jssc.SerialPortException;

public final class TestPlugin extends JavaPlugin implements Listener {
	
	public Boolean ON = false;
	public SerialPort serialPort;

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		serialPort = new SerialPort("/dev/cu.usbmodem1421");
		
		try {
			serialPort.openPort();
			serialPort.setParams(9600, 8, 1, 0);
			
		}catch(SerialPortException e) {
			
		}
	}
	
    @Override
    public void onDisable() {
    	try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			
		}
    }
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block clicked = event.getClickedBlock();
			if(clicked.getType() == Material.STONE_BUTTON) {
				
				if(ON) {
					ON = false;
					p.sendMessage("You turn off the LED");
					try {
						serialPort.writeBytes("off".getBytes());
					} catch (SerialPortException e) {
						getLogger().info(e.getMessage());
					}
				} else {
					ON = true;
					p.sendMessage("You turn on the LED");
					try {
						serialPort.writeBytes("on".getBytes());
					} catch (SerialPortException e) {
						getLogger().info(e.getMessage());
					}
				}
				
			}
		}
	}
}
