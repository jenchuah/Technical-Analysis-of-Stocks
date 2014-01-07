/**
 *
 * @author jen_chuah
 */
public class SolutionHistory {

    public int period;
    public double eval;

    public PMSolution pms;

    public SolutionHistory(int p, double e){
        period = p;
        eval = e;
    }

    public SolutionHistory(PMSolution sol, double e){
        pms = sol;
        eval = e;
    }
}
