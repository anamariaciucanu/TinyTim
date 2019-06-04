import lejos.nxt.*;
import lejos.robotics.subsumption.*;

public class MoveForward implements Behavior {
	
	private boolean suppressed = false;
	
	
	public boolean takeControl()
	{
		return true;		
	}
	
	public void suppress()
	{
		suppressed = true;		
	}


	public void action()
	{
		suppressed = false;
		
		Motor.B.forward();
		Motor.C.forward();		
			    
		while((Motor.B.isMoving() || Motor.C.isMoving()) && !suppressed)
			Thread.yield();	
	}
	


}

