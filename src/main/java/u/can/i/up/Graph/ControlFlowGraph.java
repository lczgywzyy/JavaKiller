package u.can.i.up.Graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import soot.PackManager;
import soot.Transform;
import u.can.i.up.Transformer.ControlFlowTransformer;
import u.can.i.up.Transformer.DataFlowTransformer;

public class ControlFlowGraph {

    private volatile static ControlFlowGraph instant;
    Logger logger = LogManager.getLogger();

    private ControlFlowGraph() {}

    public static ControlFlowGraph getInstance() {
        if (instant == null) {
            synchronized (ControlFlowGraph.class) {
                if (instant == null) {
                    instant = new ControlFlowGraph();
                }
            }
        }
        return instant;
    }

    /** @param dir
     *      bin文件夹路径;
     * @param classpath
     *      运行环境，包括jdk中rt.jar、Android SDK中android.jar等；
     * @param mainClass
     *      Android 工程主类，主类可能会影响整个代码的控制流图.
     */
    public void drawCFGWithCMD(String dir, String classpath, String mainClass) {
        ControlFlowGraph cfg = ControlFlowGraph.getInstance();
        String[] args = new String[]{"-process-dir", dir,
                "-src-prec", "java",
                "-cp", classpath,
                "-main-class", mainClass,
                "-pp",
                "-app",
                "-keep-line-number",
                "-allow-phantom-refs"};
        cfg.drawCFGWithCMD_PathExplorerTransformer(args);
    }

    private void drawCFGWithCMD_PathExplorerTransformer(String[] args){
        PackManager.v().getPack("jtp").add(new Transform("jtp.propagator", ControlFlowTransformer.getInstance()));
//        PackManager.v().getPack("jtp").add(new Transform("jtp.propagator", DataFlowTransformer.getInstance()));
//        PackManager.v().getPack("wjtp").add(new Transform("wjtp.dfa", AnalysisTransformer.getInstance()));
        soot.Main.main(args);
    }
}
