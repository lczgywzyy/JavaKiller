package u.can.i.up;

import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.toolkits.ide.exampleproblems.IFDSReachingDefinitions;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.toolkits.scalar.Pair;

import java.util.Map;
import java.util.Set;

public class IFDSDataFlowTransformer extends SceneTransformer {
    @Override
    protected void internalTransform(String phaseName, Map<String, String> options) {
        JimpleBasedInterproceduralCFG icfg= new JimpleBasedInterproceduralCFG();
        IFDSTabulationProblem<Unit, Pair<Value,
                                Set<DefinitionStmt>>, SootMethod,
                        InterproceduralCFG<Unit, SootMethod>> problem = new IFDSReachingDefinitions(icfg);

        IFDSSolver<Unit, Pair<Value, Set<DefinitionStmt>>,
                        SootMethod, InterproceduralCFG<Unit, SootMethod>> solver =
                new IFDSSolver<Unit, Pair<Value, Set<DefinitionStmt>>, SootMethod,
                        InterproceduralCFG<Unit, SootMethod>>(problem);

        System.out.println("Starting solver");
        solver.solve();
        System.out.println("Done");
    }
}
