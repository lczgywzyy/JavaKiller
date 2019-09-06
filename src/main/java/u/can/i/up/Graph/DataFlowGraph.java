package u.can.i.up.Graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import soot.*;
import soot.options.Options;
import u.can.i.up.Transformer.AnalysisTransformer;
import u.can.i.up.Transformer.IFDSDataFlowTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataFlowGraph {

    private volatile static DataFlowGraph instant;
    Logger logger = LogManager.getLogger();

    private DataFlowGraph() {}

    public static DataFlowGraph getInstance() {
        if (instant == null) {
            synchronized (DataFlowGraph.class) {
                if (instant == null) {
                    instant = new DataFlowGraph();
                }
            }
        }
        return instant;
    }

    // TODO API
    public void drawDFGWithAPI(String src_path, String classpath, String mainClass){
        initial(src_path, classpath, mainClass);
        drawDFGWithAPI_IFDSDataFlowTransformer();
    }

    private static void initial(String src_path, String classpath, String mainClass) {
        soot.G.reset();

        // Set Soot's SRC_PATH
        ArrayList<String> src_path_list = new ArrayList<>();
        src_path_list.add(src_path);
        Options.v().set_process_dir(src_path_list);

        // Set Soot's language
        Options.v().set_src_prec(Options.src_prec_java);

        Options.v().set_output_format(Options.output_format_jimple);

        // Set Soot's internal classpath
        Options.v().set_soot_classpath(classpath);

        // Enable whole-program mode
        Options.v().set_whole_program(true);
        Options.v().set_app(true);

        // Call-graph options
        Options.v().setPhaseOption("wjtp", "on");
        Options.v().setPhaseOption("cg", "safe-newinstance:true");
        Options.v().setPhaseOption("cg.cha","enabled:false");

        // Enable SPARK call-graph construction
        Options.v().setPhaseOption("cg.spark","enabled:true");
        Options.v().setPhaseOption("cg.spark","verbose:true");
        Options.v().setPhaseOption("cg.spark","on-fly-cg:true");

        Options.v().set_allow_phantom_refs(true);

        Options.v().set_keep_line_number(true);

        // Set the main class of the application to be analysed
        Options.v().set_main_class(mainClass);

        // Load the main class
        SootClass c = Scene.v().forceResolve(mainClass, SootClass.BODIES);
        c.setApplicationClass();
        Scene.v().loadNecessaryClasses();

        // Load the "main" method of the main class and set it as a Soot entry point
//        SootMethod entryPoint = c.getMethodByName("onCreate");
//        List<SootMethod> entryPoints = new ArrayList<SootMethod>();
//        entryPoints.add(entryPoint);
//        Scene.v().setEntryPoints(entryPoints);

        List<SootMethod> entryPoints = new ArrayList<SootMethod>();
        for (SootClass currentClass : Scene.v().getClasses()) {
            for (SootMethod method : currentClass.getMethods()) {
                if (method.hasActiveBody()) {
                    entryPoints.add(method);
                }
            }
        }
        Scene.v().setEntryPoints(entryPoints);
    }

    private void drawDFGWithAPI_IFDSDataFlowTransformer(){
//        PackManager.v().getPack("wjtp").add(new Transform("wjtp.herosifds", IFDSDataFlowTransformer.getInstance()));
        PackManager.v().runPacks();
//        soot.Main.main(new String[]{});
    }

    // TODO CMD
    public void drawDFGWithCMD(String src_path, String classpath, String mainClass){
        String[] args_1 = new String[]{"-process-dir", src_path,
                "-src-prec", "java",
                "-cp", classpath,
                "-w",
                "-main-class", mainClass,
                "-pp",
                "-app",
                "-p", "wjtp",
                "-keep-line-number",
                "-allow-phantom-refs"};
        String[] args_2 = new String[]{
                "-process-dir", src_path,
                "-cp", Scene.v().defaultClassPath() + File.pathSeparator + classpath, "-pp",
                "-w", 						// 执行整个程序分析
                "-src-prec", "java",		// 指定源文件类型
                "-main-class", mainClass,	// 指定主类
                "-f", "J", 					// 指定输出文件类型
                mainClass
        };
        String[] args_help = new String[]{"--help"};
        drawDFGWithCMD_IFDSDataFlowTransformer(args_1);
    }

    private void drawDFGWithCMD_IFDSDataFlowTransformer(String[] args){
        System.err.println("111");

//        PackManager.v().getPack("wjtp").add(new Transform("wjtp.herosifds", IFDSDataFlowTransformer.getInstance()));

        //        PackManager.v().getPack("wjtp").add(new Transform("wjtp.herosifds", new SceneTransformer() {
//            protected void internalTransform(String phaseName, Map options) {
//                System.err.println(Scene.v().getApplicationClasses());
//            }
//        }));
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.dfa", AnalysisTransformer.getInstance()));
        soot.Main.main(args);
        System.err.println("222");
    }
}
