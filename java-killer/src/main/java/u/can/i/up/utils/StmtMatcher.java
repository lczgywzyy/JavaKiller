package u.can.i.up.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import u.can.i.up.Transformer.ControlFlowTransformer;

import java.util.List;

public class StmtMatcher {

    private Logger logger = LogManager.getLogger();

    private volatile static StmtMatcher instance = new StmtMatcher();

    private StmtMatcher() {}

    public static StmtMatcher getInstance() {
        return instance;
    }

    public boolean checkStmt(Unit stmt) {
//        if (stmt == null || !(stmt instanceof JAssignStmt || !(stmt instanceof JInvokeStmt))) {
        if (stmt == null || !(stmt instanceof JInvokeStmt)) {
            return false;
        }
        SootMethodRef sootMethodRef = ((JInvokeStmt)stmt).getInvokeExpr().getMethodRef();
        boolean isStatic = sootMethodRef.isStatic();
        SootClass declaringClass = sootMethodRef.getDeclaringClass();
        String name = sootMethodRef.getName();
        List<Type> parameterTypes = sootMethodRef.getParameterTypes();
        Type returnType = sootMethodRef.getReturnType();
        return false;
    }
}
