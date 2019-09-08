package u.can.i.up;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;
import u.can.i.up.Graph.ControlFlowGraph;
import u.can.i.up.Models.Rules;
import u.can.i.up.utils.RulesReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainTest {

    Logger logger = LogManager.getLogger();

    @Test
    public void drawCFGWithCMD() {
        String dir = "/Users/lczgywzyy/Projects/ASProj/CoconutTest/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes";
        ControlFlowGraph cfg = ControlFlowGraph.getInstance();
        String[] args1 = new String[]{"-process-dir", dir,
                "-src-prec", "java",
                "-cp", "/Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home/jre/lib/rt.jar:/Users/lczgywzyy/Library/Android/sdk/platforms/android-29/android.jar",
                "-android-jars", "/Users/lczgywzyy/Library/Android/sdk/platforms",
                "-main-class", "MainActivity",
                "-pp",
                "-app",
                "-keep-line-number",
                "-allow-phantom-refs"};
        String[] args2 = new String[]{"--help"};
//        cfg.drawCFGWithCMD_PathExplorerTransformer(args1);
//        cfg.drawCFGWithCMD_IFDSDataFlowTransformer(args1);
    }
    @Test
    public void drawCFGWithAPI() {
        String dir = "/Users/lczgywzyy/Projects/ASProj/CoconutTest/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes";
        String mainClass = "MainActivity";
        ControlFlowGraph cfg = ControlFlowGraph.getInstance();
        cfg.drawCFGWithCMD(dir, "", mainClass);
    }

    public void drawCFGWithAPI2(){
        String process_dir = "/Users/lczgywzyy/Projects/ASProj/CoconutTest/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes";
        String mainClass = "MainActivity";
        String androidJarPath = "";

        Options.v().set_src_prec(Options.src_prec_java);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_keep_line_number(true);

        Options.v().set_process_dir(Collections.singletonList(process_dir));
        Options.v().set_android_jars("/Users/lczgywzyy/Library/Android/sdk/platforms");

        // Set Soot's internal classpath
        Options.v().set_soot_classpath(Scene.v().getSootClassPath());

        // Enable whole-program mode
        Options.v().set_whole_program(true);
        Options.v().set_app(true);

        // Call-graph options
        Options.v().setPhaseOption("cg", "safe-newinstance:true");
        Options.v().setPhaseOption("cg.cha","enabled:false");

        // Enable SPARK call-graph construction
        Options.v().setPhaseOption("cg.spark","enabled:true");
        Options.v().setPhaseOption("cg.spark","verbose:true");
        Options.v().setPhaseOption("cg.spark","on-fly-cg:true");

        Options.v().set_allow_phantom_refs(true);

//        Chain<SootClass> aa = Scene.v().getApplicationClasses();

        // Set the main class of the application to be analysed
//        Options.v().set_main_class(mainClass);

        // Load the main class
        SootClass c = Scene.v().loadClass(mainClass, SootClass.BODIES);
        c.setApplicationClass();

        PackManager.v().runPacks();

        // Load the "main" method of the main class and set it as a Soot entry point
        SootMethod entryPoint = c.getMethodByName("onCreate");
        List<SootMethod> entryPoints = new ArrayList<SootMethod>();
        entryPoints.add(entryPoint);
        Scene.v().setEntryPoints(entryPoints);

//        PackManager.v().getPack("wjtp").add(new Transform("wjtp.herosifds", PathExplorerTransformer.v()));
//        String[] args = new String[]{"--help"};
//        soot.Main.main(args);
    }
    @Test
    public void testJson(){
        Rules rulesModel = new Rules();
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader("conf/apis.json"));
            rulesModel = gson.fromJson(reader, Rules.class);
            Rules rules = RulesReader.getInstance().getApiRules();
            logger.info(rulesModel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testJson2(){
        RulesReader.getInstance().initialRules();
        Rules rules = RulesReader.getInstance().getApiRules();
        logger.info(rules);
    }
}