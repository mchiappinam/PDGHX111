package me.mchiappinam.pdghx1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayerManager;

import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class Main extends JavaPlugin {
	
	protected static List<String> lista=new ArrayList<String>();
	public boolean andamento=false;
	public boolean contagemFim=false;
	public Player desafiador=null;
	public Player desafiado=null;
	public Player vencedor=null;
	public Player perdedor=null;
	int ttempoResposta;
	//protected static List<String> camarote=new ArrayList<String>();

	protected static Economy econ=null;
	protected SCCore core;
	protected SimpleClans core2;
	protected int version = 0;

	public void onEnable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2iniciando...");
		File file = new File(getDataFolder(),"config.yml");
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2verificando se a config existe...");
		if(!file.exists()) {
			try {
				getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2config inexistente, criando config...");
				saveResource("config_template.yml",false);
				File file2 = new File(getDataFolder(),"config_template.yml");
				file2.renameTo(new File(getDataFolder(),"config.yml"));
				getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2config criada");
			}catch(Exception e) {getServer().getConsoleSender().sendMessage("§c[PDGHX1] §cERRO AO CRIAR CONFIG");}
		}
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2config criada");
		
		if(!setupEconomy()) {
			getLogger().warning("ERRO: Vault (Economia) nao encontrado!");
			getLogger().warning("Desativando o plugin...");
			getServer().getPluginManager().disablePlugin(this);
        }

		if (hookSimpleClans()) {
			getLogger().info("Hooked to SimpleClans2!");
			version = 2;
		}else if (getServer().getPluginManager().getPlugin("SimpleClans") != null) {
			getLogger().info("Hooked to SimpleClans!");
			core2 = ((SimpleClans)getServer().getPluginManager().getPlugin("SimpleClans"));
			version = 1;
		}else{
			getServer().getPluginManager().disablePlugin(this);
		}
		
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2registrando eventos...");
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2eventos registrados");
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2definindo comandos...");
	    getServer().getPluginCommand("x1").setExecutor(new Comando(this));
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2comandos definidos");
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2ativado - Developed by mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2Acesse: http://pdgh.com.br/");
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2Acesse: https://hostload.com.br/");
	}
	    
	public void onDisable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2desativado - Developed by mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2Acesse: http://pdgh.com.br/");
		getServer().getConsoleSender().sendMessage("§3[PDGHX1] §2Acesse: https://hostload.com.br/");
	}
	
	public void resetar() {
        andamento=false;
        desafiador=null;
        desafiado=null;
    	lista.clear();
	}
	
	public void tempoResposta() {
    	ttempoResposta = getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    		public void run() {
                resetar();
                getServer().broadcastMessage("§3[Ⓧ①] §a"+desafiado.getName()+" §fnegou o x1 de §a"+desafiador.getName()+" §fpois passou o tempo limite de resposta.");
    		}
    	}, 60*20L);
	}
	
	public void help(Player p) {
		double taxa = getConfig().getDouble("TaxaMoney")+getConfig().getDouble("TaxaConsole");
		double taxaCamarote = getConfig().getDouble("TaxaMoney")/20;
		p.sendMessage("§3§lPDGH X1 - Comandos:");
		p.sendMessage("§2/x1 desafiar <nick> -§a- Desafia alguém para o x1.");
		p.sendMessage("§2/x1 aceitar -§a- Aceita o x1 desafiado.");
		p.sendMessage("§2/x1 negar -§a- Nega o x1 desafiado.");
		p.sendMessage("§2/x1 camarote -§a- Vai até o camarote do x1 §6(somente VIPs)§a.");
		p.sendMessage("§2/x1 info -§a- Status do x1.");
		p.sendMessage("§cTaxa do x1 de §a§l$"+taxa+"§c.");
		p.sendMessage("§cPrÃªmio de §a§l$"+getConfig().getDouble("TaxaMoney")*2+" §cpara o vencedor.");
		p.sendMessage("§cTaxa do camarote de §a§l$"+taxaCamarote+"§c.");
		p.sendMessage("§cLimite de 1 x1 por vez.");
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp=getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ=rsp.getProvider();
        return econ != null;
	}

	private boolean hookSimpleClans() {
		try {
			for(Plugin plugin : getServer().getPluginManager().getPlugins()) {
				if ((plugin instanceof SCCore)) {
					core = ((SCCore)plugin);
					return true;
				}
			}
		}catch (NoClassDefFoundError e) {
			return false;
		}
		return false;
	}

	public ClanPlayerManager getClanPlayerManager() {
		return core.getClanPlayerManager();
	}
}
