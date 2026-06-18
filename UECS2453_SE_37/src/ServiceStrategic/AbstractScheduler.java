package ServiceStrategic;
import java.util.*;
import java.time.LocalDate;
import model.Assignment;

public abstract class AbstractScheduler implements SchedulingStrategy{
	public abstract String getAlgorithmName();
	public abstract ArrayList<Assignment> solve (ArrayList<Assignment> list);
	public abstract void printResult(ArrayList<Assignment> list);
}
