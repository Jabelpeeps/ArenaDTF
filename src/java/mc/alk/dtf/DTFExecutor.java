package mc.alk.dtf;

import org.bukkit.command.CommandSender;

import mc.alk.arena.BattleArena;
import mc.alk.arena.competition.Match;
import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.serializers.ArenaSerializer;
import mc.alk.arena.util.MessageUtil;

public class DTFExecutor extends CustomCommandExecutor{

	@MCCommand(cmds={"addFlag"}, admin=true)
	public static boolean addFlag(ArenaPlayer sender, Arena arena, Integer index) {
		if (!(arena instanceof DTFArena)){
			return MessageUtil.sendMessage(sender,"&eArena " + arena.getName() +" is not a CTF arena!");
		}
		if (index < 1 || index > 100){
			return MessageUtil.sendMessage(sender,"&2index must be between [1-100]!");}

		DTFArena ctf = (DTFArena) arena;
		ctf.addFlag(index -1, sender.getLocation());
		ArenaSerializer.saveArenas(DTF.getSelf());
		return MessageUtil.sendMessage(sender,"&2Team &6"+index+"&2 flag added!");
	}

	@MCCommand(cmds={"clearFlags"}, admin=true)
	public static boolean clearFlags(CommandSender sender, Arena arena) {
		if (!(arena instanceof DTFArena)){
			return MessageUtil.sendMessage(sender,"&eArena " + arena.getName() +" is not a CTF arena!");
		}
		DTFArena ctf = (DTFArena) arena;
		ctf.clearFlags();
		return MessageUtil.sendMessage(sender,"&2Flags cleared for &6"+arena.getName());
	}
	@MCCommand(cmds={"capture"}, op=true)
	public static boolean capture(CommandSender sender, ArenaPlayer player) {
		Match m = BattleArena.getBAController().getMatch(player);
		((DTFArena)m.getArena()).captured(player);
		return true;
	}
}
