package ServiceStrategic;
import java.util.*;
import model.Assignment;

public interface SchedulingStrategy {
	public ArrayList<Assignment> solve(ArrayList<Assignment> assignments);
    public String getAlgorithmName();
}
