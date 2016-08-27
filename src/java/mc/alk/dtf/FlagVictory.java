package mc.alk.dtf;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import mc.alk.arena.competition.Match;
import mc.alk.arena.controllers.messaging.MatchMessager;
import mc.alk.arena.events.matches.MatchIntervalMessageEvent;
import mc.alk.arena.events.matches.MatchTimeExpiredMessageEvent;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.scoreboard.ArenaDisplaySlot;
import mc.alk.arena.objects.scoreboard.ArenaObjective;
import mc.alk.arena.objects.scoreboard.ArenaScoreboard;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.objects.victoryconditions.VictoryCondition;
import mc.alk.arena.objects.victoryconditions.interfaces.DefinesLeaderRanking;
import mc.alk.arena.util.TimeUtil;

public class FlagVictory extends VictoryCondition implements DefinesLeaderRanking{

	final ArenaObjective scores;
	Integer capturesToWin;
	MatchMessager mmh;
	Map<ArenaTeam, Flag> teamFlags;

	public FlagVictory(Match match) {
		super(match);
		this.scores = new ArenaObjective("FlagCaptures", "Defend the Flag");
		scores.setDisplayName(ChatColor.GOLD+"Flag Captures");
		ArenaScoreboard scoreboard = match.getScoreboard();
		scores.setDisplaySlot(ArenaDisplaySlot.SIDEBAR);
		scoreboard.addObjective(scores);

		/// set all points to 0 so they display in Scoreboard
		this.resetScores();
	}

	public String getScoreString() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("{prefix}", match.getParams().getPrefix());
		String teamstr = mmh.getMessage("DefendTheFlag.teamscore");
		String separator = mmh.getMessage("DefendTheFlag.teamscore_separator");
		StringBuilder sb = new StringBuilder();
		List<ArenaTeam> teams = match.getTeams();
		if (teams == null)
			return "";
		boolean first = true;
		for (int i=0;i<teams.size();i++){
			if (!first) sb.append(separator);
			ArenaTeam t = teams.get(i);
			Flag f = teamFlags.get(t);
			Map<String,String> map2 = new HashMap<String,String>();
			map2.put("{team}", t.getDisplayName());
			map2.put("{captures}",scores.getPoints(t)+"");
			map2.put("{maxcaptures}",capturesToWin+"");
			String holder = null;
			if (f.isHome()){
				holder = mmh.getMessage("DefendTheFlag.flaghome");
			} else if (!(f.getEntity() instanceof Player)){
				holder = mmh.getMessage("DefendTheFlag.flagfloor");
			} else {
				Player p = (Player) f.getEntity();
				holder = mmh.getMessage("DefendTheFlag.flagperson") + p.getDisplayName();
			}
			map2.put("{flagholder}",holder);
			String str = mmh.format(teamstr,map2);
			sb.append(str);
			first = false;
		}
		map.put("{teamscores}",sb.toString());
		return mmh.getMessage("DefendTheFlag.score",map);
	}

	public void resetScores(){
		scores.setAllPoints(match, 1);
		scores.setAllPoints(match, 0);
	}

	public Integer addScore(ArenaTeam team, ArenaPlayer ap) {
		scores.addPoints(ap, 1);
		return scores.addPoints(team, 1);
	}

	@ArenaEventHandler
	public void onMatchIntervalMessage(MatchIntervalMessageEvent event){
		Map<String,String> map = new HashMap<String,String>();
		map.put("{prefix}", match.getParams().getPrefix());
		map.put("{timeleft}", TimeUtil.convertSecondsToString(event.getTimeRemaining()));
		map.put("{score}", getScoreString());
		event.setMatchMessage(mmh.getMessage("DefendTheFlag.time_remaining", map));
	}

	@ArenaEventHandler
	public void onMatchTimeExpiredMessage(MatchTimeExpiredMessageEvent event){
		StringBuilder sb = new StringBuilder();
		Map<String,String> map = new HashMap<String,String>();
		match.setMatchResult(scores.getMatchResult(match));
		map.put("{prefix}", match.getParams().getPrefix());
		String node = null;
		switch(match.getMatchResult().getResult()){
		case WIN:
			for (ArenaTeam t: match.getMatchResult().getVictors()){
				sb.append(t.getDisplayName() +" ");
			}
			node = "DefendTheFlag.time_expired_win";
			break;
		case TIE:
			for (ArenaTeam t: match.getMatchResult().getDrawers()){
				sb.append(t.getDisplayName() +" ");
			}
			node = "DefendTheFlag.time_expired_draw";
			break;
		default:
			/// not really sure...
			node = "DefendTheFlag.time_expired_draw";
			break;
		}
		map.put("{teams}", sb.toString());
		event.setMatchMessage(mmh.getMessage(node, map));
	}

	public void setFlags(Map<ArenaTeam, Flag> teamFlags) {
		this.teamFlags = teamFlags;
	}

	public void setNumCaptures(int capturesToWin) {
		this.capturesToWin = capturesToWin;
	}

	public void setMessageHandler(MatchMessager mmh) {
		this.mmh = mmh;
	}

	public List<ArenaTeam> getLeaders() {
		return scores.getLeaders();
	}

	public TreeMap<?, Collection<ArenaTeam>> getRanks() {
		return scores.getRanks();
	}
}
