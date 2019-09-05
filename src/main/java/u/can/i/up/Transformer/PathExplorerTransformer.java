package u.can.i.up.Transformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.JimpleBody;
import soot.toolkits.graph.CompleteUnitGraph;
import u.can.i.up.utils.UnitPair;

import java.util.*;

public class PathExplorerTransformer extends BodyTransformer {

    private Logger logger = LogManager.getLogger();

    private volatile static PathExplorerTransformer instance = new PathExplorerTransformer();

    private PathExplorerTransformer() {}

    public static PathExplorerTransformer getInstance() {
        return instance;
    }

    @Override
    protected void internalTransform(Body b, String phaseName,
                                     Map<String, String> options) {
        JimpleBody body = (JimpleBody) b;

        // For debug...
//        if(!body.getMethod().getDeclaringClass().getName().startsWith("com.coconuttest.tyu91.coconuttest.SmsTestActivity")){
//            return;
//        }

        // TODO 每一个body 对应类中的一个Declared Method.
//        G.v().out.println("- METHOD: " + body.getMethod().getDeclaration() + " | " + body.getMethod().getDeclaringClass());
        logger.info("- METHOD: " + body.getMethod().getDeclaration() + " | " + body.getMethod().getDeclaringClass());

        CompleteUnitGraph stmtGraph = new CompleteUnitGraph(body);

        Stack<Unit> entryPoints = new Stack<>();
        entryPoints.addAll(stmtGraph.getHeads());

        while (!entryPoints.empty()) {
            Stack<UnitPair> workList = new Stack<>();

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
//                    G.v().out.println("BACKEDGE\n");
                    logger.info("<--- BACKEDGE\n");
                    continue;
                }

                traversed.add(stmt);
                // TODO 打印当前语句，可以在此处判断语句中是否有特定API调用。
//                G.v().out.println(pair.toString());
                logger.info(pair.toString());

                List<Unit> successors = stmtGraph.getSuccsOf(stmt);

                if (successors.isEmpty()) {
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
