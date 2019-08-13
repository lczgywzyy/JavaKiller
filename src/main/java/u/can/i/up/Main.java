package u.can.i.up;

public class Main {

    public static void main(String [] args){

        // bin/classed文件夹
        String dir = "/Users/lczgywzyy/Projects/ASProj/CoconutTest/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes";

        // 运行环境，包括jdk中rt.jar、Android SDK中android.jar等，路径中间用":"拼接；
        String classpath = "/Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home/jre/lib/rt.jar:/Users/lczgywzyy/Library/Android/sdk/platforms/android-29/android.jar";

        // Android 工程主类，主类可能会影响整个代码的控制流图.
        String mainClass = "MainActivity";

        ControlFlowGraph cfg = ControlFlowGraph.getInstance();
        cfg.drawCFGWithCMD(dir, classpath, mainClass);
    }

}
