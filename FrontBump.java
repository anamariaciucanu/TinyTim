import lejos.nxt.*;
import lejos.robotics.subsumption.*;

import java.lang.Math;

public class FrontBump implements Behavior{

	private TouchSensor sensor;
	private boolean suppressed = false;
	private int qoRotation = 222;
	
	public FrontBump(SensorPort _port)
	{
		sensor = new TouchSensor(_port);	
	}
	
	public boolean takeControl()
	{
		return sensor.isPressed(); 
	}
	
	public void suppress()
	{
		suppressed = true;
	}
	
	public void action()
	{
		suppressed = false;

		Motor.C.rotate(-360, true);
		Motor.B.rotate(-360, true);
		Motor.C.rotate((-1)*qoRotation, true);
		Motor.B.rotate(qoRotation, true);
		
		
		while(Motor.B.isMoving() && Motor.C.isMoving() && !suppressed)
		{
			Thread.yield();
		}
	}
}
