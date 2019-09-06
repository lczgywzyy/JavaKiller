package u.can.i.up.Graph;

import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

import java.util.Iterator;

public class BackwardFlowAnalysisImpl extends BackwardFlowAnalysis {

    private FlowSet emptySet;

    /**
     * Construct the analysis from a DirectedGraph representation of a Body.
     *
     * @param graph
     */
    public BackwardFlowAnalysisImpl(DirectedGraph graph) {
        super(graph);
        emptySet = new ArraySparseSet();
        doAnalysis();//执行fixed-point
    }

    @Override
    protected void flowThrough(Object in, Object d, Object out) {
        FlowSet inSet = (FlowSet)in;
        FlowSet outSet = (FlowSet)out;
        Unit u = (Unit) d;
        kill(inSet, u, outSet);
        gen(outSet, u);
    }

    private void kill(FlowSet inSet, Unit u, FlowSet outSet) {
        // TODO Auto-generated method stub
        FlowSet kills = (FlowSet)emptySet.clone();//Unit的kills
        Iterator defIt = u.getDefBoxes().iterator();
        while(defIt.hasNext()) {
            ValueBox defBox = (ValueBox)defIt.next();

            if(defBox.getValue() instanceof Local){
                Iterator inIt = inSet.iterator();
                while(inIt.hasNext()) {
                    Local inValue = (Local)inIt.next();
                    if(inValue.equivTo(defBox.getValue())) {
                        kills.add(defBox.getValue());
                    }
                }
            }
        }
        inSet.difference(kills, outSet);
    }
    private void gen(FlowSet outSet, Unit u) {
        // TODO Auto-generated method stub
        Iterator useIt = u.getUseBoxes().iterator();
        while (useIt.hasNext()) {
            ValueBox e = (ValueBox) useIt.next();
            if (e.getValue() instanceof Local)
                outSet.add(e.getValue());
        }
    }

    @Override
    protected Object newInitialFlow() {
        // TODO Auto-generated method stub
        return emptySet.emptySet();
    }

    @Override
    protected Object entryInitialFlow() {
        // TODO Auto-generated method stub
        return emptySet.emptySet();
    }

    @Override
    protected void merge(Object in1, Object in2, Object out) {
        // TODO Auto-generated method stub
        FlowSet inSet1 = (FlowSet)in1;
        FlowSet inSet2 = (FlowSet)in2;
        FlowSet outSet = (FlowSet)out;
        //inSet1.union(inSet2, outSet);
        inSet1.intersection(inSet2, outSet);
    }

    @Override
    protected void copy(Object source, Object dest) {
        // TODO Auto-generated method stub
        FlowSet srcSet = (FlowSet)source;
        FlowSet destSet = (FlowSet)dest;
        srcSet.copy(destSet);
    }
}
