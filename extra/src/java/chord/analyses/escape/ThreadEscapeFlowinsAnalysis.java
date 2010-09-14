package chord.analyses.escape;

import chord.doms.DomH;
import chord.doms.DomV;
import chord.analyses.alias.DomC;
import chord.analyses.alias.Ctxt;
import chord.analyses.alias.CtxtsAnalysis;
import joeq.Compiler.Quad.Quad;
import chord.project.Chord;
import chord.project.ClassicProject;
import chord.project.analyses.ProgramRel;
import chord.project.analyses.JavaAnalysis;
import chord.util.Execution;

// Vanilla flow-insensitive k-CFA or k-obj analysis for thread escape.
// Need to do this in Java to specify which alias analysis task to run.
@Chord(name="thresc-flowins-java")
public class ThreadEscapeFlowinsAnalysis extends JavaAnalysis {
  Execution X = Execution.v("adaptive"); // What CtxtsAnalysis uses

  int relSize(String name) {
    ProgramRel rel = (ProgramRel)ClassicProject.g().getTrgt(name);
    rel.load();
    int size = rel.size();
    rel.close();
    return size;
  }

  public void run() {
    ClassicProject.g().runTask("argCopy-dlog");
    ClassicProject.g().runTask("retCopy-dlog");
    ClassicProject.g().runTask("ctxts-java");
    ClassicProject.g().runTask(CtxtsAnalysis.getCspaKind());
    ClassicProject.g().runTask("flowins-thresc-dlog");

    int numEscaping = relSize("flowinsEscE");
    int numLocal = relSize("flowinsLocE");

    DomH domH = (DomH)ClassicProject.g().getTrgt("H");
    DomV domV = (DomV)ClassicProject.g().getTrgt("V");
    DomC domC = (DomC)ClassicProject.g().getTrgt("C");

    int numVars = domV.size();
    boolean useObjectSensitivity = "co".equals(System.getProperty("chord.inst.ctxt.kind", null));
    
    int sizeA = 0;
    int sizeC = 0;
    for (Ctxt c : domC) {
      if (c.length() > 0 && domH.indexOf(c.head()) != -1) { // Allocation site
        sizeA++;
        if (useObjectSensitivity) sizeC++; // If object-based, this is a context as well
      }
      else
        sizeC++;
    }

    X.putOption("useObjectSensitivity", useObjectSensitivity);

    X.putOutput("numEscaping", numEscaping);
    X.putOutput("numLocal", numLocal);
    X.putOutput("totalSizeA", sizeA);
    X.putOutput("totalSizeC", sizeC);
    X.putOutput("totalSizeV", numVars*sizeC);

    X.finish(null);
  }
}
