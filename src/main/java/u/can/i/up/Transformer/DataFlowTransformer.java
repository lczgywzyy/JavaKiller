package u.can.i.up.Transformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.JimpleBody;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import u.can.i.up.Analysis.ReachingDefinitionAnalysis;
import u.can.i.up.utils.UnitPair;

import java.util.*;

public class DataFlowTransformer extends BodyTransformer {

    private Logger logger = LogManager.getLogger();

    private volatile static DataFlowTransformer instance = new DataFlowTransformer();

    private DataFlowTransformer() {}

    public static DataFlowTransformer getInstance() {
        return instance;
    }

    @Override
    protected void internalTransform(Body b, String phaseName,
                                     Map<String, String> options) {
        JimpleBody body = (JimpleBody) b;

        // For debug...
        if(!body.getMethod().getDeclaringClass().getName().startsWith("com.coconuttest.tyu91.coconuttest.ContactsTestActivity")){
            return;
        }

        // TODO 每一个body 对应类中的一个Declared Method.
//        G.v().out.println("- METHOD: " + body.getMethod().getDeclaration() + " | " + body.getMethod().getDeclaringClass());
        logger.info("- METHOD: " + body.getMethod().getDeclaration() + " | " + body.getMethod().getDeclaringClass());

        CompleteUnitGraph stmtGraph = new CompleteUnitGraph(body);

        Stack<Unit> entryPoints = new Stack<>();
        entryPoints.addAll(stmtGraph.getHeads());

        while (!entryPoints.empty()) {
            Stack<UnitPair> workList = new Stack<>();

            // TODO ---> NEW ENTRY POINT
//            G.v().out.println("NEW ENTRY POINT");
            logger.info("---> NEW ENTRY POINT");
            Unit entry = entryPoints.pop();

            workList.add(new UnitPair(entry, 1));
            Set<Unit> traversed = new HashSet<>();

            while (!workList.isEmpty()) {
                UnitPair pair = workList.pop();
                Unit stmt = pair.getUnit();
                int level = pair.getLevel();

                if (traversed.contains(stmt)) {
                    // TODO <--- BACKEDGE
//                    G.v().out.println("BACKEDGE\n");
                    logger.info("<--- BACKEDGE\n");
                    continue;
                }

                traversed.add(stmt);
                // TODO 打印当前语句，可以在此处判断语句中是否有特定API调用。
//                G.v().out.println(pair.toString());
                logger.info(pair.toString());
                if (pair.toString().contains("query")) {
                    UnitGraph graph = new BriefUnitGraph(body);
                    // 执行活跃变量分析
                    ReachingDefinitionAnalysis analysis = new ReachingDefinitionAnalysis(graph);
                    FlowSet dataflow_before = analysis.getFlowBefore(pair.getUnit());
                    FlowSet dataflow_after = analysis.getFlowAfter(pair.getUnit());
                    logger.info("~~~~~~~~~~~" + dataflow_before);
                    logger.info("~~~~~~~~~~~" + dataflow_after);

//                    Set<String> entrySet = new HashSet<>();
//                    Set<String> exitSet = new HashSet<>();
//                    Iterator<Unit> unitIt = graph.iterator();
//                    while (unitIt.hasNext()) {
//                        String entryVals = "";
//                        String exitVals = "";
//                        FlowSet<Local> set_dataflow;
//                        Unit s = unitIt.next();
//                        set_dataflow = analysis.getFlowBefore(s);
//                        for (Local local: set_dataflow) {
//                            entryVals = local + "->" + entryVals;
//                            entrySet.add(entryVals);
//                        }
//                        set_dataflow = analysis.getFlowAfter(s);
//                        for (Local local: set_dataflow) {
//                            exitVals = exitVals + "->" + local;
//                            exitSet.add(exitVals);
//                        }
//                    }
//                    logger.info("Entry Set:");
//                    for (String ss: entrySet) {
//                        logger.info(ss);
//                    }
//                    logger.info("Exit Set:");
//                    for (String ss: exitSet) {
//                        logger.info(ss);
//                    }
                }

                List<Unit> successors = stmtGraph.getSuccsOf(stmt);

                if (successors.isEmpty()) {
                    // TODO ==== LAST STATEMENT
//                    G.v().out.println("LAST STATEMENT\n");
                    logger.info("==== LAST STATEMENT\n");
                } else {
                    List<UnitPair> successorPairs = new ArrayList<>();
                    for (Unit successor : successors) {
                        successorPairs.add(new UnitPair(successor, level + 1));
                    }
                    workList.addAll(successorPairs);
                }
            }
        }

    }
}
