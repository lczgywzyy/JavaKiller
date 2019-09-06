package u.can.i.up.Transformer;

import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import soot.*;
import soot.jimple.DefinitionStmt;
import soot.jimple.toolkits.ide.exampleproblems.IFDSReachingDefinitions;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.toolkits.scalar.Pair;

import java.util.Map;
import java.util.Set;

public class IFDSDataFlowTransformer extends SceneTransformer {

    private volatile static IFDSDataFlowTransformer instance = new IFDSDataFlowTransformer();

    private IFDSDataFlowTransformer() {}

    public static IFDSDataFlowTransformer getInstance() {
        return instance;
    }

    private Logger logger = LogManager.getLogger();

    @Override
    protected void internalTransform(String phaseName, Map<String, String> options) {
        System.err.println(Scene.v().getApplicationClasses());
//        logger.info("aaa");
//        JimpleBasedInterproceduralCFG icfg= new JimpleBasedInterproceduralCFG();
//        IFDSTabulationProblem<Unit, Pair<Value,
//                Set<DefinitionStmt>>, SootMethod,
//                InterproceduralCFG<Unit, SootMethod>> problem = new IFDSReachingDefinitions(icfg);
//
//        IFDSSolver<Unit, Pair<Value, Set<DefinitionStmt>>,
//                SootMethod, InterproceduralCFG<Unit, SootMethod>> solver =
//                new IFDSSolver<Unit, Pair<Value, Set<DefinitionStmt>>, SootMethod,
//                        InterproceduralCFG<Unit, SootMethod>>(problem);
//
//        System.out.println("Starting solver");
//        solver.solve();
//        System.out.println("Done");
    }
}
