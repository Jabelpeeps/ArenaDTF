package mc.alk.dtf;

import mc.alk.arena.objects.teams.ArenaTeam;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class CapturePoint extends Flag{
	static int count = 0;
	/// Hopefully this doesnt conflict with a real entity id
	/// But chances should be very very slim
	final int id = count++;

	public CapturePoint(ArenaTeam team, ItemStack is, Location homeLocation) {
		super(team, is, homeLocation);
	}
	@Override
	public int getID(){
		return id;
	}

	@Override
	public void spawn(){
		/* do nothing */
	}

	@Override
	public Location getCurrentLocation() {return homeLocation;}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void remove() {
		/* do nothing */
	}
}
