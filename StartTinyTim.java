import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class StartTinyTim {

	public static void main(String[] args) {

		Motor.A.setAcceleration(6000);
		Motor.B.setAcceleration(4000);
		Motor.C.setAcceleration(4000);
		
		Behavior b1 = new MoveForward();
		Behavior b2 = new DetectedWall(SensorPort.S4, SensorPort.S2);
		Behavior b3 = new FrontBump(SensorPort.S2);
					
		Behavior [] behaviors = {b1, b2, b3};
		Arbitrator arb = new Arbitrator(behaviors);
		
	
		arb.start();
	}

}
