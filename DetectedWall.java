import lejos.nxt.*;
import lejos.robotics.subsumption.*;

import java.lang.Math;

public class DetectedWall implements Behavior {
	
	private	boolean suppressed;
	private UltrasonicSensor sonar;
	private TouchSensor sensor;

	private int limSonarDistance = 18;
	private int qoRotation = 222;
	private short epsilon = 8;	
	private int DISTANCE_THRESHOLD = limSonarDistance+epsilon*4;
	private int currentSonarDistance = 0;
	private int diffDistance = 0;
	private int absDiffDistance = 0;
	private int frontDistance = 0;
	private int rotationAngle = 0; 
	private boolean wallmode = false;


	
		
	public DetectedWall(SensorPort _port1, SensorPort _port2)
	{
	 sonar = new UltrasonicSensor(_port1);
	 sensor = new TouchSensor(_port2);
	}
	   
	public boolean takeControl()
	{
		return (sonar.getDistance() < limSonarDistance && !sensor.isPressed());
	}
	
	public void suppress()
	{
		wallmode = false;
		suppressed = true;		
	}
	
	public void rotateAngle(int _angle, boolean _direction)
	{		
		if(_direction) //left
		{
			Motor.C.rotate((-1)*qoRotation*_angle/90, true);
			Motor.B.rotate(qoRotation*_angle/90, true);
		}
		else //right
		{
			Motor.B.rotate((-1)*qoRotation*_angle/90, true);
			Motor.C.rotate(qoRotation*_angle/90, true);
		}
		while (Motor.B.isMoving() && Motor.C.isMoving() && !suppressed)
		{
			Thread.yield();
		}
				
	}
	
	public void action()
	{
		suppressed = false;		
		
		//Saw a wall, turn left
		if(!wallmode)
		{
				rotateAngle(90, true);
				wallmode = true;	
		}
    			
		while(wallmode)
		{
			Motor.A.rotate(-90);
			currentSonarDistance = sonar.getDistance();
			Motor.A.rotate(90);
			frontDistance = sonar.getDistance();
			
			if(frontDistance <= limSonarDistance + epsilon/2)
			{
				rotateAngle(90, true);
				continue;
			}
			else
			{			
			diffDistance = currentSonarDistance - limSonarDistance;
			absDiffDistance = Math.abs(diffDistance);
			
			
			//Distance to obstacle is very big, turn right
			if(absDiffDistance > DISTANCE_THRESHOLD)
			{
					//Turn left 90
					LCD.drawString("Out to the right", 1, 1);
					rotateAngle(90, false);
					Motor.B.rotate(620, true);
					Motor.C.rotate(620, true);
					while (Motor.B.isMoving() && Motor.C.isMoving() && !suppressed)
					{
						Thread.yield();
					}
			}			
			else
			{	
				rotationAngle = (Motor.B.getAcceleration()*absDiffDistance)%30;
				
				if(diffDistance < 0)
				{
					//Robot turned against wall - turn away left
					rotateAngle(rotationAngle, true);
				}
				else if(diffDistance > 0 && absDiffDistance >= epsilon)
				{
					//Robot turned away from the wall - turn towards it right
					if(rotationAngle>10)
					{
						rotateAngle(rotationAngle, false);
					}
				}						

				Motor.B.forward();
				Motor.C.forward();
			}
			}
		}
	}
}

