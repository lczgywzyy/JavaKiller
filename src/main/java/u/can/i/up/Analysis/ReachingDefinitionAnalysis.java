package u.can.i.up.Analysis;

import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

import java.util.Iterator;

public class ReachingDefinitionAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<Local>> {
    public ReachingDefinitionAnalysis(DirectedGraph g) {
        super(g);
        doAnalysis();
    }

    @Override
    protected void merge(FlowSet<Local> src1, FlowSet<Local> src2, FlowSet<Local> dest) {
        // dest <- src1 U src2
        src1.union(src2, dest);
    }


    @Override
    protected void copy(FlowSet<Local> srcSet, FlowSet<Local> destSet) {
        srcSet.copy(destSet);
    }

    @Override
    protected FlowSet<Local> newInitialFlow() {
        return new ArraySparseSet<Local>();
    }

    @Override
    protected FlowSet<Local> entryInitialFlow() {
        return new ArraySparseSet<Local>();
    }

    protected void flowThrough(FlowSet<Local> in, Unit d, FlowSet<Local> out) {
        // TODO Auto-generated method stub
        FlowSet inSet = (FlowSet)in,
                outSet = (FlowSet)out;
        Unit u = (Unit) d;
        kill(inSet,u,outSet);
        gen(outSet,u);
    }

    private void kill(FlowSet<Local> inSet, Unit u, FlowSet<Local> outSet) {
        // TODO Auto-generated method stub
        FlowSet kills = new ArraySparseSet<Local>();//Unit的kills
        Iterator defIt = u.getDefBoxes().iterator();
        while(defIt.hasNext()){
            ValueBox defBox = (ValueBox)defIt.next();

            if(defBox.getValue() instanceof Local){
                Iterator inIt = inSet.iterator();
                while(inIt.hasNext()){
                    Local inValue = (Local)inIt.next();
                    if(inValue.equivTo(defBox.getValue())){
                        kills.add(defBox.getValue());
                    }
                }
            }
        }
        inSet.difference(kills, outSet);
    }

    private void gen(FlowSet<Local> outSet, Unit u) {
        // TODO Auto-generated method stub
        Iterator useIt = u.getUseBoxes().iterator();
        while(useIt.hasNext()){
            ValueBox e = (ValueBox)useIt.next();
            if(e.getValue() instanceof Local)
                outSet.add((Local)e.getValue());
        }
    }
}
