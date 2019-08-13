package u.can.i.up;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;

import java.util.Collections;

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

    private static void initial(String src_path, String mainClass) {
        soot.G.reset();
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_validate(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_output_dir("sootOutput");
        Options.v().set_src_prec(Options.src_prec_java);
        Options.v().set_android_jars("/Users/lczgywzyy/Library/Android/sdk/platforms");
        Options.v().set_process_dir(Collections.singletonList(src_path));//路径应为文件夹
        Options.v().set_keep_line_number(true);
        Options.v().set_whole_program(true);
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_soot_classpath(Scene.v().getSootClassPath()+ ":/Users/lczgywzyy/Library/Android/sdk/platforms/android-29/android.jar");
        Options.v().set_app(true);
//        Scene.v().setMainClass(Scene.v().loadClass(mainClass, SootClass.BODIES)); // how to make it work ?
        Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.Thread", SootClass.SIGNATURES);
        Scene.v().loadNecessaryClasses();
    }

    public void drawCFGWithCMD_PathExplorerTransformer(String[] args){
        PackManager.v().getPack("jtp").add(new Transform("jtp.propagator", PathExplorerTransformer.v()));
        soot.Main.main(args);
    }
    public void drawCFGWithCMD_IFDSDataFlowTransformer(String[] args){
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.herosifds", new IFDSDataFlowTransformer()));
        soot.Main.main(args);
    }
    public void drawCFGWithAPI(String src_path, String mainClass){
        initial(src_path, mainClass);
        SootClass appclass = Scene.v().loadClassAndSupport("MainActivity");//若无法找到，则生成一个。
        logger.info("" + appclass.getName());
    }
}
