package me.mchiappinam.pdghx1;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comando implements CommandExecutor {

	private Main plugin;
	public Comando(Main main) {
		plugin=main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("x1")) {
			if (args.length==0) {
				plugin.help((Player)sender);
				return true;
			}
			if(args[0].equalsIgnoreCase("desafiar")) {
				if((args.length<2) || (args.length>2)) {
					plugin.help((Player)sender);
					return true;
				}
				if(plugin.andamento) {
					sender.sendMessage("§3[Ⓧ①] §cEstá acontecendo um x1 no momento. Tente novamente mais tarde.");
					return true;
				}
			    if(plugin.contagemFim) {
					sender.sendMessage("§3[Ⓧ①] §cAguarde o término da contagem para o vencedor do antigo x1 sair da arena.");
					return true;
			    }
				if(plugin.getServer().getPlayer(args[1]) == null) {
					sender.sendMessage("§3[Ⓧ①] §cO jogador §a"+args[1]+" §cnão está online.");
					return true;
				}
				if(sender.getName().contains(plugin.getServer().getPlayer(args[1]).getName())) {
					sender.sendMessage("§3[Ⓧ①] §cVocê não pode se desafiar.");
					return true;
				}
				double taxa = plugin.getConfig().getDouble("TaxaMoney")+plugin.getConfig().getDouble("TaxaConsole");
		        if (!(Main.econ.getBalance(sender.getName()) >= plugin.getConfig().getDouble("TaxaMoney")+plugin.getConfig().getDouble("TaxaConsole"))) {
				    sender.sendMessage("§3[Ⓧ①] §cVocê não tem money suficiente.");
				    sender.sendMessage("§3[Ⓧ①] §cMoney necessário: §a$"+taxa+"§c.");
				    return true;
		        }
		        if (!(Main.econ.getBalance(plugin.getServer().getPlayer(args[1]).getName()) >= plugin.getConfig().getDouble("TaxaMoney")+plugin.getConfig().getDouble("TaxaConsole"))) {
				    sender.sendMessage("§3[Ⓧ①] §cO jogador "+plugin.getServer().getPlayer(args[1]).getName()+" não tem money suficiente.");
				    sender.sendMessage("§3[Ⓧ①] §cMoney necessário: §a$"+taxa+"§c.");
				    return true;
		        }
                if(((Player)sender).isInsideVehicle()) {
				     sender.sendMessage("§3[Ⓧ①] §cVocê está dentro de um veículo!");
				     return true;
				}else if(plugin.getServer().getPlayer(args[1]).isInsideVehicle()) {
					sender.sendMessage("§3[Ⓧ①] §a"+plugin.getServer().getPlayer(args[1]).getName()+" §cestá dentro de um veículo!");
					plugin.getServer().getPlayer(args[1]).sendMessage("§3[Ⓧ①] §a"+sender.getName()+" §ctentou te desafiar para x1 com você dentro de um veículo!");
					return true;
				}
                if(((Player)sender).isDead()) {
				     sender.sendMessage("§3[Ⓧ①] §cVocê está morto!");
				     return true;
				}else if(plugin.getServer().getPlayer(args[1]).isDead()) {
					sender.sendMessage("§3[Ⓧ①] §a"+plugin.getServer().getPlayer(args[1]).getName()+" §cestá morto!");
					plugin.getServer().getPlayer(args[1]).sendMessage("§3[Ⓧ①] §a"+sender.getName()+" §ctentou te desafiar para x1 com você morto!");
					return true;
				}
	  			try {
	  				if (plugin.version == 2) {
	  					if(plugin.getClanPlayerManager().getAnyClanPlayer((Player)sender).getClan().getName()==plugin.getClanPlayerManager().getAnyClanPlayer(plugin.desafiado).getClan().getName()) {
	  						sender.sendMessage("§3[Ⓧ①] §cVocê não pode desafiar §a"+plugin.desafiado.getName()+" §cpois vocês são do mesmo clan. ("+plugin.getClanPlayerManager().getAnyClanPlayer((Player)sender).getClan().getName()+")");
	  						return true;
	  					}
	  				}else{
	  					if(plugin.core2.getClanManager().getClanByPlayerName(((Player)sender).getName()).getName()==plugin.core2.getClanManager().getClanByPlayerName(plugin.desafiado.getName()).getName()) {
	  						sender.sendMessage("§3[Ⓧ①] §cVocê não pode desafiar §a"+plugin.desafiado.getName()+" §cpois vocês são do mesmo clan. ("+plugin.core2.getClanManager().getClanByPlayerName(((Player)sender).getName()).getName()+")");
	  						return true;
	  					}
	  				}
	  			}catch (Exception e) {
	  				//sender.sendMessage("§4§l[PDGH] §c§lErro! Você não está registrado nos clans.");
	  			}
                plugin.desafiador=((Player)sender);
                plugin.desafiado=plugin.getServer().getPlayer(args[1]);
                plugin.andamento=true;
                Main.lista.add(sender.getName());
                plugin.tempoResposta();
                plugin.getServer().broadcastMessage("§3[Ⓧ①] §a"+plugin.desafiador.getName()+" §fvs §a"+plugin.desafiado.getName());
                plugin.getServer().broadcastMessage("§3[Ⓧ①] §fSerá que §a"+plugin.desafiado.getName()+" §faceita ou arrega? :p");
                plugin.desafiador.sendMessage(" ");
                plugin.desafiador.sendMessage("§3[Ⓧ①] §5"+ChatColor.BOLD+"✸ §a"+plugin.desafiado.getName()+" §ftem 1 minuto para aceitar ou negar o x1.");
                plugin.desafiado.sendMessage(" ");
                plugin.desafiado.sendMessage("§3[Ⓧ①] §5"+ChatColor.BOLD+"✸ §f1 minuto para aceitar ou negar o x1.");
                plugin.desafiado.sendMessage("§3[Ⓧ①] §5"+ChatColor.BOLD+"✸ §fAceite com o comando §a/x1 aceitar");
                plugin.desafiado.sendMessage("§3[Ⓧ①] §5"+ChatColor.BOLD+"✸ §fNegue com o comando §a/x1 negar");
                plugin.desafiado.sendMessage("§cTaxa de §a§l$"+taxa+"§c.");
                plugin.desafiado.sendMessage("§cPrêmio de §a§l$"+plugin.getConfig().getDouble("TaxaMoney")*2+" §cpara o vencedor.");
				return true;
			}else if(args[0].equalsIgnoreCase("aceitar")) {
				if(args.length>1) {
					plugin.help((Player)sender);
					return true;
				}
				if(!plugin.andamento) {
					sender.sendMessage("§3[Ⓧ①] §cNenhum x1 acontecendo no momento.");
					return true;
				}
			    if(plugin.contagemFim) {
					sender.sendMessage("§3[Ⓧ①] §cAguarde o tÃ©rmino da contagem para o vencedor do antigo x1 sair da arena.");
					return true;
			    }
		        if(Main.lista.contains(sender.getName())) {
		        	sender.sendMessage("§3[Ⓧ①] §cVocê não pode aceitar um desafio seu!");
		        	return true;
				}
		        if(!sender.getName().contains(plugin.desafiado.getName())) {
		        	sender.sendMessage("§3[Ⓧ①] §cVocê não foi o desafiado!");
		        	return true;
		        }
				double taxa = plugin.getConfig().getDouble("TaxaMoney")+plugin.getConfig().getDouble("TaxaConsole");
		        if(!(Main.econ.getBalance(sender.getName()) >= plugin.getConfig().getDouble("TaxaMoney")+plugin.getConfig().getDouble("TaxaConsole"))) {
				    sender.sendMessage("§3[Ⓧ①] §cVocê não tem money suficiente.");
				    sender.sendMessage("§3[Ⓧ①] §cMoney necessÃ¡rio: §a$"+taxa+"§c.");
				    return true;
				}
		        for(String l : Main.lista) {
					if(!(Main.econ.getBalance(l) >= plugin.getConfig().getDouble("TaxaMoney")+plugin.getConfig().getDouble("TaxaConsole"))) {
						sender.sendMessage("§3[Ⓧ①] §a"+plugin.getServer().getPlayer(l).getName()+" §cnão tem money suficiente.");
						sender.sendMessage("§3[Ⓧ①] §cMoney necessÃ¡rio: §a$"+taxa+"§c.");
						plugin.getServer().getPlayer(l).sendMessage("§3[Ⓧ①] §a"+sender.getName()+" §ctentou aceitar seu x1 com você sem money suficiente.");
						plugin.getServer().getPlayer(l).sendMessage("§3[Ⓧ①] §cMoney necessÃ¡rio: §a$"+taxa+"§c.");
						return true;
					}
					if(((Player)sender).isInsideVehicle()) {
				     	sender.sendMessage("§3[Ⓧ①] §cVocê estÃ¡ dentro de um veÃ­culo!");
				     	return true;
					}else if(plugin.getServer().getPlayer(l).isInsideVehicle()) {
						sender.sendMessage("§3[Ⓧ①] §a"+plugin.getServer().getPlayer(l).getName()+" §cestÃ¡ dentro de um veÃ­culo!");
						plugin.getServer().getPlayer(l).sendMessage("§3[Ⓧ①] §a"+sender.getName()+" §ctentou aceitar seu x1 com você dentro de um veÃ­culo!");
						return true;
					}
                	if(((Player)sender).isDead()) {
				     	sender.sendMessage("§3[Ⓧ①] §cVocê estÃ¡ morto!");
				     	return true;
					}else if(plugin.getServer().getPlayer(l).isDead()) {
						sender.sendMessage("§3[Ⓧ①] §a"+plugin.getServer().getPlayer(l).getName()+" §cestÃ¡ morto!");
						plugin.getServer().getPlayer(l).sendMessage("§3[Ⓧ①] §a"+sender.getName()+" §ctentou aceitar seu x1 com você morto!");
						return true;
					}
				}
	  			try {
	  				if (plugin.version == 2) {
	  					if(plugin.getClanPlayerManager().getAnyClanPlayer((Player)sender).getClan().getName()==plugin.getClanPlayerManager().getAnyClanPlayer(plugin.desafiador).getClan().getName()) {
	  						sender.sendMessage("§3[Ⓧ①] §cVocê não pode aceitar o x1 de §a"+plugin.desafiador.getName()+" §cpois vocês são do mesmo clan. ("+plugin.getClanPlayerManager().getAnyClanPlayer((Player)sender).getClan().getName()+")");
	  						return true;
	  					}
	  				}else{
	  					if(plugin.core2.getClanManager().getClanByPlayerName(((Player)sender).getName()).getName()==plugin.core2.getClanManager().getClanByPlayerName(plugin.desafiador.getName()).getName()) {
	  						sender.sendMessage("§3[Ⓧ①] §cVocê não pode aceitar o x1 de §a"+plugin.desafiador.getName()+" §cpois vocês são do mesmo clan. ("+plugin.core2.getClanManager().getClanByPlayerName(((Player)sender).getName()).getName()+")");
	  						return true;
	  					}
	  				}
	  			}catch (Exception e) {
	  				//sender.sendMessage("§4§l[PDGH] §c§lErro! Você não estÃ¡ registrado nos clans.");
	  			}
		        for(String l : Main.lista) {
		        	Main.econ.withdrawPlayer(l, plugin.getConfig().getDouble("TaxaMoney")+plugin.getConfig().getDouble("TaxaConsole"));
		        	plugin.getServer().getPlayer(l).setFoodLevel(20);
		        	plugin.getServer().getPlayer(l).setHealth(20);
		        }
	        	Main.econ.withdrawPlayer(sender.getName(), plugin.getConfig().getDouble("TaxaMoney")+plugin.getConfig().getDouble("TaxaConsole"));
	        	((Player)sender).setFoodLevel(20);
                plugin.ctempoResposta();
                Main.lista.add(sender.getName());
	            final Location loc1 = new Location(plugin.getServer().getWorld("world_x1"), 4.5, 53, 7.5);
	            loc1.setPitch(0);
	            loc1.setYaw(180);
	            final Location loc2 = new Location(plugin.getServer().getWorld("world_x1"), -3.5, 53, -6.5);
	            loc2.setPitch(0);
	            loc2.setYaw(0);
		        Random randomgen=new Random();
		        int i=randomgen.nextInt(2) + 1;
		        if(i == 1) {
					plugin.posTele=1;
					plugin.desafiador.teleport(loc1);
		        }else if(i == 2) {
					plugin.posTele=2;
					plugin.desafiador.teleport(loc2);
		        }
                if(plugin.posTele==1) {
					((Player)sender).teleport(loc2);
                }else{
					((Player)sender).teleport(loc1);
                }
                plugin.getServer().broadcastMessage("§3[Ⓧ①] §a"+plugin.desafiado.getName()+" §faceitou o x1 de §a"+plugin.desafiador.getName());
                plugin.tempoParaLiberarNaArena();
				return true;
			}else if(args[0].equalsIgnoreCase("negar")) {
				if(args.length>1) {
					plugin.help((Player)sender);
					return true;
				}
				if(!plugin.andamento) {
					sender.sendMessage("§3[Ⓧ①] §cNenhum x1 acontecendo no momento.");
					return true;
				}
			    if(plugin.contagemFim) {
					sender.sendMessage("§3[Ⓧ①] §cAguarde o tÃ©rmino da contagem para o vencedor do antigo x1 sair da arena.");
					return true;
			    }
		        if(Main.lista.contains(sender.getName())) {
		        	sender.sendMessage("§3[Ⓧ①] §cVocê não pode negar um desafio seu!");
		        	return true;
				}
		        if(!sender.getName().contains(plugin.desafiado.getName())) {
		        	sender.sendMessage("§3[Ⓧ①] §cVocê não foi o desafiado!");
		        	return true;
		        }
                plugin.ctempoResposta();
                plugin.getServer().broadcastMessage("§3[Ⓧ①] §a"+plugin.desafiado.getName()+" §fnegou o x1 de §a"+plugin.desafiador.getName());
                plugin.andamento=false;
                plugin.desafiador=null;
                plugin.desafiado=null;
        		Main.lista.clear();
				return true;
			}
		}
		return false;
	}
}